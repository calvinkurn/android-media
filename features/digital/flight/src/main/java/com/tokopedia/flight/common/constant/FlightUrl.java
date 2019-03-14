package com.tokopedia.flight.common.constant;

/**
 * Created by nathan on 10/24/17.
 */

public class FlightUrl {

    public static final String FLIGHT_PATH = "travel/v1/flight/";
    public static final String FLIGHT_PATH_V11 = "travel/v1.1/flight/";
    public static final String FLIGHT_AIRPORT_PATH = FLIGHT_PATH + "dropdown/airport";
    public static final String FLIGHT_AIRLINE_PATH = FLIGHT_PATH + "dropdown/airline";
    public static final String FLIGHT_EMAIL = FLIGHT_PATH + "order/resend";
    public static final String FLIGHT_SEARCH_SINGLE = FLIGHT_PATH + "search/single";
    public static final String FLIGHT_SEARCH_COMBINED = FLIGHT_PATH + "search/combined";
    public static final String FLIGHT_CLASS_PATH = FLIGHT_PATH + "dropdown/class";
    public static final String FLIGHT_CART_PATH = FLIGHT_PATH + "cart";
    public static final String FLIGHT_CART_PATH_V11 = FLIGHT_PATH_V11 + "cart";
    public static final String FLIGHT_CHECK_VOUCHER_CODE = FLIGHT_PATH + "voucher/check";
    public static final String FLIGHT_CANCEL_VOUCHER_CODE = FLIGHT_PATH + "voucher/cancel";
    public static final String FLIGHT_PASSENGER_SAVED = FLIGHT_PATH + "passenger";
    public static final String FLIGHT_CANCELLATION_PATH = FLIGHT_PATH + "cancel/";
    public static final String FLIGHT_CANCELLATION_PATH_V11 = FLIGHT_PATH_V11 + "cancel/";
    public static final String FLIGHT_CANCELLATION_PASSENGER = FLIGHT_CANCELLATION_PATH + "passenger";
    public static final String FLIGHT_CANCELLATION_ESTIMATE_REFUND = FLIGHT_CANCELLATION_PATH + "estimate";
    public static final String FLIGHT_CANCELLATION_REQUEST = FLIGHT_CANCELLATION_PATH_V11 + "request";
    public static final String FLIGHT_VERIFY_BOOKING = "travel/v1/oms/verify";
    public static final String FLIGHT_CHECKOUT_BOOKING = "travel/v1/oms/checkout";
    public static final String FLIGHT_ORDERS = FLIGHT_PATH + "order/list";
    public static final String FLIGHT_ORDER = FLIGHT_PATH + "order/{id}";
    public static final String FLIGHT_PROMO = FLIGHT_PATH + "promo/banner";
    public static final String PROMO_PATH =  "promo/";
    public static final String CONTACT_US_PATH = "contact-us";
    public static final String CONTACT_US_FLIGHT_HOME_PREFIX = "?pid=97&flag_app=1&device=android&utm_source=android";
    public static final String CONTACT_US_FLIGHT_PREFIX = "?pid=46&ivtype=4";
    public static final String CATEGORY_ID = "27";
    public static final String TNC_LINK = "https://www.tokopedia.com/bantuan/pengembalian-dana-dan-penggantian-jadwal";
    public static String BASE_URL = "https://api-staging.tokopedia.com";
    public static String WEB_DOMAIN = "https://www.tokopedia.com/";
    public static String ALL_PROMO_LINK = WEB_DOMAIN + "promo/";
    public static String CONTACT_US = WEB_DOMAIN + "contact-us";
    public static String CONTACT_US_FLIGHT = WEB_DOMAIN + CONTACT_US_FLIGHT_HOME_PREFIX;
    public static String CONTACT_US_FLIGHT_PREFIX_GLOBAL = WEB_DOMAIN + "contact-us?pid=46&ivtype=4";
    public static String AIRLINES_CONTACT_URL = WEB_DOMAIN + "bantuan/kontak-maskapai-penerbangan/";

    public static String getUrlPdf(String orderId, String filename, String userId) {
        return WEB_DOMAIN + "pesawat/pdf/generate?invoice_id=" + orderId + "&pdf=" + filename + "&user_id=" + userId;
    }

    public static String getUrlInvoice(String orderId, String userId) {
        return WEB_DOMAIN + "pesawat/invoice?invoice_id=" + orderId + "&user_id=" + userId;
    }
}
