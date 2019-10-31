package com.tokopedia.flight.common.constant;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by User on 11/28/2017.
 */

@StringDef({FlightErrorConstant.INVALID_JSON, FlightErrorConstant.INVALID_USER_ID,
        FlightErrorConstant.DUPLICATE_IDEMPOTENCY_KEY, FlightErrorConstant.GET_RESULT,
        FlightErrorConstant.CONNECT_REDIS, FlightErrorConstant.MISSING_REQUIRED_PARAM,
        FlightErrorConstant.INVALID_DATE, FlightErrorConstant.ADD_TO_CART,
        FlightErrorConstant.SAVE_CART, FlightErrorConstant.METHOD_NOT_ALLOWED,
        FlightErrorConstant.FLIGHT_SOLD_OUT, FlightErrorConstant.FLIGHT_EXPIRED,
        FlightErrorConstant.FLIGHT_ROUTE_NOT_FOUND})
@Retention(RetentionPolicy.SOURCE)
public @interface FlightErrorConstant {
    String INVALID_JSON = "1";
    String INVALID_USER_ID = "2";
    String DUPLICATE_IDEMPOTENCY_KEY = "3";
    String GET_RESULT = "4";
    String CONNECT_REDIS = "5";
    String MISSING_REQUIRED_PARAM = "6";
    String INVALID_DATE = "7";
    String ADD_TO_CART = "8";
    String SAVE_CART = "9";
    String METHOD_NOT_ALLOWED = "10";
    String FLIGHT_SOLD_OUT = "56";
    String FLIGHT_EXPIRED = "83";
    String FLIGHT_ROUTE_NOT_FOUND = "81";
}
