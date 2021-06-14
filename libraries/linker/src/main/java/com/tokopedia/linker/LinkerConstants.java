package com.tokopedia.linker;

public interface LinkerConstants {
    String PRODUCTTYPE_DIGITAL = "digital";
    String PRODUCTTYPE_MARKETPLACE = "marketplace";
    String FEATURE_TYPE_HOTEL = "hotelShareURL";
    String BRANCH_PROMOCODE_KEY = "branch_promo";
    String REFERRAL_ADVOCATE_PROMO_CODE = "";
    String ANDROID_DESKTOP_URL_KEY = "$android_url";
    String IOS_DESKTOP_URL_KEY = "$ios_url";
    String ProductCategory = "ProductCategory";
    String FIREBASE_KEY_INCLUDEMOBILEWEB = "app_branch_include_mobileweb";
    String CHALLENGES_DESKTOP_URL = "https://m.tokopedia.com/kontes";
    String REFERRAL_DESKTOP_URL = "https://www.tokopedia.com/referral";
    String PRODUCT_INFO = "tokopedia://product/{product_id}";
    String REFERRAL_WELCOME = "tokopedia://referral/{code}/{owner}";
    String SHOP = "tokopedia://shop/{shop_id}";
    String DISCOVERY_HOTLIST_DETAIL = "tokopedia://hot/{alias}";
    String DISCOVERY_CATALOG = "tokopedia://catalog/{EXTRA_CATALOG_ID}";
    String GROUPCHAT = "tokopedia://groupchat/{channel_id}";
    String PROMO_DETAIL = "tokopedia://promo/{slug}";
    String APPLINKS = "tokopedia";
    String WEB_DOMAIN = "https://www.tokopedia.com/";
    String MOBILE_DOMAIN = "https://m.tokopedia.com/";
    String FEATURE_GATE_BRANCH_LINKS = "mainapp_activate_branch_links";
    String APP_SHOW_REFERRAL_BUTTON = "app_show_referral_button";
    String enableBranchReinitFlow = "android_enable_branch_reinitflow";
    String ENABLE_XIAOMI_PAI_TRACKING = "android_enable_xiaomi_pai_tracking";
    String STRING_FORMAT_TAG = "%s - %s";
    String STRING_FORMAT_DESKTOP_URL = "%s%s";
    String STRING_FORMAT_GROUP_CHAT = "groupchat/%s%sutm_source=%s&utm_medium=%s&utm_campaign=%s&utm_content=%s";
    String STRING_FORMAT_UTM = "utm_source=%s&utm_medium=%s&utm_campaign=%s&utm_content=%s";
    String STRING_FORMAT_CHANNEL = "%s - Android";
    String KEY_URI_REDIRECT_MODE = "$uri_redirect_mode";
    String VALUE_URI_REDIRECT_MODE = "2";
    String REGEX_APP_LINK = "\\{.*?\\} ?";

    String ID = "id";
    String PRICE = "price";
    String PRICE_IDR_TO_DOUBLE = "priceIDRtoDouble";
    String BRAND = "price";
    String NAME = "name";
    String VARIANT = "variant";
    String QTY = "qty";
    String CATEGORY = "category";

    int EVENT_COMMERCE_VAL = 1;
    int EVENT_LOGIN_VAL = 2;
    int EVENT_LOGOUT_VAL = 3;
    int EVENT_USER_REGISTRATION_VAL = 4;
    int EVENT_USER_IDENTITY = 5;
    int EVENT_ITEM_VIEW = 6;
    int EVENT_ADD_TO_WHISHLIST =7;
    int EVENT_ADD_TO_CART = 8;
    int EVENT_PURCHASE_FLIGHT = 9;
    int EVENT_DIGITAL_HOMEPAGE = 10;
    int EVENT_DIGITAL_SCREEN_LAUNCH = 11;

    String KEY_ANDROID_DEEPLINK_PATH = "$android_deeplink_path";
    String KEY_IOS_DEEPLINK_PATH = "$ios_deeplink_path";
    String KEY_DESKTOP_URL = "$desktop_url";
    String KEY_PAYMENT = "paymentID";
    String KEY_PRODUCTTYPE = "productType";
    String KEY_USERID = "userId";
    String KEY_OG_URL = "$og_url";
    String KEY_OG_TITLE = "$og_title";
    String KEY_OG_IMAGE_URL = "$og_image_url";
    String KEY_OG_DESC = "$og_description";
    String KEY_GA_CLIENT_ID = "$google_analytics_client_id";
    String KEY_NEW_BUYER = "new_buyer";
    String KEY_MONTHLY_NEW_BUYER = "monthly_new_buyer";

    String EMAIL_LABLE = "email";
    String PHONE_LABLE = "phone";
    String USER_ID = "userId";
    String MEDIUM = "medium";
    String EVENT_LOGIN_LABLE = "login";
    String EVENT_REGISTER_LABLE = "sign_up";
    String EVENT_FLIGHT_PURCHASE = "FLIGHT_PURCHASE ";
    String EVENT_MARKETPLACE_FIRST_TXN = "marketplace_first_txn";

    // Recharge Events
    String EVENT_DIGITAL_SUBHOMEPAGE_LAUNCHED = "digital_subhomepage_launched";
    String EVENT_DIGITAL_CATEGORY_LAUNCHED = "digital_category_screen_launched";

    String HOTEL_LABEL = "hotel";
    String PDP_LABEL = "pdp";
    String SHARE_LABEL = "Share";

    int ERROR_INIT_FAILED = 101;
    int ERROR_REQUEST_NOT_SUCCESSFUL = 102;
    int ERROR_SOMETHING_WENT_WRONG = 103;

    String MSG_UNINITIALIZED_LINKER = "Linker manager not initialized";

    String PRODUCT_ID = "product_id";
    String CATEGORY_LEVEL_1 = "Category Level 1";
    String DESCRIPTION = "description";
    String SHOP_ID = "shop_id";
    String CURRENCY = "currency";
    String PRODUCT_CATEGORY = "product_category";
    String PRODUCT_NAME = "product_name";
    String JOURNEY_ID = "journey_id";
    String INVOICE_ID = "invoice_id";
    String PRODUCT_TYPE = "productType";

    // Recharge Constants
    String PRODUCT_TYPE_DIGITAL = "Digital";

    String BRANCH_UTM_SOURCE = "~channel";
    String BRANCH_UTM_MEDIUM = "~feature";
    String BRANCH_CAMPAIGN = "~campaign";

    String UTM_SOURCE = "utm_source";
    String UTM_MEDIUM = "utm_medium";
    String UTM_CAMPAIGN = "utm_campaign";
    String UTM_TERM = "utm_term";
    String SCREEN_NAME_KEY = "screenName";
    String SCREEN_NAME_VALUE = "Deeplink Page";

    String CONTENT = "content";
    String CONTENT_TYPE = "content_type";
    String LEVEL1_NAME = "level1_name";
    String LEVEL1_ID = "level1_id";
    String LEVEL2_NAME = "level2_name";
    String LEVEL2_ID = "level2_id";
    String LEVEL3_NAME = "level3_name";
    String LEVEL3_ID = "level3_id";
    String SKU = "sku";
    String CONTENT_ID = "content_id";
    String BRANCH_LINK_DOMAIN_1="tokopedia.link";
    String BRANCH_LINK_DOMAIN_2="tokopedia-alternate.app.link";
    String BRANCH_LINK_DOMAIN_3="tokopedia.app.link";
}
