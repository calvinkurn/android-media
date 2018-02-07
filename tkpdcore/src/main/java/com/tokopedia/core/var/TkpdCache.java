package com.tokopedia.core.var;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.drawer2.view.DrawerHelper;

/**
 * Created by Nisie on 11/11/15.
 */
public class TkpdCache extends com.tokopedia.abstraction.constant.TkpdCache {

    public static final String ADD = "ADD";
    public static final String NOTIFICATION_DATA = "NOTIFICATION_DATA";
    public static final String USER_INFO = "USER_INFO";
    public static final String INDEX = "INDEX";
    public static final String ETALASE_ADD_PROD = "ETALASE_ADD_PROD";
    public static final String G_CODE = "G_CODE";
    public static final String LOCA_CODE = "LOCA_CODE";
    public static final String LOCA_STATUS_UPDATE = "LOCA_STATUS_UPDATE";
    public static final String LOCA_GCM_NOTIFICATION = "LOCA_GCM_NOTIFICATION";
    public static final String GCM_NOTIFICATION = "GCM_NOTIFICATION";

    public static final String LOGIN_ID = "LOGIN_ID";
    public static final String REGISTERED = "REGISTERED";
    public static final String LOGIN_UUID = "LOGIN_UUID";
    public static final String MAINTENANCE = "MAINTENANCE";
    public static final String SHIPPING = "SHIPPING";
    public static final String SERVICE = "SERVICE-";
    public static final String DISTRICT = "DISTRICT-";
    public static final String CITY = "CITY-";
    public static final String CATALOG = "catalog";
    public static final String DECLINE_REVIEW = "DECLINE_REVIEW";
    public static final String VERIFICATION_NUMBER = "VERIFICATION_NUMBER";
    public static final String GCM_STORAGE = "GCM_STORAGE";
    public static final String FIRST_TIME = "FIRST_TIME";
    public static final String CACHE_MAIN = "CACHE_MAIN";
    public static final String CACHE_PROMO = "CACHE_PROMO";
    public static final String NETWORK_HANDLER_CONFIG_GENERAL = "NETWORK_HANDLER_CONFIG_GENERAL";
    public static final String NETWORK_URL_KEY = "NETWORK_URL";
    public static final String APP_INFO = "APP_INFO";
    public static final String ALLOW_REFRESH = "ALLOW_REFRESH";
    public static final String EXCLUSION = "EXCLUSION";
    public static final String CACHE_RECHARGE_WIDGET_TAB_SELECTION = "CACHE_RECHARGE_WIDGET_TAB_SELECTION";
    public static final String DEFAULT_GRID_SETTINGS = "DEFAULT_GRID_SETTINGS";
    public static final String DIGITAL_LAST_INPUT_CLIENT_NUMBER = "DIGITAL_LAST_INPUT_CLIENT_NUMBER";
    public static final String DIGITAL_INSTANT_CHECKOUT_HISTORY = "DIGITAL_INSTANT_CHECKOUT_HISTORY";
    public static final String DIGITAL_USSD_MOBILE_NUMBER = "DIGITAL_USSD_MOBILE_NUMBER";
    public static final String CACHE_API = "CACHE_API";
    public static String LAST_BCA = "LAST_BCA";
    public static String LAST_MANDIRI = "LAST_MANDIRI";
    public static String LAST_BRI = "LAST_BRI";
    public static String LIST_REPORT_TYPE = "LIST_REPORT_TYPE";
    public static final String DIGITAL_WIDGET_LAST_ORDER = "DIGITAL_WIDGET_LAST_ORDER";
    public static final String FIREBASE_REMOTE_CONFIG = "FIREBASE_REMOTE_CONFIG";
    public static final String APP_RATING = "APP_RATING";

    public static void clearAllCache(Context context) {
        LocalCacheHandler.clearCache(context, TkpdCache.USER_INFO);
        LocalCacheHandler.clearCache(context, DrawerHelper.DRAWER_CACHE);
        LocalCacheHandler.clearCache(context, TkpdCache.ETALASE_ADD_PROD);
        LocalCacheHandler.clearCache(context, TkpdCache.REGISTERED);
        LocalCacheHandler.clearCache(context, TkpdCache.VERIFICATION_NUMBER);
        LocalCacheHandler.clearCache(context, TkpdCache.CACHE_MAIN);
        LocalCacheHandler.clearCache(context, TkpdCache.CACHE_PROMO);
    }

    public class Key extends com.tokopedia.abstraction.constant.TkpdCache.Key {

        public static final String UNIVERSEARCH = "universearch";
        public static final String DISTRICT_ID = "district_id";
        public static final String DISTRICT_NAME = "district_name";

        public static final String CITY_ID = "city_id";
        public static final String CITY_NAME = "city_name";

        public static final String IS_VALID = "is_valid";

        public static final String IS_HAS_CART = "is_has_cart";
        public static final String TOTAL_CART = "total_cart";

        public static final String ALLOW_SHOP = "allow_shop";

        public static final String NAME = "name";
        public static final String PRICE = "price";
        public static final String WEIGHT = "weight";
        public static final String ORDER = "order";
        public static final String DESC = "desc";
        public static final String CURR = "curr";
        public static final String WEIGHT_TYPE = "weight_type";
        public static final String ETALASE = "etalase";
        public static final String CONDITION = "condition";
        public static final String INSURANCE = "insurance";
        public static final String ADD_TO = "add_to";
        public static final String QTY1 = "qty1";
        public static final String QTY2 = "qty2";
        public static final String PRC = "prc";
        public static final String IMG = "img";
        public static final String SERVER = "server";
        public static final String IMG_DESC = "img_desc";

        public static final String NOTIFICATION_PASS_DATA = "notification_pass_data";
        public static final String NOTIFICATION_CONTENT = "notification_content";
        public static final String NOTIFICATION_DESC = "notification_desc";
        public static final String NOTIFICATION_CODE = "notification_code";
        public static final String PREV_CODE = "prev_code";
        public static final String PREV_TIME = "prev_time";

        public static final String TOTAL_NOTIF = "total_notif";
        public static final String MESSAGE_COUNT = "message_count";

        public static final String BDAY = "bday";
        public static final String USER_NAME = "user_name";

        public static final String LOGIN_ID = "LOGIN_ID";
        public static final String VERIFIED = "verified";
        public static final String UUID = "uuid";
        public static final String MESSAGE = "message";
        public static final String STATUS2 = "STATUS";
        public static final String NEED_PASSWORD = "need_password";

        public static final String SHIPPING_NAME = "shipping_name";
        public static final String ALL_SHIPPING_NAME = "all_shipping_name";
        public static final String ALL_SHIPPING_ID = "all_shipping_id";
        public static final String SHIPPING_ID = "shipping_id";

        public static final String SERVICE_NAME = "service_name";
        public static final String SERVICE_ID = "service_id";

        public static final String EXPIRY = "expiry";
        public static final String CATALOG = "catalog_";
        public static final String DELAY = "delay";

        public static final String PURCHASE_COUNT = "purchase_count";
        public static final String PURCHASE = "purchase";
        public static final String SALES_COUNT = "sales_count";
        public static final String USER_PHONE_NUMBER = "user_phone_number";
        public static final String GCM_ID = "gcm_id";
        public static final String IS_FIRST_TIME = "is_first_time";

        public static final String RECENT_PRODUCT_ALL = "RECENT_PRODUCT_ALL";
        public static final String RECENT_PRODUCT = "RECENT_PRODUCT";
        public static final String FAV_SHOP = "FAV_SHOP";
        public static final String PRODUCT_FEED = "PRODUCT_FEED";
        public static final String WISHLIST = "WISHLIST";
        public static final String WISHLIST_ALL = "WISHLIST_ALL";
        public static final String FAVORITE_SHOP = "FAVORITE_SHOP";
        public static final String RECOMMEND_SHOP = "RECOMMEND_SHOP";
        public static final String FAVORITE_PAGING = "FAVORITE_PAGING";
        public static final String TOP_ADS_SHOP = "TOP_ADS_SHOP";

        public static final String PROMO = "PROMO";

        public static final String HOT_LIST = "hot_list";

        public static final String TIMEOUT = "timeout";
        public static final String MAX_RETRIES = "max_retries";
        public static final String MULTI = "multi";
        public static final String HOST = "host";
        public static final String IS_HTTP = "is_http";
        public static final String PROXY_ADDRESS = "proxy_address";
        public static final String PROXY_PORT = "proxy_port";

        public static final String DB_VERSION = "DB_VERSION";
        public static final String APP_VERSION = "APP_VERSION";

        public static final String URL = "URL";

        public static final String ACCOUNT_NUMBER = "acc";

        public static final String TIMESTAMP = "timestamp";
        public static final java.lang.String DEPOSIT = "deposit";
        public static final java.lang.String LOYALTY = "loyalty";
        public static final java.lang.String LOYALTY_URL = "loyalty_url";
        public static final java.lang.String USER_SHOP = "user_shop";
        public static final java.lang.String USER_PIC_URI = "user_pic_uri";
        public static final java.lang.String SHOP_PIC_URI = "shop_pic_uri";
        public static final java.lang.String SHOP_ID = "shop_id";
        public static final java.lang.String IS_PRODUCT_MANAGER = "is_product_manager";
        public static final String SALES = "sales";
        public static final String TOTAL_AVAIL = "total_avail";
        public static final java.lang.String INC_NOTIF = "inc_notif";
        public static final String SHOP_INFO = "SHOP_INFO";
        public static final String SHOP_NOTE_LIST = "SHOP_NOTE_LIST";
        public static final String KEBIJAKAN_PENGEMBALIAN_PRODUK = "KEBIJAKAN_PENGEMBALIAN_PRODUK";

        public static final String SHOP_SCORE_SUMMARY = "SHOP_SCORE_SUMMARY";
        public static final String SHOP_SCORE_DETAIL = "SHOP_SCORE_DETAIL";

        public static final String WIDGET_RECHARGE_TAB_LAST_SELECTED = "WIDGET_RECHARGE_TAB_LAST_SELECTED";

        public static final String CATEOGRY_HEADER_LEVEL = "CATEGORY_LEVEL_";

        public static final String DIGITAL_CLIENT_NUMBER_CATEGORY = "DIGITAL_CLIENT_NUMBER_CATEGORY_";
        public static final String DIGITAL_OPERATOR_ID_CATEGORY = "DIGITAL_OPERATOR_ID_CATEGORY_";
        public static final String DIGITAL_PRODUCT_ID_CATEGORY = "DIGITAL_PRODUCT_ID_CATEGORY_";
        public static final String DIGITAL_INSTANT_CHECKOUT_LAST_IS_CHECKED_CATEGORY = "DIGITAL_INSTANT_CHECKOUT_LAST_IS_CHECKED_CATEGORY_";

        public static final String DIGITAL_CATEGORY_ITEM_LIST = "DIGITAL_CATEGORY_ITEM_LIST";
        public static final String DIGITAL_LAST_ORDER = "DIGITAL_LAST_ORDER";

        public static final String KEY_TOKOCASH_DATA = "TOKOCASH_DATA";

        public static final String KEY_LOCATION = "KEY_FP_LOCATION";
        public static final String KEY_LOCATION_LAT = "KEY_FP_LOCATION_LAT";
        public static final String KEY_LOCATION_LONG = "KEY_FP_LOCATION_LONG";

        public static final String KEY_USSD_SIM1 = "KEY_USSD_SIM1";
        public static final String KEY_USSD_SIM2 = "KEY_USSD_SIM2";

        public static final String SHOW_HIDE_APP_SHARE_BUTTON_KEY = "SHOW_HIDE_APP_SHARE_BUTTON_KEY";
        public static final String APP_SHARE_DESCRIPTION_KEY = "APP_SHARE_DESCRIPTION_KEY";
        public static final String MAINAPP_ACTIVATE_BRANCH_LINKS_KEY = "MAINAPP_ACTIVATE_BRANCH_LINKS_KEY";

        public static final String KEY_TOKOCASH_BALANCE_CACHE = "TOKOCASH_BALANCE_CACHE";

        public static final String CONFIG_SHOW_HIDE_APP_SHARE_BUTTON = "mainapp_show_app_share_button";
        public static final String CONFIG_APP_SHARE_DESCRIPTION = "app_share_description";
        public static final String CONFIG_MAINAPP_ACTIVATE_BRANCH_LINKS = "mainapp_activate_branch_links";
        public static final String HOME_CATEGORY_CACHE = "HOME_CATEGORY_CACHE";
        public static final String HOME_BRAND_OS_CACHE = "HOME_BRAND_OS_CACHE";
        public static final String HOME_BANNER_CACHE = "HOME_BANNER_CACHE";
        public static final String HOME_TOP_PICK_CACHE = "HOME_TOP_PICK_CACHE";
        public static final String HOME_TICKER_CACHE = "HOME_TICKER_CACHE";
        public static final String KEY_TOKOPOINT_DRAWER_DATA = "KEY_TOKOPOINT_DRAWER_DATA";
        public static final String KEY_APP_RATING_VERSION = "APP_RATING_VERSION";
        public static final String KEY_ADVANCED_APP_RATING_VERSION = "ADVANCED_APP_RATING_VERSION";
    }

    public class RemoteConfigKey {
        public static final String MAINAPP_SHOW_APP_SHARE_BUTTON = "mainapp_show_app_share_button";
        public static final String APP_SHARE_DESCRIPTION = "app_share_description";
        public static final String MAINAPP_ACTIVATE_BRANCH_LINKS = "mainapp_activate_branch_links";

        public static final String TOKO_CASH_TOP_UP = "toko_cash_top_up";
        public static final String TOKO_CASH_LABEL = "toko_cash_label";

        public static final String MAINAPP_RATING_TITLE = "mainapp_rating_title";
        public static final String MAINAPP_RATING_MESSAGE = "mainapp_rating_message";
        public static final String MAINAPP_SHOW_SIMPLE_APP_RATING = "mainapp_show_simple_app_rating";
        public static final String MAINAPP_SHOW_ADVANCED_APP_RATING = "mainapp_show_advanced_app_rating";

        public static final String SELLERAPP_SHOW_ADVANCED_APP_RATING = "sellerapp_show_advanced_app_rating";

        public static final String NOTIFICATION_LOGGER = "notification_logger";
    }

}
