package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalTokoMart {

    @JvmField
    val HOST_TOKOMART = "tokomart"

    @JvmField
    val INTERNAL_TOKOMART = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_TOKOMART}"

    //TokoMartHomeActivity
    @JvmField
    val TOKOMART_HOME = "$INTERNAL_TOKOMART/home"

    @JvmField
    val TOKOMART_SEARCH = "$INTERNAL_TOKOMART/search"

    @JvmField
    val TOKOMART_CATEGORY = "$INTERNAL_TOKOMART/category/{category_id}"
}