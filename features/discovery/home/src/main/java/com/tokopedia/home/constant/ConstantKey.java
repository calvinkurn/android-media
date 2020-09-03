package com.tokopedia.home.constant;

public class ConstantKey {
    public class RemoteConfigKey {
        public static final String APP_SHOW_RECOMENDATION_BUTTON = "mainapp_show_recomendation_button";
        public static final String APP_SHOW_TOKOPOINT_NATIVE = "app_enable_tokopoint_native";
        public static final String MAINAPP_NATIVE_PROMO_LIST = "mainapp_native_promo_list";
        public static final String REMOTE_CONFIG_KEY_FIRST_INSTALL_SEARCH = "android_user_first_install_search";
        public static final String REMOTE_CONFIG_KEY_FIRST_DURATION_TRANSITION_SEARCH = "android_user_duration_auto_transition_search";
        public static final String AB_TEST_REVIEW_KEY = "InboxUlasanRevamp2";
        public static final String AB_TEST_AUTO_TRANSITION_KEY = "auto_transition";
    }

    public class TkpdCache {
        public static final String HOME_DATA_CACHE = "HOME_DATA_CACHE";
        public static final String CACHE_RECHARGE_WIDGET_TAB_SELECTION = "CACHE_RECHARGE_WIDGET_TAB_SELECTION";
        public static final String WIDGET_RECHARGE_TAB_LAST_SELECTED = "WIDGET_RECHARGE_TAB_LAST_SELECTED";
        public static final String EXPLORE_DATA_CACHE = "EXPLORE_DATA_CACHE";
    }

    public class RequestKey {
        public static final String USER_ID = "userId";
        public static final String DEFAULT_USER_ID = "";
    }

    public class Analytics {
        public class AppScreen {
            public class UnifyTracking {
                public static final String SCREEN_UNIFY_HOME_BERANDA = "/";
            }
        }
    }

    public class LocationCache {
        public static final String KEY_LOCATION = "KEY_FP_LOCATION";
        public static final String KEY_LOCATION_LAT = "KEY_FP_LOCATION_LAT";
        public static final String KEY_LOCATION_LONG = "KEY_FP_LOCATION_LONG";
    }

    public class FirstInstallCache {
        public static final String KEY_FIRST_INSTALL_SEARCH = "KEY_FIRST_INSTALL_SEARCH";
        public static final String KEY_FIRST_INSTALL_TIME_SEARCH = "KEY_IS_FIRST_INSTALL_TIME_SEARCH";
    }

    public class ABtestValue {
        public static final String VALUE_NEW_REVIEW_FLOW = "New Review Flow";
        public static final String AUTO_TRANSITION_VARIANT = "default";
    }
}
