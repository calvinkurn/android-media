package com.tokopedia.logisticdata.data.constant;

/**
 * @author anggaprasetiyo on 09/05/18.
 */
public class LogisticDataConstantUrl {

    public static class KeroRates {
        static public String BASE_URL = "https://gw.tokopedia.com/";

        public static final String HMAC_KEY = "web_service_v4";
        public static final String VERSION = "v2/";
        static final String BASE_PATH = "";

        public static final String PATH_RATES_V2 = VERSION + BASE_PATH + "rates";
    }

    public static class CourierTracking {
        public static String BASE_URL = "https://ws.tokopedia.com/v4/tracking-order/";
        public static final String PATH_TRACK_ORDER = "track_order.pl";
    }
}
