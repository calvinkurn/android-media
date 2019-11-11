package com.tokopedia.flight.orderlist.constant;

/**
 * Created by nathan on 10/24/17.
 */

public class FlightOrderUrl {

    public static final String FLIGHT_PATH = "travel/v1/flight/";
    public static final String FLIGHT_EMAIL = FLIGHT_PATH + "order/resend";
    public static final String FLIGHT_ORDERS = FLIGHT_PATH + "order/list";
    public static final String FLIGHT_ORDER = FLIGHT_PATH + "order/{id}";

}
