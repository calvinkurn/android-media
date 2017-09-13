package com.tokopedia.posapp.deeplink;

/**
 * Created by okasurya on 8/25/17.
 */

public interface Constants {
    interface Applinks {
        String PRODUCT_INFO = "tokopedia://product/{productId}";
        String CREDIT_CARD_INSTALLMENT = "posapp://installment";
        String PAYMENT_BANK = "posapp://payment/bank";
        String PAYMENT_SCAN_CC = "posapp://payment/scan/{bank_id}/{term}";
    }
}
