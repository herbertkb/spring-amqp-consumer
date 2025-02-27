package com.example.amqp.consumer;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import static org.apache.camel.LoggingLevel.INFO;


@Component
public class ConsumerRouteBuilder extends RouteBuilder {

    @Value("${MAX_BODY_LENGTH}")
    private Integer maxBodyLength;

    @Value("${DELAY}")
    private Integer delay;

    @Override
    public void configure() throws Exception {
        from("amqp:{{destination}}")
            .id("amqp-consumer")
            .delay(delay)
            .process(exchange -> {
                String body = exchange.getIn().getBody(String.class);
                int bodyLength = body.length()  < maxBodyLength ? body.length() : maxBodyLength;
                exchange.getIn().setBody(body.substring(0, bodyLength));                    
            })
            .log(INFO, "Received: ${body}");
    }
}
