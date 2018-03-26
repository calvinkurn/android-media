package com.tokopedia.posapp.common;

/**
 * @author okasurya on 3/26/18.
 */

public class PosUrl {
    public static String POS_DOMAIN = "https://gw.tokopedia.com/";

    public class Product {
        public static final String PRODUCT_LIST_V1 = "o2o/v1/shops/get_products";
        public static final String PRODUCT_LIST_V2 = "o2o/v2/shops/get_products";
        public static final String GET_ETALASE = "o2o/v1/shops/get_etalase";
    }

    public class Shop {
        public static final String OUTLET_LIST_V1 = "o2o/v1/outlet_list";
    }

    public class Payment {
        public static final String GET_PAYMENT_PARAM = "o2o/get_payment_params";
        public static final String GET_CREDIT_CARDS = "o2o/v1/payment/get_credit_cards";
        public static final String GET_INSTALLMENT_TERM = "o2o/v1/payment/get_installment_terms/{merchant_code}/{profile_code}";
        public static final String CREATE_PAYMENT = "o2o/v1/payment/create";
        public static final String PROCESS_CREDIT_CARD = "o2o/v1/payment/process/creditcard";
        public static final String GET_PAYMENT_STATUS = "o2o/v1/payment/status";
        public static final String CREATE_ORDER = "o2o/v1/payment_action";
    }
}
