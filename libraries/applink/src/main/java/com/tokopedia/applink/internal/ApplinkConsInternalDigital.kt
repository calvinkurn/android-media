package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConsInternalDigital {

    const val HOST_DIGITAL = "digital"

    const val INTERNAL_DIGITAL = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_DIGITAL}"

    const val CART_DIGITAL = "${INTERNAL_DIGITAL}/cart"
    const val TELCO_DIGITAL = "${INTERNAL_DIGITAL}/telco"
}