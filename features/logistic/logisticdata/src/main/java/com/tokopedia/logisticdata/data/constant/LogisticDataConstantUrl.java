package com.tokopedia.logisticdata.data.constant;

/**
 * @author anggaprasetiyo on 09/05/18.
 */
public class LogisticDataConstantUrl {

    public static String BASE_DOMAIN = "https://ws.tokopedia.com/";

    public static class KeroRates {
        static public String BASE_URL = "https://gw.tokopedia.com/";

        public static final String LIVE_BASE_URL = "https://gw.tokopedia.com/";
        public static final String STAGING_BASE_URL = "https://gw-staging.tokopedia.com//";
        public static final String ALPHA_BASE_URL = "https://gw-alpha.tokopedia.com/";

        public static final String HMAC_KEY = "web_service_v4";
        public static final String VERSION = "v2/";
        static final String BASE_PATH = "";

        public static final String PATH_RATES_V2 = VERSION + BASE_PATH + "rates";
    }

    public static class PeopleAction {
        static public String BASE_URL = "https://ws.tokopedia.com/";

        public static final String HMAC_KEY = "web_service_v4";
        public static final String VERSION = "v4/";
        static final String BASE_PATH = "action/people/";

        public static final String PATH_EDIT_ADDRESS = VERSION + BASE_PATH + "edit_address.pl";
    }

    public static class CourierTracking {
        public static String BASE_URL = BASE_DOMAIN + "v4/tracking-order/";
        public static final String PATH_TRACK_ORDER = "track_order.pl";
    }
}
