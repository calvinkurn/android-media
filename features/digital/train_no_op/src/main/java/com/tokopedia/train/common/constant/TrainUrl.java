package com.tokopedia.train.common.constant;

public class TrainUrl {

    public static String BASE_URL = "http://gql-staging.tokopedia.com";
    public static String WEB_DOMAIN = "https://tiket.tokopedia.com/";
    public static String BASE_WEB_DOMAIN = "https://www.tokopedia.com/";
    public static String KAI_WEBVIEW = WEB_DOMAIN + "kereta-api";

    private static String PATH_USER_BOOKING_LIST = "/user/bookings";
    private static String PARAM_DIGITAL_ISPULSA = "?ispulsa=1";

    public static String TRAIN_ORDER_LIST = KAI_WEBVIEW + PATH_USER_BOOKING_LIST + PARAM_DIGITAL_ISPULSA;

}