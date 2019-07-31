package com.tokopedia.nps;

/**
 * Created by meta on 03/10/18.
 */
public class NpsConstant {

    public class Key {
        public static final String APP_RATING = "APP_RATING";
        public static final String KEY_RATING = "RATING";
        public static final String KEY_APP_RATING_VERSION = "APP_RATING_VERSION";
        public static final String KEY_ADVANCED_APP_RATING_VERSION = "ADVANCED_APP_RATING_VERSION";
    }

    public class Analytic {
        public static final String IMPRESSION_APP_RATING = "impressionAppRating";
        public static final String CLICK_APP_RATING = "clickAppRating";
        public static final String CANCEL_APP_RATING = "cancelAppRating";

        public static final String APP_RATING = "Application Rating";
        public static final String IMPRESSION = "Impression";

        public static final String CLICK = "Click";
    }

    public class Feedback {
        public static final int GOOD_RATING_THRESHOLD = 3;
        public static final String PACKAGE_CONSUMER_APP = "com.tokopedia.tkpd";
        public static final String APPLINK_PLAYSTORE = "market://details?id=";
        public static final String URL_PLAYSTORE = "https://play.google.com/store/apps/details?id=";
    }
}
