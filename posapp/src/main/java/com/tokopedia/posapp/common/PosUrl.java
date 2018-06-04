package com.tokopedia.posapp.common;

import com.tokopedia.posapp.product.common.ProductConstant;

/**
 * @author okasurya on 3/26/18.
 */

public class PosUrl {
    public static String POS_DOMAIN = "https://gw.tokopedia.com/";

    public static class Product {
        public static final String BASE_CUSTOMER_PRODUCT_LIST = "o2o/v1/outlet/";
        public static final String GET_CUSTOMER_PRODUCT_LIST = BASE_CUSTOMER_PRODUCT_LIST + "{"+ ProductConstant.Key.OUTLET_ID +"}/product";
        public static final String GET_ADMIN_PRODUCT_LIST = "o2o/v2/shops/get_products";
        public static final String GET_ETALASE = "o2o/v1/shops/get_etalase";
        public static final String EDIT_PRODUCT = "o2o/v1/price/update/{"+ ProductConstant.Key.OUTLET_ID +"}";
    }

    public class Shop {
        public static final String OUTLET_LIST_V1 = "o2o/v1/shops/outlet_list";
        public static final String GET_SHOP_INFO_V1 = "o2o/v1/shops/get_shop_info";
    }

    public class Payment {
        public static final String GET_PAYMENT_PARAM = "o2o/get_payment_params";
        public static final String GET_CREDIT_CARDS = "o2o/v1/payment/get_credit_cards";
        public static final String GET_INSTALLMENT_TERM = "o2o/v1/payment/get_installment_terms/{merchant_code}/{profile_code}";
        public static final String CREATE_PAYMENT = "o2o/v1/payment/create";
        public static final String PROCESS_CREDIT_CARD = "o2o/v1/payment/process/creditcard";
        public static final String GET_PAYMENT_STATUS = "o2o/v1/payment/status";
        public static final String CREATE_ORDER = "o2o/v1/payment_action";
        public static final String CHECK_TRANSACTION_STATUS = "/o2o/v1/payment/get_order_data";
    }

    public class ContentType {
        public static final String JSON = "Content-Type: application/json";
    }
}
