package com.tokopedia.payment.activity;

public class TopPayActivity {

    public static final int PAYMENT_SUCCESS = 5;
    public static final int PAYMENT_CANCELLED = 6;
    public static final int PAYMENT_FAILED = 7;
    public static final int REQUEST_CODE = 45675;

    public static <T> T createInstance(Object context, Object paymentPassData) {
        throw new RuntimeException("This no op module. Please use complete module :features:payment:payment");
    }
}
