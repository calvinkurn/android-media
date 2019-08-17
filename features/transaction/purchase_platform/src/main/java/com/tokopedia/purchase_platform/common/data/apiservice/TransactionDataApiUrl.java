package com.tokopedia.purchase_platform.common.data.apiservice;

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

//        // Cart
//        public static final String PATH_SHOP_GROUP_LIST = BASE_PATH + VERSION_2 + "/shop_group";
//        public static final String PATH_REMOVE_FROM_CART = BASE_PATH + VERSION_2 + "/remove_product_cart";
//        public static final String PATH_UPDATE_CART = BASE_PATH + VERSION_2 + "/update_cart";
//
//        // Shipment
//        public static final String PATH_CART_LIST_MULTIPLE_ADDRESS = BASE_PATH + VERSION_2 + "/cart_list/multi_address";
//        public static final String PATH_SHIPPING_ADDRESS = BASE_PATH + VERSION_2 + "/shipping_address";
//        public static final String PATH_SHIPMENT_ADDRESS_FORM_DIRECT = BASE_PATH + VERSION_2 + "/shipment_address_form";
//        public static final String PATH_SHIPMENT_ADDRESS_ONE_CLICK_CHECKOUT = PATH_SHIPMENT_ADDRESS_FORM_DIRECT + "/one_click_shipment";
//        public static final String PATH_SAVE_SHIPMENT = BASE_PATH + VERSION_2 + "/save_shipment";
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
