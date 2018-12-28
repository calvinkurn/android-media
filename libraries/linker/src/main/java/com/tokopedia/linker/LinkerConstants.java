package com.tokopedia.linker;

public class LinkerConstants {
    public static final String PRODUCTTYPE_DIGITAL = "digital";
    public static final String PRODUCTTYPE_MARKETPLACE = "marketplace";
    public static final String BRANCH_PROMOCODE_KEY = "branch_promo";
    public static final String REFERRAL_ADVOCATE_PROMO_CODE = "";
    public static final String ANDROID_DESKTOP_URL_KEY = "$android_url";
    public static final String IOS_DESKTOP_URL_KEY = "$ios_url";
    public static final String ProductCategory = "ProductCategory";
    public static final String FIREBASE_KEY_INCLUDEMOBILEWEB = "app_branch_include_mobileweb";
    public static final String CHALLENGES_DESKTOP_URL = "https://m.tokopedia.com/kontes";
    public static final String REFERRAL_DESKTOP_URL = "https://www.tokopedia.com/referral";
    public static final String PRODUCT_INFO = "tokopedia://product/{product_id}";
    public static final String REFERRAL_WELCOME = "tokopedia://referral/{code}/{owner}";
    public static final String SHOP = "tokopedia://shop/{shop_id}";
    public static final String DISCOVERY_HOTLIST_DETAIL = "tokopedia://hot/{alias}";
    public static final String DISCOVERY_CATALOG = "tokopedia://catalog/{EXTRA_CATALOG_ID}";
    public static final String GROUPCHAT = "tokopedia://groupchat/{channel_id}";
    public static final String PROMO_DETAIL = "tokopedia://promo/{slug}";
    public static final String APPLINKS = "tokopedia";
    public static final String WEB_DOMAIN = "https://www.tokopedia.com/";
    public static final String MOBILE_DOMAIN = "https://m.tokopedia.com/";
    public static final String FEATURE_GATE_BRANCH_LINKS = "mainapp_activate_branch_links";
    public static final String APP_SHOW_REFERRAL_BUTTON = "app_show_referral_button";
    public static final String STRING_FORMAT_TAG = "%s - %s";
    public static final String STRING_FORMAT_DESKTOP_URL = "%s%s";
    public static final String STRING_FORMAT_GROUP_CHAT = "groupchat/%s%sutm_source=%s&utm_medium=%s&utm_campaign=%s&utm_content=%s";
    public static final String STRING_FORMAT_UTM = "utm_source=%s&utm_medium=%s&utm_campaign=%s&utm_content=%s";
    public static final String STRING_FORMAT_CHANNEL = "%s - Android";
    public static final String KEY_URI_REDIRECT_MODE = "$uri_redirect_mode";
    public static final String VALUE_URI_REDIRECT_MODE = "2";
    public static final String REGEX_APP_LINK = "\\{.*?\\} ?";

    public static final String ID = "id";
    public static final String PRICE = "price";
    public static final String PRICE_IDR_TO_DOUBLE = "priceIDRtoDouble";
    public static final String BRAND = "price";
    public static final String NAME = "name";
    public static final String VARIANT = "variant";
    public static final String QTY = "qty";
    public static final String CATEGORY = "category";

    public static final int EVENT_COMMERCE_VAL = 1;
    public static final int EVENT_LOGIN_VAL = 2;
    public static final int EVENT_LOGOUT_VAL = 3;
    public static final int EVENT_USER_REGISTRATION_VAL = 4;
    public static final int EVENT_USER_IDENTITY = 5;

    public static final String KEY_ANDROID_DEEPLINK_PATH = "$android_deeplink_path";
    public static final String KEY_IOS_DEEPLINK_PATH = "$ios_deeplink_path";
    public static final String KEY_DESKTOP_URL = "$desktop_url";
    public static final String KEY_PAYMENT = "paymentID";
    public static final String KEY_PRODUCTTYPE = "productType";
    public static final String KEY_USERID = "userId";
    public static final String KEY_OG_URL = "$og_url";
    public static final String KEY_OG_TITLE = "$og_title";
    public static final String KEY_OG_IMAGE_URL = "$og_image_url";
    public static final String KEY_OG_DESC = "$og_description";
    public static final String KEY_GA_CLIENT_ID = "$google_analytics_client_id";

    public static final String EMAIL_LABLE = "email";
    public static final String PHONE_LABLE = "phone";
    public static final String EVENT_LOGIN_LABLE = "login";
    public static final String EVENT_REGISTER_LABLE = "sign_up";

    public static final int ERROR_INIT_FAILED = 101;
    public static final int ERROR_REQUEST_NOT_SUCCESSFUL = 102;
    public static final int ERROR_SOMETHING_WENT_WRONG = 103;

    public static final String MSG_UNINITIALIZED_LINKER = "Linker manager not initialized";

}
