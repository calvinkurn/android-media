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
    String ARG_NOTIFICATION_UPDATE_APPS_TITLE = "title_update";
    String ARG_NOTIFICATION_TARGET_USER_ID = "to_user_id";
    String ARG_NOTIFICATION_CART_EXISTS = "is_cart_exists";
    String ARG_NOTIFICATION_APPLINK = "applinks";
    String ARG_NOTIFICATION_ISPROMO = "ispromo";
    String ARG_NOTIFICATION_APPLINK_MESSAGE = "message";
    String ARG_NOTIFICATION_APPLINK_SELLER_INFO = "seller_info";
    String ARG_NOTIFICATION_APPLINK_MESSAGE_CUSTOM_INDEX = "sender_id";
    String ARG_NOTIFICATION_APPLINK_DISCUSSION = "talk";
    String ARG_NOTIFICATION_APPLINK_DISCUSSION_CUSTOM_INDEX = "sender_id";
    String ARG_NOTIFICATION_APPLINK_RIDE = "ride";
    String ARG_NOTIFICATION_APPLINK_PROMO_LABEL = "promo";
    String ARG_NOTIFICATION_APPLINK_TOPCHAT = "topchat";
    String KEY_ORIGIN = "origin";
    int REGISTRATION_STATUS_OK = 1;
    int REGISTRATION_STATUS_ERROR = 2;
    String REGISTRATION_MESSAGE_OK = "FCM Sucessfully";
    String REGISTRATION_MESSAGE_ERROR = "FCM Error";
    String URL_MARKET = "market://details?id=";
    String EXTRA_PLAYSTORE_URL = "market://details?id=com.tokopedia.tkpd";
    String EXTRA_FROM_PUSH = "from_notif";
    String EXTRA_APPLINK = "applink_url";
    String EXTRA_APPLINK_FROM_PUSH = "applink_from_notif";
    String EXTRA_UNREAD = "unread";
    String EXTRA_PUSH_PERSONALIZATION = "EXTRA_PUSH_PERSONALIZATION";
    String EXTRA_APPLINK_CATEGORY = "applink_category";
    String EXTRA_APPLINK_RESET = "applink_reset";
    String EXTRA_APPLINK_FROM_INTERNAL = "EXTRA_APPLINK_FROM_INTERNAL";
    int ARG_NOTIFICATION_APPLINK_MESSAGE_ID = 1001;
    int ARG_NOTIFICATION_APPLINK_DISCUSSION_ID = 1002;
    int ARG_NOTIFICATION_APPLINK_PROMO = 1003;
    String APPLINK_CUSTOMER_SCHEME = "tokopedia";

    String MOE_KEY_URL = "gcm_webUrl";

    String ACTION_BC_RESET_APPLINK = "com.tokopedia.tkpd.APPLINK_ACTION";
    String ARG_NOTIFICATION_APPLINK_LOGIN_REQUIRED = "login_required";

    String WEB_PLAYSTORE_BUYER_APP_URL = "https://play.google.com/store/apps/details?id=com.tokopedia.tkpd";
    String FROM_APP_SHORTCUTS = "FROM_APP_SHORTCUTS" ;

    /**
     * @deprecated extends {@link com.tokopedia.abstraction.constant.TkpdAppLink} on module instead
     */
    @Deprecated
    interface Applinks {
        String HOME = "tokopedia://home";
        String HOME_FEED = "tokopedia://home/feed";
        String FEED = "tokopedia://feed";
        String FEED_DETAILS = "tokopedia://feedcommunicationdetail/{extra_detail_id}";
        String HOME_CATEGORY = "tokopedia://home/category";
        String HOME_HOTLIST = "tokopedia://hot";
        String MESSAGE = "tokopedia://message";
        String MESSAGE_DETAIL = "tokopedia://message/{message_id}";
        String TALK = "tokopedia://talk";
        String TALK_DETAIL = "tokopedia://talk/{talk_id}";
        String RIDE = "tokopedia://ride/uber";
        String RIDE_DETAIL = "tokopedia://ride/uber/{request_id}";
        String SHOP = "tokopedia://shop/{shop_id}";
        String SHOP_ETALASE = "tokopedia://shop/{shop_id}/etalase/{etalase_id}";
        String SHOP_TALK = "tokopedia://shop/{shop_id}/talk";
        String SHOP_REVIEW = "tokopedia://shop/{shop_id}/review";
        String SHOP_NOTE = "tokopedia://shop/{shop_id}/note";
        String SHOP_INFO = "tokopedia://shop/{shop_id}/info";
        String PRODUCT_INFO = "tokopedia://product/{product_id}";
        String PRODUCT_ADD = "tokopedia://product/add";
        String CREDIT_CARD_AUTH_SETTING = "tokopedia://payment/credit-card";
        String CART = "tokopedia://cart";
        String SELLER_NEW_ORDER = "tokopedia://seller/new-order";
        String SELLER_SHIPMENT = "tokopedia://seller/shipment";
        String SELLER_STATUS = "tokopedia://seller/status";
        String SELLER_HISTORY = "tokopedia://seller/history";
        String CREATE_SHOP = "tokopedia://buka-toko-online-gratis";
        String REPUTATION = "tokopedia://review";
        String PRODUCT_REPUTATION = "tokopedia://product/{product_id}/review";
        String WEBVIEW = "tokopedia://webview";
        String WEBVIEW_PARENT_HOME = "tokopedia://webviewbackhome";
        String PRODUCT_TALK = "tokopedia://product/{product_id}/talk";
        String DIGITAL = "tokopedia://digital";
        String DIGITAL_PRODUCT = "tokopedia://digital/form";
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
        String PAYMENT_BACK_TO_DEFAULT = "tokopedia://payment/backtodefault";
        String WISHLIST = "tokopedia://wishlist";
        String RECENT_VIEW = "tokopedia://recentlyviewed";
        String TOPPICKS = "tokopedia://toppicks";
        String TOPPICK_DETAIL = "tokopedia://toppicks/{toppick_id}";
        String LOGIN = "tokopedia://login";
        String OFFICIAL_STORES = "tokopedia://official-stores";
        String RESCENTER = "tokopedia://resolution/{resolution_id}";
        String TOPCHAT = "tokopedia://topchat/{message_id}";
        String TOPCHAT_IDLESS = "tokopedia://topchat";
        String REFERRAL = "tokopedia://referral";
        String OFFICIAL_STORES_PROMO = "tokopedia://official-stores/promo/{slug}";
        String OFFICIAL_STORE_PROMO = "tokopedia://official-store/promo/{slug}";
        String OFFICIAL_STORES_PROMO_TERMS = "tokopedia://official-stores/promo-terms";
        String PROMO_SALE = "tokopedia://sale/{slug}/";
        String PROMO_SALE_TERMS = "tokopedia://promo-sale/promo-terms";
        String WALLET_HOME = "tokopedia://wallet";
        String WALLET_ACTIVATION = "tokopedia://wallet/activation";
        String WALLET_TRANSACTION_HISTORY = "tokopedia://wallet/transaction/history";
        String BROWSER = "tokopedia://browser";
        String FAVORITE = "tokopedia://home/favorite";

        String EVENTS = "tokopedia://events";
        String EVENTS_HIBURAN = "tokopedia://hiburan";
        String EVENTS_DETAILS = "tokopedia://events/{event}";
        String REFERRAL_WELCOME = "tokopedia://referral/{code}/{owner}";
        String KOLCOMMENT = "tokopedia://kolcomment/{id}";
        String PROMO_LIST = "tokopedia://promoNative";
        String EXPLORE = "tokopedia://jump/{section}";

        String SELLER_INFO_DETAIL = "tokopedia://sellerinfo/detail";

        interface SellerApp {
            String PRODUCT_ADD = "sellerapp://product/add";
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
        String NOTIFICATION_RINGTONE = "notifications_new_message_ringtone";
        String NOTIFICATION_VIBRATE = "notifications_new_message_vibrate";
        String NOTIFICATION_PROMO = "notification_receive_promo";
        String NOTIFICATION_PM = "notification_receive_pm";
        String NOTIFICATION_TALK = "notification_receive_talk";
        String NOTIFICATION_REVIEW = "notification_receive_review";
        String NOTIFICATION_REP = "notification_receive_reputation";
        String NOTIFICATION_SALES = "notification_sales";
        String NOTIFICATION_PURCHASE = "notification_purchase";
        String NOTIFICATION_RESCENTER = "notification_receive_rescenter";
        String NOTIFICATION_SELLER_INFO = "notification_seller_info";
    }

    interface AppLinkQueryParameter {
        String WALLET_TOP_UP_VISIBILITY = "top_up_visible";
    }
}
