package com.tokopedia.network.constant;

import com.tokopedia.url.TokopediaUrl;

/**
 * Created by Angga.Prasetiyo on 07/12/2015.
 */
public class TkpdBaseURL {

    public static String LIVE_DOMAIN = TokopediaUrl.Companion.getInstance().getWS();
    public static String BASE_DOMAIN = LIVE_DOMAIN;
    public static String WEB_DOMAIN = TokopediaUrl.Companion.getInstance().getWEB();
    public static String CHAT_REPORT_URL = TokopediaUrl.Companion.getInstance().getMOBILEWEB() + "chat/report/";
    public static String BASE_CONTACT_US = TokopediaUrl.Companion.getInstance().getWEB() + "contact-us";

    public static String TOKOPOINT_API_DOMAIN = TokopediaUrl.Companion.getInstance().getGW() + "tokopoints/api/";
    public static String PROMO_API_DOMAIN = TokopediaUrl.Companion.getInstance().getAPI() + "promo/";

    public static class Product {
        public static final String V4_ACTION_PRODUCT = "v4/action/product/";
        public static final String URL_PRODUCT_ACTION = BASE_DOMAIN + V4_ACTION_PRODUCT;

        public static final String PATH_EDIT_DESCRIPTION = "edit_description.pl";
        public static final String PATH_EDIT_WEIGHT_PRICE = "edit_weight_price.pl";
    }

    public static class Shop {
        public static final String URL_MY_SHOP_ADDRESS = BASE_DOMAIN + "v4/myshop-address/";
        public static final String URL_MY_SHOP_ORDER = BASE_DOMAIN + "v4/myshop-order/";
        public static final String URL_MY_SHOP_ORDER_ACTION = BASE_DOMAIN + "v4/action/myshop-order/";
        public static final String PATH_SHIPPING_WEBVIEW = "v4/web-view/";
        public static final String URL_SHIPPING_WEBVIEW = BASE_DOMAIN + PATH_SHIPPING_WEBVIEW;

        public static final String PATH_GET_LOCATION = "get_location.pl";

        public static final String PATH_EDIT_SHIPPING_REF = "edit_shipping_ref.pl";
        public static final String PATH_PROCEED_ORDER = "proceed_order.pl";
        public static final String PATH_PROCEED_SHIPPING = "proceed_shipping.pl";

        public static final String PATH_GET_SHIPPING_FORM = "get_edit_shipping_form.pl";
        public static final String PATH_GET_ORDER_LIST = "get_order_list.pl";
        public static final String PATH_GET_ORDER_NEW = "get_order_new.pl";
        public static final String PATH_GET_ORDER_PROCESS = "get_order_process.pl";
        public static final String PATH_GET_ORDER_STATUS = "get_order_status.pl";
        public static final String PATH_GET_PROCEED_SHIPPING_FORM = "get_proceed_shipping_form.pl";

        public static final String PATH_RETRY_PICKUP = "retry_pickup.pl";

        public static final String PATH_INSURANCE_TERMS_AND_CONDITIONS = "get_insurance_info.pl";
    }

    public static final class Etc {

        public static final String PATH_GET_CITY = "get_city.pl";
        public static final String PATH_GET_DISTRICT = "get_district.pl";
        public static final String PATH_GET_PROVINCE = "get_province.pl";
    }

    public static class Transaction {
        public static final String URL_TX_PAYMENT_VOUCHER = BASE_DOMAIN + "v4/tx-voucher/";

        public static final String PATH_CHECK_VOUCHER_CODE = "check_voucher_code.pl";

    }

    public static class Shipment {
        public static final String PATH_DISTRICT_RECOMMENDATION = "/v2/district-recommendation";
    }

    public static class Ace {
        public static final String PATH_SEARCH_PRODUCT = "search/product/v3";
    }

    public static class KunyitTalk {
        public static final String BASE_HOST_INBOX_TALK = "/talk/v2";

        public static final String GET_PRODUCT_TALK = BASE_HOST_INBOX_TALK + "/read";
        public static final String GET_COMMENT_TALK = BASE_HOST_INBOX_TALK + "/comment";

        public static final String ADD_COMMENT_TALK = BASE_HOST_INBOX_TALK + "/comment/create";

    }

    public static class Accounts {
        public static final String PATH_GET_TOKEN = "token";
    }

    public static class ContactUs {
        public static final String WEB_BASE = WEB_DOMAIN + "contact-us/";
        public static final String PATH_FEEDBACK = "ws/feedback";
    }

    public static class DigitalApi {
        public static final String VERSION = "v1.4/";
        public static final String HMAC_KEY = "web_service_v4";

        public static final String PATH_STATUS = "status";
        public static final String PATH_CATEGORY_LIST = "category/list";
        public static final String PATH_GET_CART = "cart";
        public static final String PATH_PATCH_OTP_SUCCESS = "cart/otp-success";
        public static final String PATH_CHECKOUT = "checkout";
        public static final String PATH_CHECK_VOUCHER = "voucher/check";
        public static final String PATH_CANCEL_VOUCHER = "voucher/cancel";
        public static final String PATH_USSD = "ussd/balance";
        public static final String PATH_SMARTCARD_INQUIRY = "smartcard/inquiry";
        public static final String PATH_SMARTCARD_COMMAND = "smartcard/command";
    }

    public static class DigitalWebsite {
        public static final String PATH_TRANSACTION_LIST = "order-list/";
        public static final String PATH_PRODUCT_LIST = "products/";
        public static final String PATH_SUBSCRIPTIONS = "subscribe/";
        public static final String PATH_FAVORITE_NUMBER = "favorite-list/";
        public static final String PATH_MY_BILLS = "mybills/";
    }

    public static class Chat {
        public static final String GET_MESSAGE = "/tc/v1/list_message";
        public static final String SEARCH = "/tc/v1/search";
        public static final String DELETE = "/tc/v1/delete";
        public static final String GET_TEMPLATE = "tc/v1/templates";
        public static final String UPDATE_TEMPLATE = "/tc/v1/templates/{index}";
        public static final String DELETE_TEMPLATE = "/tc/v1/templates/{index}";
        public static final String SET_TEMPLATE = "tc/v1/templates";
        public static final String CREATE_TEMPLATE = "tc/v1/templates";
    }

    public class Maps {
        public static final String PATH_MAPS_PLACES = "maps/places/autocomplete";
        public static final String PATH_MAPS_PLACES_DETAIL = "maps/places/place-details";
        public static final String PATH_MAPS_GEOCODE = "maps/geocode";
    }

    public class TokoPoint {
        public static final String VERSION = "v1/";
        public static final String HMAC_KEY = "web_service_v4";

        public static final String GET_COUPON_LIST = "coupon/list";


    }

    public class Purchase {
        public static final String PATH_ORDER_DETAIL = "/v4/order/detail";
        public static final String PATH_ORDER_HISTORY = "/v4/order/history";
        public static final String PATH_CHANGE_COURIER = "/v4/order/change_courier";
    }

    public class Promo {
        public static final String PATH_MENU_INDEX = "wp-json/wp/v2/hmenu";
        public static final String PATH_PROMO_LIST = "wp-json/wp/v2/posts";
    }
}
