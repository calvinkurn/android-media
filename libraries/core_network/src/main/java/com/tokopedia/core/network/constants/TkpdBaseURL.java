package com.tokopedia.core.network.constants;

import com.tokopedia.url.TokopediaUrl;

/**
 * Created by Angga.Prasetiyo on 07/12/2015.
 */

@Deprecated
public class TkpdBaseURL {

    public static final String FLAG_APP = "?flag_app=1";
    public static String BASE_DOMAIN = TokopediaUrl.Companion.getInstance().getWS();
    public static String MERLIN_DOMAIN = TokopediaUrl.Companion.getInstance().getMERLIN();
    public static String GOOGLE_APIS = "https://www.googleapis.com";
    public static String TOPADS_DOMAIN = TokopediaUrl.Companion.getInstance().getTA();
    public static String ACCOUNTS_DOMAIN = TokopediaUrl.Companion.getInstance().getACCOUNTS();
    public static String INBOX_DOMAIN = TokopediaUrl.Companion.getInstance().getINBOX();
    public static String CHAT_DOMAIN = TokopediaUrl.Companion.getInstance().getCHAT();
    public static String JS_DOMAIN = TokopediaUrl.Companion.getInstance().getJS();
    public static String JS_STAGING_DOMAIN = "https://js-staging.tokopedia.com/";
    public static String JS_ALPHA_DOMAIN = "https://ajax-alpha.tokopedia.com/js/";
    public static String GOLD_MERCHANT_DOMAIN = TokopediaUrl.Companion.getInstance().getGOLDMERCHANT();
    public static String WEB_DOMAIN = TokopediaUrl.Companion.getInstance().getWEB();
    public static String MOBILE_DOMAIN = TokopediaUrl.Companion.getInstance().getMOBILEWEB();
    public static String BASE_CONTACT_US = WEB_DOMAIN + "contact-us";
    public static String TOKOPEDIA_CART_DOMAIN = TokopediaUrl.getInstance().getFS() + "tkpdcart/";
    public static String DIGITAL_API_DOMAIN = TokopediaUrl.Companion.getInstance().getPULSA_API();
    public static String DIGITAL_WEBSITE_DOMAIN = TokopediaUrl.Companion.getInstance().getPULSA();
    public static String SCROOGE_DOMAIN = TokopediaUrl.Companion.getInstance().getPAY();
    public static String HOME_DATA_BASE_URL = TokopediaUrl.Companion.getInstance().getGQL();
    public static String SCROOGE_CREDIT_CARD_DOMAIN = TokopediaUrl.Companion.getInstance().getPAY_ID();
    public static String MAPS_DOMAIN = TokopediaUrl.Companion.getInstance().getGW();

    public static class Product {
        public static final String V4_PRODUCT = "v4/product/";
        public static final String URL_PRODUCT = BASE_DOMAIN + V4_PRODUCT;
        public static final String V4_ACTION_PRODUCT = "v4/action/product/";
        public static final String URL_PRODUCT_ACTION = BASE_DOMAIN + V4_ACTION_PRODUCT;
        public static final String URL_REVIEW_ACTION = BASE_DOMAIN + "v4/action/review/";

        public static final String PATH_GET_DETAIL_PRODUCT = "get_detail.pl";
        public static final String PATH_GET_EDIT_PRODUCT_FORM = "get_edit_product_form.pl";

        public static final String PATH_DELETE_PRODUCT = "delete_product.pl";
        public static final String PATH_EDIT_CATEGORY = "edit_category.pl";
        public static final String PATH_EDIT_ETALASE = "edit_etalase.pl";
        public static final String PATH_EDIT_INSURANCE = "edit_insurance.pl";
        public static final String PATH_EDIT_PRICE = "edit_price.pl";
        public static final String PATH_EDIT_PRODUCT = "edit_product.pl";
        public static final String PATH_EDIT_DESCRIPTION = "edit_description.pl";
        public static final String PATH_EDIT_WEIGHT_PRICE = "edit_weight_price.pl";

        public static final String PATH_LIKE_DISLIKE_REVIEW = "like_dislike_review.pl";
        public static final String PATH_REPORT_REVIEW = "report_review.pl";
    }

    public static class User {
        public static final String URL_FAVE_SHOP_ACTION = BASE_DOMAIN + "v4/action/favorite-shop/";
        public static final String URL_INBOX_RES_CENTER = BASE_DOMAIN + "v4/inbox-resolution-center/";
        public static final String URL_INVOICE = BASE_DOMAIN + "v4/";
        public static final String PATH_NOTIFICATION = "v4/notification/";
        public static final String URL_NOTIFICATION = BASE_DOMAIN + PATH_NOTIFICATION;
        public static final String URL_PEOPLE_ACTION = BASE_DOMAIN + "v4/action/people/";
        public static final String URL_PEOPLE = BASE_DOMAIN + "v4/people/";
        public static final String URL_SESSION = BASE_DOMAIN + "v4/session/";

        public static final String URL_POST_FAVORITE_SHOP = "v4/action/favorite-shop/fav_shop.pl";

        public static final String PATH_FAVE_SHOP = "fav_shop.pl";
        public static final String PATH_GET_CREATE_RESOLUTION_FORM_NEW = "get_create_resolution_form_new.pl";
        public static final String PATH_GET_EDIT_RESOLUTION_FORM = "get_edit_resolution_form.pl";
        public static final String PATH_GET_RES_CENTER_PRODUCT_LIST = "get_product_list.pl";
        public static final String PATH_GET_SOLUTION = "get_form_solution.pl";
        public static final String PATH_GET_KURIR_LIST = "get_kurir_list.pl";
        public static final String PATH_GET_APPEAL_RESOLUTION_FORM = "get_appeal_resolution_form.pl";
        public static final String PATH_TRACK_SHIPPING_REF_V2 = "v4/inbox-resolution-center/track_shipping_ref.pl";

        public static final String PATH_RENDER_INVOICE = "invoice.pl";
        public static final String PATH_GET_NOTIFICATION = "get_notification.pl";

        public static final String PATH_DELETE_ADDRESS = "delete_address.pl";
        public static final String PATH_EDIT_ADDRESS = "edit_address.pl";
        public static final String PATH_EDIT_DEFAULT_ADDRESS = "edit_default_address.pl";
        public static final String PATH_EDIT_NOTIFICATION = "edit_notification.pl";

        public static final String PATH_GET_ADDRESS = "get_address.pl";
        public static final String PATH_GET_FAVORITE_SHOP = "get_favorit_shop.pl";
        public static final String PATH_GET_PEOPLE_INFO = "get_people_info.pl";

        public static final String PATH_LOGIN = "login.pl";
        public static final String PATH_LOGOUT = "logout.pl";
    }

    public static class Shop {
        public static final String PATH_MY_SHOP = "v4/myshop/";
        public static final String URL_MY_SHOP = BASE_DOMAIN + PATH_MY_SHOP;
        public static final String PATH_MY_SHOP_ETALASE = "v4/myshop-etalase/";
        public static final String PATH_ACTION_MY_SHOP_ETALASE = "v4/action/myshop-etalase/";
        public static final String PATH_MY_SHOP_INFO = "v4/action/myshop-info/";
        public static final String URL_MY_SHOP_ORDER = BASE_DOMAIN + "v4/myshop-order/";
        public static final String URL_MY_SHOP_ORDER_ACTION = BASE_DOMAIN + "v4/action/myshop-order/";
        public static final String URL_MY_SHOP_PAYMENT = BASE_DOMAIN + "v4/myshop-payment/";
        public static final String PATH_MY_SHOP_SHIPMENT = "v4/myshop-shipment/";
        public static final String URL_MY_SHOP_SHIPMENT = BASE_DOMAIN + PATH_MY_SHOP_SHIPMENT;
        public static final String PATH_MY_SHOP_SHIPMENT_ACTION = "v4/action/myshop-shipment/";
        public static final String URL_MY_SHOP_SHIPMENT_ACTION = BASE_DOMAIN + PATH_MY_SHOP_SHIPMENT_ACTION;
        public static final String URL_REPUTATION_ACTION = BASE_DOMAIN + "v4/action/reputation/";
        public static final String PATH_SHOP = "v4/shop/";
        public static final String URL_SHOP = BASE_DOMAIN + PATH_SHOP;
        public static final String PATH_SHIPPING_WEBVIEW = "v4/web-view/";
        public static final String URL_SHIPPING_WEBVIEW = BASE_DOMAIN + PATH_SHIPPING_WEBVIEW;
        public static final String URL_ACTION_SHOP_ORDER = "v4/myshop-order/";

        public static final String PATH_GET_OPEN_SHOP_FORM = "get_open_shop_form.pl";

        public static final String PATH_EVENT_SHOP_ADD_ETALASE = "event_shop_add_etalase.pl";
        public static final String PATH_GET_SHOP_ETALASE = "get_shop_etalase.pl";

        public static final String PATH_UPDATE_SHOP_CLOSE = "update_shop_close.pl";

        public static final String PATH_GET_SHOP_INFO = "get_shop_info.pl";

        public static final String PATH_EDIT_SHIPPING_REF = "edit_shipping_ref.pl";
        public static final String PATH_PROCEED_ORDER = "proceed_order.pl";
        public static final String PATH_PROCEED_SHIPPING = "proceed_shipping.pl";

        public static final String PATH_GET_SHIPPING_FORM = "get_edit_shipping_form.pl";
        public static final String PATH_GET_DETAIL_INFO_DETAIL = "get_shipping_detail_info.pl";
        public static final String PATH_GET_ORDER_LIST = "get_order_list.pl";
        public static final String PATH_GET_ORDER_NEW = "get_order_new.pl";
        public static final String PATH_GET_ORDER_PROCESS = "get_order_process.pl";
        public static final String PATH_GET_ORDER_STATUS = "get_order_status.pl";

        public static final String PATH_GET_PAYMENT_INFO = "get_payment_info.pl";

        public static final String PATH_UPDATE_SHIPPING_INFO = "update_shipping_info.pl";

        public static final String PATH_GET_SHIPPING_INFO = "get_shipping_info.pl";

        public static final String PATH_DELETE_REP_REVIEW_RESPONSE = "delete_reputation_review_response.pl";

        public static final String PATH_GET_LIKE_DISLIKE_REVIEW = "get_like_dislike_review_shop.pl";
        public static final String PATH_GET_SHOP_LOCATION = "get_shop_location.pl";
        public static final String PATH_GET_SHOP_PRODUCT = "get_shop_product.pl";

        public static final String PATH_RETRY_PICKUP = "retry_pickup.pl";
    }

    public static final class Etc {

        public static final String PATH_GET_FAVORITE_SHOP = "v4/home/get_favorite_shop.pl";
        public static final String PATH_GET_WISHLIST = "get_wishlist.pl";

        public static final String PATH_TERM_CONDITION = "terms.pl";
        public static final String PATH_PRIVACY_POLICY = "privacy.pl";

        public static final String PATH_GET_LIST_FAVE_SHOP_ID = "/v4/home/get_list_fave_shop_id.pl";
    }

    public static final class Tome {
        public static final String PATH_IS_FAVORITE_SHOP = "v1/user/isfollowing";
    }

    public static class ResCenter {
        public static final String URL_RES_CENTER_ACTION = BASE_DOMAIN + "v4/action/resolution-center/";

        public static final String PATH_REJECT_ADMIN_RES_SUBMIT = "reject_admin_resolution_submit.pl";
        public static final String PATH_REJECT_ADMIN_RES_VALIDATION = "reject_admin_resolution_validation_new.pl";
        public static final String PATH_REPLY_CONVERSATION_SUBMIT = "reply_conversation_submit.pl";
        public static final String PATH_REPLY_CONVERSATION_VALIDATION_NEW = "reply_conversation_validation_new.pl";
        public static final String PATH_CANCEL_RESOLUTION_V2 = "v4/action/resolution-center/cancel_resolution.pl";
        public static final String PATH_REPORT_RESOLUTION_V2 = "v4/action/resolution-center/report_resolution.pl";
        public static final String PATH_FINISH_RES_RETURN_V2 = "v4/action/resolution-center/finish_resolution_retur.pl";
        public static final String PATH_ACCEPT_ADMIN_RESOLUTION_V2 = "v4/action/resolution-center/accept_admin_resolution.pl";
        public static final String PATH_INPUT_ADDRESS_RESOLUTION_V2 = "v4/action/resolution-center/input_address_resolution.pl";
        public static final String PATH_EDIT_ADDRESS_RESOLUTION_V2 = "v4/action/resolution-center/edit_address_resolution.pl";
        public static final String PATH_REPLY_CONVERSATION_SUBMIT_V2 = "v4/action/resolution-center/reply_conversation_submit.pl";
        public static final String PATH_REPLY_CONVERSATION_VALIDATION_V2 = "v4/action/resolution-center/reply_conversation_validation.pl";
        public static final String PATH_GENERATE_TOKEN_HOST_WITHOUT_HEADER = "generate_token_host.pl";
        public static final String PATH_GENERATE_TOKEN_HOST = "v4/action/resolution-center/" + PATH_GENERATE_TOKEN_HOST_WITHOUT_HEADER;
    }

    public static class Search {
        public static final String URL_CATALOG_SELLER = "search/v1/catalog/product";

        public static final String URL_CATALOG = BASE_DOMAIN + "v4/catalog/";
        public static final String URL_HOT_LIST = BASE_DOMAIN + "v4/hotlist/";
        public static final String PATH_GET_CATALOG_DETAIL = "get_catalog_detail.pl";

        public static final String PATH_GET_HOTLIST = "get_hotlist.pl";
        public static final String PATH_GET_HOTLIST_BANNER = "v4/hotlist/get_hotlist_banner.pl";
    }

    public static class Transaction {
        public static final String URL_TRACKING_ORDER = BASE_DOMAIN + "v4/tracking-order/";
        public static final String URL_TX_ORDER_ACTION = BASE_DOMAIN + "v4/action/tx-order/";


        public static final String PATH_TRACK_ORDER = "track_order.pl";

        public static final String PATH_DELIVERY_CONFIRM = "delivery_confirm.pl";
        public static final String PATH_DELIVERY_FINISH_ORDER = "delivery_finish_order.pl";
        public static final String PATH_REQUEST_CANCEL_ORDER = "request_cancel_order.pl";
        public static final String PATH_REORDER = "reorder.pl";
    }

    public static class Upload {
        public static final String V4_ACTION_GENERATE_HOST = "v4/action/generate-host/";
        public static final String URL_GENERATE_HOST_ACTION = BASE_DOMAIN + V4_ACTION_GENERATE_HOST;

        public static final String PATH_GENERATE_HOST = "generate_host.pl";
        public static final String PATH_GENERATE_HOST_V2 = "v4/action/generate-host/generate_host.pl";

        public static final String PATH_UPLOAD_IMAGE_HELPER = "/web-service/v4/action/upload-image-helper/";
        public static final String PATH_ADD_PRODUCT_PICTURE = "add_product_picture.pl";
        public static final String PATH_OPEN_SHOP_PICTURE = "open_shop_picture.pl";
        public static final String PATH_CONTACT_IMAGE = "/web-service/v4/action/upload-image/upload_contact_image.pl";
        public static final String PATH_CREATE_RESOLUTION_PICTURE_FULL = "/web-service/v4/action/upload-image-helper/create_resolution_picture.pl";
        public static final String PATH_UPLOAD_VIDEO = "/upload/video";
        public static final String PATH_UPLOAD_ATTACHMENT = "/upload/attachment";
    }

    public static class Ace {
        public static final String PATH_SEARCH = "search/";
        public static final String URL_SEARCH = TokopediaUrl.Companion.getInstance().getACE() + PATH_SEARCH;

        public static final String PATH_CATALOG = "v1/catalog";
        public static final String PATH_HOTLIST_CATEGORY = "/hoth/hotlist/v1/category";
        public static final String PATH_SEARCH_PRODUCT = "search/product/v3";
        public static final String PATH_GET_ATTRIBUTE = "search/product/attributes/v3";
        public static final String PATH_GET_DYNAMIC_ATTRIBUTE = "v2/dynamic_attributes";
        public static final String PATH_GET_DYNAMIC_ATTRIBUTE_V4 = "v4/dynamic_attributes";
        public static final String PATH_BROWSE_CATALOG = "search/v2.1/catalog";
    }

    public static class Merlin {
        public static final String PATH_CATEGORY_RECOMMENDATION = "v4/product/category/recommendation";

    }

    public static class TopAds {
        public static final String PATH_GET_PROMO_TOP_ADS = "/promo/v1.1/display/products";
        public static final String PATH_GET_SHOP_TOP_ADS = "promo/v1.3/display/ads";
    }

    public static class Mojito {
        public static final String PATH_PRODUCT = "users/";

        public static final String PATH_WISHLIST = "wishlist/";
        public static final String PATH_WISH_LIST_V_1_1 = "v1.1";
        public static final String PATH_USER_RECENT_VIEW = "users/";
        public static final String PATH_RECENT_VIEW = "/recentview/products/v1";
        public static final String API_V1_BRANDS_CATEGORY = "/os/api/v1/brands/category/android/{categoryId}";
        public static final String PATH_USER_WISHLIST = "/users";
        public static final String PATH_CHECK_WISHLIST = "/v1/users/{userId}/wishlist/check/{listId}";
        public static final String PATH_OS_BANNER = "/os/api/search/banner/android";
    }

    public static class Accounts {
        public static final String PATH_GET_TOKEN = "token";
        public static final String PATH_DISCOVER_LOGIN = "api/discover";
        public static final String GENERATE_HOST = "/api/upload-host";
    }

    public static class Home {
        public static final String PATH_API_V1_ANNOUNCEMENT_TICKER = "/api/v1/tickers";
    }

    public static class GoldMerchant {
        public static final String GET_PRODUCT_VIDEO = "/v1/product/video/";
        public static final String GET_SHOP_SCORE_SUMMARY = "/v1/shopstats/shopscore/sum/";
        public static final String GET_SHOP_SCORE_DETAIL = "/v1/shopstats/shopscore/dtl/";
    }

    public static class FCM {
        public static final String UPDATE_FCM = "/api/gcm/update";
    }

    public static class ContactUs {
        public static final String PATH_GET_SOLUTION = "ajax/solution/{id}";
        public static final String PATH_CREATE_STEP_1 = "ajax/create/step/1";
        public static final String PATH_CREATE_STEP_2 = "ajax/create/step/2";
        public static final String PATH_COMMENT_RATING = "ws/contact-us/rating";
    }

    public static class DigitalApi {
        public static final String VERSION = "v1.4/";
        public static final String HMAC_KEY = "web_service_v4";
    }

    public static class HadesCategory {
        public static final String CHECK_VERSION = "/v1/categories_version";
        public static final String URL_HADES = TokopediaUrl.Companion.getInstance().getHADES();
        public static final String PATH_CATEGORIES = "/v2/categories/{catId}/detail";
        public static final String PATH_CATEGORIES_LAYOUT_ROOT = "/v1/category_layout/{catId}?type=root";
        public static final String PATH_CATEGORIES_LAYOUT = "/v1/category_layout/{catId}";
        public static final String FETCH_CATEGORIES = "/v1/categories?filter=type==tree";
    }

    public static class ResCenterV2 {
        public static final String BASE_RESOLUTION = TokopediaUrl.Companion.getInstance().getAPI() + "resolution/";
        public static final String BASE_RESOLUTION_VERSION_1 = "v1/";
        public static final String BASE_RESOLUTION_VERSION_2 = "v2/";
        public static final String BASE_INBOX_RESOLUTION_V2 = BASE_RESOLUTION_VERSION_2 + "inbox";
        public static final String BASE_DETAIL_RESOLUTION = BASE_RESOLUTION_VERSION_1 + "detail/{resolution_id}";
        public static final String BASE_DETAIL_RESOLUTION_V2 = BASE_RESOLUTION_VERSION_2 + "detail/{resolution_id}";

        public static final String GET_RESOLUTION_DETAIL = BASE_DETAIL_RESOLUTION;
        public static final String GET_RESOLUTION_DETAIL_V2 = BASE_DETAIL_RESOLUTION_V2;
        public static final String GET_RESOLUTION_CONVERSATION = BASE_DETAIL_RESOLUTION + "/conversation";
        public static final String GET_RESOLUTION_CONVERSATION_MORE = BASE_DETAIL_RESOLUTION + "/conversation/more";
        public static final String GET_RESOLUTION_HISTORY_AWB = BASE_DETAIL_RESOLUTION + "/history/awb";
        public static final String GET_RESOLUTION_HISTORY_ACTION = BASE_DETAIL_RESOLUTION + "/history/action";
        public static final String GET_RESOLUTION_HISTORY_ACTION_V2 = BASE_DETAIL_RESOLUTION_V2 + "/history/action";
        public static final String GET_RESOLUTION_HISTORY_ADDRESS = BASE_DETAIL_RESOLUTION + "/history/address";
        public static final String GET_RESOLUTION_LIST_PRODUCT = BASE_DETAIL_RESOLUTION + "/product";
        public static final String GET_RESOLUTION_PRODUCT_DETAIL = BASE_DETAIL_RESOLUTION + "/product/{trouble_id}";
        public static final String ACTION_REPLY_RESOLUTION = BASE_DETAIL_RESOLUTION + "/reply";
        public static final String ACTION_FINISH_RESOLUTION = BASE_DETAIL_RESOLUTION + "/finish";
        public static final String ACTION_CANCEL_RESOLUTION = BASE_DETAIL_RESOLUTION + "/cancel";
        public static final String ACTION_ASK_HELP_RESOLUTION = BASE_DETAIL_RESOLUTION + "/report_resolution";
        public static final String ACTION_ACCEPT_SOLUTION = BASE_DETAIL_RESOLUTION + "/accept";

        public static final String BASE_RESOLUTION_DETAIL_V1 = BASE_RESOLUTION_VERSION_1 + "detail/{resolution_id}";


        public static final String BASE_DETAIL_NEXT_ACTION_RESOLUTION_V2 = BASE_DETAIL_RESOLUTION_V2 + "/next_action";
        public static final String GET_RESOLUTION_CONVERSATION_V2 = BASE_DETAIL_RESOLUTION_V2 + "/conversation";
        public static final String GET_RESOLUTION_CONVERSATION_MORE_V2 = GET_RESOLUTION_CONVERSATION_V2 + "/more";
        public static final String POST_RESOLUTION_CONVERSATION_ADDRESS = BASE_RESOLUTION_DETAIL_V1 + "/address";
        public static final String POST_RESOLUTION_CONVERSATION_ADDRESS_EDIT = BASE_RESOLUTION_DETAIL_V1 + "/conversation/{conversation_id}/edit_address";


        public static final String GET_INBOX_RESOLUTION_V2_BUYER = BASE_INBOX_RESOLUTION_V2 + "/buyer";
        public static final String GET_INBOX_RESOLUTION_V2_SELLER = BASE_INBOX_RESOLUTION_V2 + "/seller";
        public static final String GET_INBOX_RESOLUTION_V2_SINGLE_ITEM = BASE_INBOX_RESOLUTION_V2 + "/{resolution_id}";

    }

    public static class Replacement {
        public static final String URL_REPLACEMENT = BASE_DOMAIN + "";

        public static final String PATH_CANCEL_REPLACEMENT = "v4/replacement/cancel";
    }

    public static class Payment {
        public static final String PATH_CC_DISPLAY = "v2/ccvault/metadata";
        public static final String PATH_CC_DELETE = "v2/ccvault/delete";

        public static final String PATH_ZEUS_CHECK_WHITELIST = "zeus/whitelist/status";
        public static final String PATH_ZEUS_UPDATE_WHITELIST = "zeus/whitelist";

    }

    public static class Chat {
        public static final String GET_MESSAGE = "/tc/v1/list_message";
        public static final String REPLY = "/tc/v1/reply";
        public static final String SEARCH = "/tc/v1/search";
        public static final String DELETE = "/tc/v1/delete";
        public static final String GET_TOPCHAT_NOTIFICATION = "tc/v1/notif_unreads";
        public static final String SET_TEMPLATE = "tc/v1/templates";
    }

    public static class Reputation {
        public static final String URL_REPUTATION = BASE_DOMAIN + "reputationapp/";
        public static final String PATH_SEND_REPUTATION_SMILEY = "reputation/api/v1/insert";
        public static final String REPUTATIONAPP_REVIEW_API = "reputationapp/review/api/";
        private static final String REPUTATION_VERSION = "v1";
        public static final String PATH_GET_INBOX_REPUTATION = "reputation/api/"
                + REPUTATION_VERSION + "/inbox";
        public static final String PATH_GET_DETAIL_INBOX_REPUTATION = "review/api/"
                + REPUTATION_VERSION + "/list";
        public static final String PATH_SEND_REVIEW_VALIDATE = "review/api/"
                + REPUTATION_VERSION + "/insert/validate";
        public static final String PATH_SEND_REVIEW_SUBMIT = "review/api/"
                + REPUTATION_VERSION + "/insert/submit";
        public static final String PATH_SKIP_REVIEW = "review/api/" + REPUTATION_VERSION + "/skip";
        public static final String PATH_EDIT_REVIEW_VALIDATE = "review/api/"
                + REPUTATION_VERSION + "/edit/validate";
        public static final String PATH_EDIT_REVIEW_SUBMIT = "review/api/"
                + REPUTATION_VERSION + "/edit/submit";
        public static final String PATH_REPORT_REVIEW = "review/api/"
                + REPUTATION_VERSION + "/report";
        public static final String PATH_DELETE_REVIEW_RESPONSE = "review/api/"
                + REPUTATION_VERSION + "/response/delete";
        public static final String PATH_INSERT_REVIEW_RESPONSE = "review/api/"
                + REPUTATION_VERSION + "/response/insert";
        public static final String PATH_GET_LIKE_DISLIKE_REVIEW = "review/api/"
                + REPUTATION_VERSION + "/likedislike";
        public static final String PATH_LIKE_DISLIKE_REVIEW = "review/api/"
                + REPUTATION_VERSION + "/likedislike";
        public static final String PATH_GET_REVIEW_PRODUCT_LIST = REPUTATIONAPP_REVIEW_API
                + REPUTATION_VERSION + "/product";
        public static final String PATH_GET_REVIEW_SHOP_LIST = REPUTATIONAPP_REVIEW_API
                + REPUTATION_VERSION + "/shop";
        public static final String PATH_GET_REVIEW_HELPFUL_LIST = REPUTATIONAPP_REVIEW_API
                + REPUTATION_VERSION + "/mosthelpful";
        public static final String PATH_GET_REVIEW_PRODUCT_RATING = REPUTATIONAPP_REVIEW_API
                + REPUTATION_VERSION + "/rating";
    }

    public class Maps {
        public static final String PATH_MAPS_PLACES = "maps/places/autocomplete";
        public static final String PATH_MAPS_PLACES_DETAIL = "maps/places/place-details";
        public static final String PATH_MAPS_GEOCODE = "maps/geocode";
    }

    public class Purchase {
        public static final String PATH_ORDER_DETAIL = "/v4/order/detail";
        public static final String PATH_ORDER_HISTORY = "/v4/order/history";
        public static final String PATH_CHANGE_COURIER = "/v4/order/change_courier";
    }
}
