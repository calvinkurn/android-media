package com.tokopedia.posapp.react;

/**
 * Created by okasurya on 8/29/17.
 */

public interface PosReactConst {
    String USER_ID = "user_id";

    interface Screen {
        String MAIN_POS_O2O = "pos";
        String PARAM_POS_PAGE = "POS_PAGE";
    }

    interface Page {
        String PRODUCT_LIST = "POS";
        String PAYMENT = "PAYMENT";
        String LOCAL_CART = "LOCAL_CART";
        String PAYMENT_PROCESSING = "PROCESSING";
        String TRANSACTION_HISTORY = "HISTORY";
        String INSTALLMENT = "CICILAN";
        String INVOICE = "INVOICE";
        String PAYMENT_ERROR = "PAYMENT_ERROR";
    }

    interface CacheTable {
        String PRODUCT = "PRODUCT";
        String CART = "KEY_CART";
        String BANK = "BANK";
        String ETALASE = "ETALASE";
        String GLOBAL = "GLOBAL";
    }
}
