package com.tokopedia.config;

/**
 * Created by ricoharisin on 11/21/16.
 */

public class GlobalConfig {

    public static final int SELLER_APPLICATION = 2;
    public static final int CONSUMER_APPLICATION = -1;
    public static final int CONSUMER_PRO_APPLICATION = 3;

    public static final String PACKAGE_SELLER_APP = "com.tokopedia.sellerapp";
    public static final String PACKAGE_CONSUMER_APP = "com.tokopedia.customerappp";

    public static String VERSION_NAME = "1.0";
    public static String VERSION_NAME_SUFFIX = "production";
    public static String RAW_VERSION_NAME = "1.0";

    public static int VERSION_CODE = 1;
    public static String FLAVOR = "liveProd";
    public static int APPLICATION_TYPE = CONSUMER_APPLICATION;
    public static String PACKAGE_APPLICATION = PACKAGE_CONSUMER_APP;
    public static Boolean DEBUG = false;
    public static boolean ENABLE_DISTRIBUTION = false;
    public static boolean IS_PREINSTALL = false;
    public static String PREINSTALL_NAME = "";
    public static String PREINSTALL_DESC = "";
    public static String PREINSTALL_SITE = "";
    public static String APPLICATION_ID = "";
    public static String DEVICE_ID = "";

    /**
     * This field is used to enable the debug trace to find the performance insights. Used by automation test for perforamnce
     */
    public static Boolean ENABLE_DEBUG_TRACE = false;

    /**
     * This field is used to pass the list of  trace names for which tracing need to be recorded if debug trace is enabled
     */
    public static String[] DEBUG_TRACE_NAME = {};

    // use to set default root Activity for incoming deeplink
    public static String HOME_ACTIVITY_CLASS_NAME = "";
    // if the deeplink can be handled by multiple activities, this DeeplinkHandlerActivity and DeeplinkActivity
    // should be the least priority
    public static String DEEPLINK_HANDLER_ACTIVITY_CLASS_NAME = "";
    public static String DEEPLINK_ACTIVITY_CLASS_NAME = "";

    public static String INTERNAL_FILE_DIR = "";
    public static String INTERNAL_CACHE_DIR = "";
    public static String EXTERNAL_FILE_DIR = "";
    public static String EXTERNAL_CACHE_DIR = "";

    public static boolean isSellerApp() {
        return APPLICATION_TYPE == SELLER_APPLICATION;
    }

    public static String getPackageApplicationName() {
        return PACKAGE_APPLICATION;
    }

    public static Boolean isAllowDebuggingTools() {
        return DEBUG || ENABLE_DISTRIBUTION;
    }
}
