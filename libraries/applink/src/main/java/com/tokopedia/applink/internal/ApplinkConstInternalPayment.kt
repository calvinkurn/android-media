package com.tokopedia.applink.internal

import com.tokopedia.applink.internal.ApplinkConstInternal.INTERNAL_SCHEME

/**
 * This class is used to store deeplink "tokopedia-android-internal://payment".
 */
object ApplinkConstInternalPayment {

    const val HOST_PAYMENT = "payment"
    val INTERNAL_PAYMENT = "${INTERNAL_SCHEME}://${HOST_PAYMENT}"

    // TopPayActivity
    @JvmField
    val PAYMENT_CHECKOUT = "$INTERNAL_PAYMENT/checkout"

}