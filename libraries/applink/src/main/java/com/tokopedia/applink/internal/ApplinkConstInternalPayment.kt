package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia-android-internal://payment".
 */
object ApplinkConstInternalPayment {

    const val HOST_PAYMENT = "payment"
    val INTERNAL_PAYMENT = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_PAYMENT}"

    // TopPayActivity
    @JvmField
    val PAYMENT_CHECKOUT = "$INTERNAL_PAYMENT/checkout"

    @JvmField
    val PAYMENT_SETTING = "$INTERNAL_PAYMENT/setting"

    @JvmField
    val PAYMENT_ADD_CREDIT_CARD = "$INTERNAL_PAYMENT/add-credit-card"

}