package com.tokopedia.posapp.deeplink;

/**
 * Created by okasurya on 8/25/17.
 */

public interface Constants {
    interface Applinks {
        String PRODUCT_INFO = "posapp://product/{productId}";
        String PRODUCT_LIST = "posapp://product";
        String CREDIT_CARD_INSTALLMENT = "posapp://installment";
        String PAYMENT_CHECKOUT = "posapp://payment/checkout";
        String PAYMENT_SCAN_CC = "posapp://payment/scan";
        String PAYMENT_PROCESSING = "posapp://payment/process";
        String PAYMENT_OTP = "posapp://payment/otp";
        String PAYMENT_INVOICE = "posapp://payment/invoice";
        String CART = "posapp://cart";
    }
}
