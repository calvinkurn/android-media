package com.tokopedia.core.network.constants;

import com.tokopedia.url.TokopediaUrl;

/**
 * Created by Angga.Prasetiyo on 07/12/2015.
 */

@Deprecated
public class TkpdBaseURL {

    public static String BASE_DOMAIN = TokopediaUrl.Companion.getInstance().getWS();
    public static String MERLIN_DOMAIN = TokopediaUrl.Companion.getInstance().getMERLIN();
    public static String GOOGLE_APIS = "https://www.googleapis.com";
    public static String TOPADS_DOMAIN = TokopediaUrl.Companion.getInstance().getTA();
    public static String ACCOUNTS_DOMAIN = TokopediaUrl.Companion.getInstance().getACCOUNTS();
    public static String INBOX_DOMAIN = TokopediaUrl.Companion.getInstance().getINBOX();
    public static String JS_DOMAIN = TokopediaUrl.Companion.getInstance().getJS();
    public static String JS_STAGING_DOMAIN = "https://js-staging.tokopedia.com/";
    public static String JS_ALPHA_DOMAIN = "https://ajax-alpha.tokopedia.com/js/";
    public static String GOLD_MERCHANT_DOMAIN = TokopediaUrl.Companion.getInstance().getGOLDMERCHANT();
    public static String WEB_DOMAIN = TokopediaUrl.Companion.getInstance().getWEB();
    public static String BASE_CONTACT_US = WEB_DOMAIN + "contact-us";
    public static String TOKOPEDIA_CART_DOMAIN = TokopediaUrl.getInstance().getFS() + "tkpdcart/";
    public static String DIGITAL_API_DOMAIN = TokopediaUrl.Companion.getInstance().getPULSA_API();
    public static String SCROOGE_DOMAIN = TokopediaUrl.Companion.getInstance().getPAY();

    public static class Product {
        public static final String V4_PRODUCT = "v4/product/";

        public static final String PATH_GET_DETAIL_PRODUCT = "get_detail.pl";
        public static final String PATH_GET_EDIT_PRODUCT_FORM = "get_edit_product_form.pl";
    }

    public static class User {
        public static final String URL_INBOX_RES_CENTER = BASE_DOMAIN + "v4/inbox-resolution-center/";
        public static final String PATH_NOTIFICATION = "v4/notification/";
        public static final String URL_NOTIFICATION = BASE_DOMAIN + PATH_NOTIFICATION;
        public static final String URL_PEOPLE_ACTION = BASE_DOMAIN + "v4/action/people/";
        public static final String URL_PEOPLE = BASE_DOMAIN + "v4/people/";

        public static final String PATH_GET_CREATE_RESOLUTION_FORM_NEW = "get_create_resolution_form_new.pl";
        public static final String PATH_GET_EDIT_RESOLUTION_FORM = "get_edit_resolution_form.pl";
        public static final String PATH_GET_RES_CENTER_PRODUCT_LIST = "get_product_list.pl";
        public static final String PATH_GET_SOLUTION = "get_form_solution.pl";
        public static final String PATH_GET_KURIR_LIST = "get_kurir_list.pl";
        public static final String PATH_GET_APPEAL_RESOLUTION_FORM = "get_appeal_resolution_form.pl";
        public static final String PATH_TRACK_SHIPPING_REF_V2 = "v4/inbox-resolution-center/track_shipping_ref.pl";

        public static final String PATH_GET_NOTIFICATION = "get_notification.pl";

        public static final String PATH_EDIT_NOTIFICATION = "edit_notification.pl";

        public static final String PATH_GET_PEOPLE_INFO = "get_people_info.pl";
    }

    public static class Shop {
        public static final String PATH_MY_SHOP = "v4/myshop/";
        public static final String URL_MY_SHOP = BASE_DOMAIN + PATH_MY_SHOP;
        public static final String PATH_MY_SHOP_ETALASE = "v4/myshop-etalase/";
        public static final String PATH_ACTION_MY_SHOP_ETALASE = "v4/action/myshop-etalase/";
        public static final String URL_MY_SHOP_PAYMENT = BASE_DOMAIN + "v4/myshop-payment/";
        public static final String PATH_MY_SHOP_SHIPMENT = "v4/myshop-shipment/";
        public static final String URL_MY_SHOP_SHIPMENT = BASE_DOMAIN + PATH_MY_SHOP_SHIPMENT;
        public static final String PATH_MY_SHOP_SHIPMENT_ACTION = "v4/action/myshop-shipment/";
        public static final String URL_MY_SHOP_SHIPMENT_ACTION = BASE_DOMAIN + PATH_MY_SHOP_SHIPMENT_ACTION;
        public static final String PATH_SHOP = "v4/shop/";
        public static final String URL_SHOP = BASE_DOMAIN + PATH_SHOP;
        public static final String PATH_SHIPPING_WEBVIEW = "v4/web-view/";
        public static final String URL_SHIPPING_WEBVIEW = BASE_DOMAIN + PATH_SHIPPING_WEBVIEW;
        public static final String URL_ACTION_SHOP_ORDER = "v4/myshop-order/";

        public static final String PATH_GET_OPEN_SHOP_FORM = "get_open_shop_form.pl";

        public static final String PATH_EVENT_SHOP_ADD_ETALASE = "event_shop_add_etalase.pl";
        public static final String PATH_GET_SHOP_ETALASE = "get_shop_etalase.pl";

        public static final String PATH_GET_SHOP_INFO = "get_shop_info.pl";
        public static final String PATH_GET_DETAIL_INFO_DETAIL = "get_shipping_detail_info.pl";
        public static final String PATH_GET_ORDER_NEW = "get_order_new.pl";

        public static final String PATH_GET_PAYMENT_INFO = "get_payment_info.pl";

        public static final String PATH_UPDATE_SHIPPING_INFO = "update_shipping_info.pl";

        public static final String PATH_GET_SHIPPING_INFO = "get_shipping_info.pl";
        public static final String PATH_GET_SHOP_LOCATION = "get_shop_location.pl";
        public static final String PATH_GET_SHOP_PRODUCT = "get_shop_product.pl";

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
        public static final String URL_HOT_LIST = BASE_DOMAIN + "v4/hotlist/";

        public static final String PATH_GET_HOTLIST = "get_hotlist.pl";
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
        public static final String PATH_CATALOG = "v1/catalog";
    }

    public static class Merlin {
        public static final String PATH_CATEGORY_RECOMMENDATION = "v4/product/category/recommendation";

    }

    public static class Accounts {
        public static final String PATH_GET_TOKEN = "token";
        public static final String PATH_DISCOVER_LOGIN = "api/discover";
        public static final String GENERATE_HOST = "/api/upload-host";
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

    public static class Payment {
        public static final String PATH_ZEUS_UPDATE_WHITELIST = "zeus/whitelist";
    }

    public static class Chat {
        public static final String SEARCH = "/tc/v1/search";
        public static final String DELETE = "/tc/v1/delete";
    }

    public class Purchase {
        public static final String PATH_ORDER_DETAIL = "/v4/order/detail";
        public static final String PATH_ORDER_HISTORY = "/v4/order/history";
        public static final String PATH_CHANGE_COURIER = "/v4/order/change_courier";
    }
}