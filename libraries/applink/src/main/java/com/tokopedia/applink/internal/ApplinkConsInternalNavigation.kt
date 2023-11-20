package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConsInternalNavigation {
    const val PARAM_PAGE_SOURCE = "PAGE_SOURCE"
    const val PARAM_PAGE_SOURCE_PATH = "PAGE_SOURCE_PATH"

    const val HOST_NAVIGATION = "navigation"

    const val INTERNAL_NAVIGATION = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_NAVIGATION"

    const val MAIN_NAVIGATION = "$INTERNAL_NAVIGATION/main"
}
