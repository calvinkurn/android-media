package com.tokopedia.checkout.data;

/**
 * @author anggaprasetiyo on 08/05/18.
 */
public class ConstantApiUrl {

    public static class Cart {
        static public String BASE_URL = "https://api.tokopedia.com/";

        public static final String HMAC_KEY = "web_service_v4";
        public static final String VERSION = "v1";
        static final String BASE_PATH = "cart/";

        public static final String PATH_ADD_TO_CART = BASE_PATH + VERSION + "/add_product_cart";
        public static final String PATH_CART_LIST = BASE_PATH + VERSION + "/cart_list";
        public static final String PATH_REMOVE_FROM_CART = BASE_PATH + VERSION + "/remove_product_cart";
        public static final String PATH_UPDATE_CART = BASE_PATH + VERSION + "/update_cart";
        public static final String PATH_CHECK_PROMO_CODE_CART_LIST = BASE_PATH + VERSION + "/check_promo_code";
        public static final String PATH_SHIPPING_ADDRESS = BASE_PATH + VERSION + "/shipping_address";
        public static final String PATH_SHIPMENT_ADDRESS_FORM_DIRECT = BASE_PATH + VERSION + "/shipment_address_form";
        public static final String PATH_CHECK_PROMO_CODE_CART_COURIER = BASE_PATH + VERSION + "/check_promo_code_final";
        public static final String PATH_CHECKOUT = BASE_PATH + VERSION + "/checkout";
        public static final String PATH_RESET_CART = BASE_PATH + VERSION + "/reset_cart_cache";
        public static final String PATH_UPDATE_STATE_BY_PAYMENT = BASE_PATH + VERSION + "/update_state_by_payment";
        public static final String PATH_NOTIFICATION_COUNTER = BASE_PATH + VERSION + "/counter";
        public static final String PATH_COUPON_LIST = BASE_PATH + VERSION + "/coupon_list";
        public static final String PATH_SAVE_PICKUP_STORE_POINT = BASE_PATH + VERSION + "/save_pickup_store_point";
    }

    public static class TransactionAction {
        static public String BASE_URL = "https://ws.tokopedia.com/";

        public static final String HMAC_KEY = "web_service_v4";
        public static final String VERSION = "v4/";
        static final String BASE_PATH = "action/tx/";

        public static final String PATH_GET_PARAMETER_DYNAMIC_PAYMENT = VERSION + BASE_PATH + "toppay_get_parameter.pl";
        public static final String PATH_THANKS_DYNAMIC_PAYMENT = VERSION + BASE_PATH + "toppay_thanks_action.pl";
    }
}
