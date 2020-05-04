package com.tokopedia.flight.common.constant;

import com.tokopedia.url.TokopediaUrl;

/**
 * Created by nathan on 10/24/17.
 */

public class FlightUrl {

    public static final String FLIGHT_PATH = "travel/v1/flight/";
    public static final String FLIGHT_PATH_V11 = "travel/v1.1/flight/";
    public static final String FLIGHT_SEARCH_SINGLE = FLIGHT_PATH + "search/single";
    public static final String FLIGHT_SEARCH_COMBINED = FLIGHT_PATH + "search/combined";
    public static final String FLIGHT_CANCELLATION_PATH = FLIGHT_PATH + "cancel/";
    public static final String FLIGHT_CANCELLATION_PATH_V11 = FLIGHT_PATH_V11 + "cancel/";
    public static final String FLIGHT_CANCELLATION_PASSENGER = FLIGHT_CANCELLATION_PATH + "passenger";
    public static final String FLIGHT_CANCELLATION_ESTIMATE_REFUND = FLIGHT_CANCELLATION_PATH + "estimate";
    public static final String FLIGHT_CANCELLATION_REQUEST = FLIGHT_CANCELLATION_PATH_V11 + "request";
    public static final String CONTACT_US_FLIGHT_HOME_PREFIX = "contact-us?pid=97&flag_app=1&device=android&utm_source=android";
    public static String BASE_URL = TokopediaUrl.Companion.getInstance().getAPI();
    public static String WEB_DOMAIN = TokopediaUrl.Companion.getInstance().getWEB();
    public static String CONTACT_US_FLIGHT = WEB_DOMAIN + CONTACT_US_FLIGHT_HOME_PREFIX;
    public static String AIRLINES_CONTACT_URL = WEB_DOMAIN + "bantuan/kontak-maskapai-penerbangan/";

    public static String getUrlPdf(String orderId, String filename, String userId) {
        return WEB_DOMAIN + "pesawat/pdf/generate?invoice_id=" + orderId + "&pdf=" + filename + "&user_id=" + userId;
    }

    public static String getUrlInvoice(String orderId, String userId) {
        return WEB_DOMAIN + "pesawat/invoice?invoice_id=" + orderId + "&user_id=" + userId;
    }
}
