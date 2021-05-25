package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalTokoMart {

    @JvmField
    val HOST_TOKOMART = "tokomart"

    @JvmField
    val INTERNAL_TOKOMART = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_TOKOMART}"

    //TokoMartHomeActivity
    @JvmField
    val HOME = "$INTERNAL_TOKOMART/home"
}