package com.tokopedia.core.gcm;

/**
 * @author by alvarisi on 12/20/16.
 *         this class contain all string used for applink/PN case
 */

@Deprecated
public interface Constants {
    String ARG_NOTIFICATION_CODE = "tkp_code";
    String ARG_NOTIFICATION_TITLE = "title";
    String ARG_NOTIFICATION_DESCRIPTION = "desc";
    String ARG_NOTIFICATION_IMAGE = "url_img";
    String ARG_NOTIFICATION_BANNER = "url_banner";
    String ARG_NOTIFICATION_ICON = "url_icon";
    String ARG_NOTIFICATION_URL = "url";
    String ARG_NOTIFICATION_TARGET_USER_ID = "to_user_id";
    String ARG_NOTIFICATION_CART_EXISTS = "is_cart_exists";
    String ARG_NOTIFICATION_APPLINK = "applinks";
    String ARG_NOTIFICATION_APPLINK_MESSAGE = "message";
    String ARG_NOTIFICATION_APPLINK_SELLER_INFO = "seller_info";
    String ARG_NOTIFICATION_APPLINK_DISCUSSION = "talk";
    String ARG_NOTIFICATION_APPLINK_DISCUSSION_CUSTOM_INDEX = "sender_id";
    String ARG_NOTIFICATION_APPLINK_PROMO_LABEL = "promo";
    String ARG_NOTIFICATION_APPLINK_TOPCHAT = "topchat";
    String KEY_ORIGIN = "origin";
    int REGISTRATION_STATUS_OK = 1;
    int REGISTRATION_STATUS_ERROR = 2;
    String REGISTRATION_MESSAGE_OK = "FCM Sucessfully";
    String REGISTRATION_MESSAGE_ERROR = "FCM Error";
    String EXTRA_APPLINK = "applink_url";
    String EXTRA_APPLINK_FROM_PUSH = "applink_from_notif";
    String EXTRA_PUSH_PERSONALIZATION = "EXTRA_PUSH_PERSONALIZATION";
    String EXTRA_APPLINK_CATEGORY = "applink_category";
    String EXTRA_APPLINK_FROM_INTERNAL = "EXTRA_APPLINK_FROM_INTERNAL";
    int ARG_NOTIFICATION_APPLINK_MESSAGE_ID = 1001;
    int ARG_NOTIFICATION_APPLINK_DISCUSSION_ID = 1002;
    int ARG_NOTIFICATION_APPLINK_PROMO = 1003;

    String ACTION_BC_RESET_APPLINK = "com.tokopedia.tkpd.APPLINK_ACTION";
    String ARG_NOTIFICATION_APPLINK_LOGIN_REQUIRED = "login_required";

    String FROM_APP_SHORTCUTS = "FROM_APP_SHORTCUTS" ;

    /**
     * @deprecated extends {@link com.tokopedia.abstraction.constant.TkpdAppLink} on module instead
     */
    @Deprecated
    interface Applinks {
        String HOME = "tokopedia://home";
        String SHOP = "tokopedia://shop/{shop_id}";
        String SHOP_ETALASE = "tokopedia://shop/{shop_id}/etalase/{etalase_id}";
        String CART = "tokopedia://cart";
        String CREATE_SHOP = "tokopedia://buka-toko-online-gratis";
        String REPUTATION = "tokopedia://review";
        String PRODUCT_REPUTATION = "tokopedia://product/{product_id}/review";
        String WEBVIEW = "tokopedia://webview";
        String DIGITAL = "tokopedia://digital";
        String DIGITAL_CART = "tokopedia://digital/cart";
        String DIGITAL_CATEGORY = "tokopedia://digital/category";
        String DISCOVERY_PAGE = "tokopedia://discovery/{page_id}";
        String PROMO = "tokopedia://promo";
        String PROMO_CATEGORY = "tokopedia://promo/{promo_id}";
        String PROMO_WITH_DASH = "tokopedia://promo/{promo_id}/";
        String DISCOVERY_CATEGORY = "tokopedia://category";
        String DISCOVERY_CATEGORY_DETAIL = "tokopedia://category/{DEPARTMENT_ID}";
        String DISCOVERY_SEARCH = "tokopedia://search";
        String DISCOVERY_HOTLIST_DETAIL = "tokopedia://hot/{alias}";
        String DISCOVERY_CATALOG = "tokopedia://catalog/{EXTRA_CATALOG_ID}";
        String WISHLIST = "tokopedia://wishlist";
        String LOGIN = "tokopedia://login";
        String OFFICIAL_STORES = "tokopedia://official-stores";
        String OFFICIAL_STORES_PROMO = "tokopedia://official-stores/promo/{slug}";
        String OFFICIAL_STORE_PROMO = "tokopedia://official-store/promo/{slug}";
        String OFFICIAL_STORES_PROMO_TERMS = "tokopedia://official-stores/promo-terms";
        String PROMO_SALE = "tokopedia://sale/{slug}/";
        String PROMO_SALE_NO_SLASH = "tokopedia://sale/{slug}";
        String BRAND_LIST = "tokopedia://official-store/brand";
        String BRAND_LIST_WITH_SLASH = "tokopedia://official-store/brand/";
        String PROMO_SALE_TERMS = "tokopedia://promo-sale/promo-terms";
        String SUB_PROMO = "tokopedia://sale/{slug}/{category_slug}";
        String SUB_PROMO_WITH_SLASH = "tokopedia://sale/{slug}/{category_slug}/";
        String BROWSER = "tokopedia://browser";

        interface SellerApp {
            String SALES = "sellerapp://sales";
            String TOPADS_CREDIT = "sellerapp://topads/buy";
            String TOPADS_PRODUCT_CREATE = "sellerapp://topads/create";
            String GOLD_MERCHANT = "sellerapp://gold";
            String SELLER_APP_HOME = "sellerapp://home";
            String TOPADS_DASHBOARD = "sellerapp://topads";
            String TOPADS_PRODUCT_DETAIL = "sellerapp://topads/product/{ad_id}";
            String TOPADS_PRODUCT_DETAIL_CONSTS = "sellerapp://topads/product";
            String BROWSER = "sellerapp://browser";
        }
    }

    interface Schemes {
        String HTTP = "http";
        String HTTPS = HTTP + "s";
        String APPLINKS = "tokopedia";
        String APPLINKS_SELLER = "sellerapp";
    }

    //NOTE: strings must be same with {@link pref_notification.xml}
    interface Settings {
        String NOTIFICATION_PROMO = "notification_receive_promo";
        String NOTIFICATION_PM = "notification_receive_pm";
        String NOTIFICATION_TALK = "notification_receive_talk";
        String NOTIFICATION_REVIEW = "notification_receive_review";
        String NOTIFICATION_REP = "notification_receive_reputation";
        String NOTIFICATION_SALES = "notification_sales";
        String NOTIFICATION_PURCHASE = "notification_purchase";
        String NOTIFICATION_RESCENTER = "notification_receive_rescenter";
        String NOTIFICATION_SELLER_INFO = "notification_seller_info";
        String NOTIFICATION_GROUP_CHAT = "notification_group_chat";
    }
}