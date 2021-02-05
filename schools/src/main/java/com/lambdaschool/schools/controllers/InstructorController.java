package com.lambdaschool.schools.controllers;

import com.lambdaschool.schools.models.Instructor;
import com.lambdaschool.schools.models.Slip;
import com.lambdaschool.schools.models.SlipReturnData;
import com.lambdaschool.schools.services.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@RestController
@RequestMapping(value = "/instructors")
public class InstructorController
{
    @Autowired
    private InstructorService instructorService;

    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping(value = "/instructor/{instructorid}/advice", produces = "application/json")
    public ResponseEntity<?> getAdvice(@PathVariable long instructorid)
    {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_HTML));
        restTemplate.getMessageConverters().add(converter);

        String requestURL = "https://api.adviceslip.com/advice";
        ParameterizedTypeReference<SlipReturnData> responseType = new ParameterizedTypeReference<>()
        {
        };

        ResponseEntity<SlipReturnData> responseEntity = restTemplate.exchange(requestURL,
                HttpMethod.GET,
                null,
                responseType);

        Slip advice = responseEntity.getBody().getSlip();

        Instructor newInstructor = instructorService.addAdvice(instructorid,
                advice);
        return new ResponseEntity<>(newInstructor,
                HttpStatus.OK);


    }
}
