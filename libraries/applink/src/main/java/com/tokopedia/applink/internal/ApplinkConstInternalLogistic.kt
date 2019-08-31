package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalLogistic {
    @JvmField
    val HOST_LOGISTIC = "logistic"
    @JvmField
    val INTERNAL_LOGISTIC = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_LOGISTIC"

    @JvmField
    val SHIPPING_CONFIRMATION = "${INTERNAL_LOGISTIC}/shipping-confirmation"
}