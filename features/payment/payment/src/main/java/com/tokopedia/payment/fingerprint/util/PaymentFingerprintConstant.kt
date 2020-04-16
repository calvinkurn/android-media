package com.tokopedia.payment.fingerprint.util

import com.tokopedia.url.TokopediaUrl


/**
 * Created by zulfikarrahman on 3/27/18.
 */
object PaymentFingerprintConstant {
    const val ENABLE_FINGERPRINT = "enable_fingerprint"
    const val TRANSACTION_ID = "transaction_id"
    const val OTP_FINGERPRINT_ADD = "otp/fingerprint/add"
    const val V2_FINGERPRINT_PUBLICKEY_SAVE = "v2/fingerprint/publickey/save"
    const val V2_PAYMENT_CC_FINGERPRINT = "v2/payment/cc/fingerprint"
    var ACCOUNTS_DOMAIN = TokopediaUrl.getInstance().ACCOUNTS
    var TOP_PAY_DOMAIN = TokopediaUrl.getInstance().PAY_ID
    const val APP_LINK_FINGERPRINT = "tokopedia://fingerprint/save"
    const val TOP_PAY_PATH_CREDIT_CARD_VERITRANS = "v2/3dsecure/cc/veritrans"
    const val TOP_PAY_PATH_CREDIT_CARD_SPRINTASIA = "v2/3dsecure/sprintasia"
    const val V2_PAYMENT_GET_POST_DATA = "v2/payment/ws/param/3ds"
}