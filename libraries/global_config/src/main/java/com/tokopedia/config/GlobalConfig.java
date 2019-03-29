package com.tokopedia.config;

/**
 * Created by ricoharisin on 11/21/16.
 */

public class GlobalConfig {

    public static final int SELLER_APPLICATION = 2;
    public static final int CONSUMER_APPLICATION = -1;

    public static final String PACKAGE_SELLER_APP = "com.tokopedia.sellerapp";
    public static final String PACKAGE_CONSUMER_APP = "com.tokopedia.customerappp";

    public static String VERSION_NAME = "1.0";
    public static int VERSION_CODE = 1;
    public static int APPLICATION_TYPE = CONSUMER_APPLICATION;
    public static String PACKAGE_APPLICATION = PACKAGE_CONSUMER_APP;
    public static Boolean DEBUG = false;
    public static boolean ENABLE_DISTRIBUTION = false;
    public static boolean IS_PREINSTALL = false;
    public static String PREINSTALL_NAME = "";
    public static String PREINSTALL_DESC = "";
    public static String PREINSTALL_SITE = "";

    public static boolean isSellerApp(){
        return APPLICATION_TYPE == SELLER_APPLICATION;
    }

    public static String getPackageApplicationName() {
        return PACKAGE_APPLICATION;
    }

    public static Boolean isAllowDebuggingTools() {
        return DEBUG || ENABLE_DISTRIBUTION;
    }
}
