package com.tokopedia.transactiondata.constant;

import com.tokopedia.url.TokopediaUrl;

/**
 * @author anggaprasetiyo on 09/05/18.
 */
public class TransactionDataApiUrl {
    public static class Cart {
        static public String BASE_URL = TokopediaUrl.Companion.getInstance().getAPI();

        public static final String HMAC_KEY = "web_service_v4";
        public static final String VERSION_1 = "v1";
        public static final String VERSION_2 = "v2";
        static final String BASE_PATH = "cart/";

        public static final String PATH_ADD_TO_CART = BASE_PATH + VERSION_2 + "/add_product_cart";
        public static final String PATH_ADD_TO_CART_ONE_CLICK_SHIPMENT = PATH_ADD_TO_CART + "/one_click_shipment";
        public static final String PATH_CART_LIST = BASE_PATH + VERSION_2 + "/cart_list";
        public static final String PATH_CART_LIST_MULTIPLE_ADDRESS = BASE_PATH + VERSION_2 + "/cart_list/multi_address";
        public static final String PATH_SHOP_GROUP_LIST = BASE_PATH + VERSION_2 + "/shop_group";
        public static final String PATH_REMOVE_FROM_CART = BASE_PATH + VERSION_2 + "/remove_product_cart";
        public static final String PATH_UPDATE_CART = BASE_PATH + VERSION_2 + "/update_cart";
        public static final String PATH_CHECK_PROMO_CODE_CART_LIST = BASE_PATH + VERSION_2 + "/check_promo_code";
        public static final String PATH_SHIPPING_ADDRESS = BASE_PATH + VERSION_2 + "/shipping_address";
        public static final String PATH_SHIPMENT_ADDRESS_FORM_DIRECT = BASE_PATH + VERSION_2 + "/shipment_address_form";
        public static final String PATH_SHIPMENT_ADDRESS_ONE_CLICK_CHECKOUT = PATH_SHIPMENT_ADDRESS_FORM_DIRECT + "/one_click_shipment";
        public static final String PATH_CHECK_PROMO_CODE_CART_COURIER = BASE_PATH + VERSION_2 + "/check_promo_code_final";
        public static final String PATH_CHECKOUT = BASE_PATH + VERSION_2 + "/checkout";
        public static final String PATH_RESET_CART = BASE_PATH + VERSION_2 + "/reset_cart_cache";
        public static final String PATH_UPDATE_STATE_BY_PAYMENT = BASE_PATH + VERSION_2 + "/update_state_by_payment";
        public static final String PATH_NOTIFICATION_COUNTER = BASE_PATH + VERSION_1 + "/counter";
        public static final String PATH_COUPON_LIST = BASE_PATH + VERSION_2 + "/coupon_list";
        public static final String PATH_SAVE_PICKUP_STORE_POINT = BASE_PATH + VERSION_2 + "/save_pickup_store_point";
        public static final String PATH_CANCEL_AUTO_APPLY_COUPON = BASE_PATH + VERSION_2 + "/auto_applied_kupon/clear";
        public static final String PATH_SAVE_SHIPMENT = BASE_PATH + VERSION_2 + "/save_shipment";
    }

    public static class TransactionAction {
        static public String BASE_URL = TokopediaUrl.Companion.getInstance().getWS();

        public static final String HMAC_KEY = "web_service_v4";
        public static final String VERSION = "v4/";
        static final String BASE_PATH = "action/tx/";

        public static final String PATH_GET_PARAMETER_DYNAMIC_PAYMENT = VERSION + BASE_PATH + "toppay_get_parameter.pl";
        public static final String PATH_THANKS_DYNAMIC_PAYMENT = VERSION + BASE_PATH + "toppay_thanks_action.pl";
    }
}
