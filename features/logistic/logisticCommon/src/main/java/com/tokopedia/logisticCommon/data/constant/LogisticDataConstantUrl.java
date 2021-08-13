package com.tokopedia.logisticCommon.data.constant;

import com.tokopedia.url.TokopediaUrl;

/**
 * @author anggaprasetiyo on 09/05/18.
 */
public class LogisticDataConstantUrl {

    public static String BASE_DOMAIN = TokopediaUrl.Companion.getInstance().getWS();

    public static class KeroRates {
        static public String BASE_URL = TokopediaUrl.Companion.getInstance().getGW();

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
        static final String PEOPLE_PATH = "people/";

        public static final String PATH_EDIT_ADDRESS = VERSION + BASE_PATH + "edit_address.pl";

        public static final String PATH_ADD_ADDRESS = VERSION + BASE_PATH + "add_address.pl";

        public static final String PATH_GET_ADDRESS = VERSION + PEOPLE_PATH + "get_address.pl";

        public static final String PATH_EDIT_DEFAULT_ADDRESS = VERSION + BASE_PATH + "edit_default_address.pl";

        public static final String PATH_DELETE_ADDRESS = VERSION + BASE_PATH + "delete_address.pl";
    }

    public static class CourierTracking {
        public static String BASE_URL = BASE_DOMAIN + "v4/tracking-order/";
        public static final String PATH_TRACK_ORDER = "track_order.pl";
    }
}
