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
        FlightErrorConstant.FLIGHT_ROUTE_NOT_FOUND, FlightErrorConstant.FAILED_ADD_FACILITY,
        FlightErrorConstant.ERROR_PROMO_CODE, FlightErrorConstant.FLIGHT_DUPLICATE_BOOKING,
        FlightErrorConstant.FLIGHT_STILL_IN_PROCESS, FlightErrorConstant.FLIGHT_INVALID_USER,
        FlightErrorConstant.FLIGHT_DUPLICATE_USER_NAME})
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
    String FAILED_ADD_FACILITY = "108";
    String ERROR_PROMO_CODE = "28";
    String FLIGHT_DUPLICATE_BOOKING = "86";
    String FLIGHT_STILL_IN_PROCESS = "44";
    String FLIGHT_INVALID_USER = "22";
    String FLIGHT_DUPLICATE_USER_NAME = "62";
    String FLIGHT_ERROR_GET_CART_EXCEED_MAX_RETRY = "1335";
    String FLIGHT_ERROR_VERIFY_EXCEED_MAX_RETRY = "1337";
    String FLIGHT_ERROR_ON_CHECKOUT_GENERAL = "1339";
}
