package com.tokopedia.payment.fingerprint.util;

import com.tokopedia.url.TokopediaUrl;

/**
 * Created by zulfikarrahman on 3/27/18.
 */

public class PaymentFingerprintConstant {
    public static final String PARTNER = "partner";
    public static final String ENABLE_FINGERPRINT = "enable_fingerprint";
    public static final String TRANSACTION_ID = "transaction_id";
    public static final String OTP_FINGERPRINT_ADD = "otp/fingerprint/add";
    public static final String V2_FINGERPRINT_PUBLICKEY_SAVE = "v2/fingerprint/publickey/save";
    public static final String V2_PAYMENT_CC_FINGERPRINT = "v2/payment/cc/fingerprint";
    public static String ACCOUNTS_DOMAIN = TokopediaUrl.Companion.getInstance().getACCOUNTS();
    public static String TOP_PAY_DOMAIN = TokopediaUrl.Companion.getInstance().getPAY_ID();
    public static final String APP_LINK_FINGERPRINT = "tokopedia://fingerprint/save";
    public static final String TOP_PAY_PATH_CREDIT_CARD_VERITRANS = "v2/3dsecure/cc/veritrans";
    public static final String TOP_PAY_PATH_CREDIT_CARD_SPRINTASIA = "v2/3dsecure/sprintasia";
    public static final String V2_PAYMENT_GET_POST_DATA = "v2/payment/ws/param/3ds";
}
