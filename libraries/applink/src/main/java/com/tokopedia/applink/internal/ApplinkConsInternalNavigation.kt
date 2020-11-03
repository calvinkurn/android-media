package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConsInternalNavigation {
    private const val HOST_NAVIGATION = "navigation"
    const val INTERNAL_NAVIGATION = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_NAVIGATION"

    const val MAIN_NAVIGATION = "$INTERNAL_NAVIGATION/main"

}