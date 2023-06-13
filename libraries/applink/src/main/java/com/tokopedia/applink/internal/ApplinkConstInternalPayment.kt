package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia-android-internal://payment".
 */
object ApplinkConstInternalPayment {

    const val HOST_PAYMENT = "payment"
    const val INTERNAL_PAYMENT = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_PAYMENT"

    // TopPayActivity
    @JvmField
    val PAYMENT_CHECKOUT = "$INTERNAL_PAYMENT/checkout"
    val CHECKOUT_TIMESTAMP = "checkout_timestamp"

    @JvmField
    val PAYMENT_SETTING = "$INTERNAL_PAYMENT/setting"

    @JvmField
    val PAYMENT_ADD_CREDIT_CARD = "$INTERNAL_PAYMENT/add-credit-card"

    @JvmField
    val PAYMENT_THANK_YOU_PAGE = "$INTERNAL_PAYMENT/thankyou"

    // PMS

    const val INTERNAL_PMS = "${DeeplinkConstant.SCHEME_INTERNAL}://buyer"

    val PMS_PAYMENT_LIST = "$INTERNAL_PMS/payment"
    val GOPAY_KYC = "$INTERNAL_PAYMENT/gopayKyc"

    // how to pay

    const val INTERNAL_HOW_TO_PAY = "${DeeplinkConstant.SCHEME_INTERNAL}://howtopay"
}
