package com.tokopedia.posapp.react;

/**
 * Created by okasurya on 8/29/17.
 */

public interface PosReactConst {
    interface Screen {
        String MAIN_POS_O2O = "pos";
        String PARAM_POS_PAGE = "POS_PAGE";
    }

    interface Page {
        String PRODUCT_LIST = "POS";
        String PAYMENT = "PAYMENT";
        String LOCAL_CART = "LOCAL_CART";
        String PAYMENT_PROCESSING = "PROCESSING";
    }

    interface CacheTable {
        String PRODUCT = "PRODUCT";
        String CART = "CART";
        String BANK = "BANK";
    }

    interface EventEmitter {
        String LOCAL_CART_OPEN = "localCartOpen";
    }
}
