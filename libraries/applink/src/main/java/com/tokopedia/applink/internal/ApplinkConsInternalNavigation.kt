package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConsInternalNavigation {
    const val PARAM_PAGE_SOURCE = "PAGE_SOURCE"

    const val SOURCE_HOME = "home"
    const val SOURCE_ACCOUNT = "account"

    @JvmField
    val HOST_NAVIGATION = "navigation"

    @JvmField
    val INTERNAL_NAVIGATION = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_NAVIGATION"

    @JvmField
    val MAIN_NAVIGATION = "$INTERNAL_NAVIGATION/main"

}