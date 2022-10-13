package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConsInternalNavigation {
    const val PARAM_PAGE_SOURCE = "PAGE_SOURCE"

    const val SOURCE_HOME = "home"
    const val SOURCE_ACCOUNT = "account"
    const val SOURCE_HOME_UOH = "home_uoh"
    const val SOURCE_HOME_WISHLIST = "home_wishlist"
    const val SOURCE_HOME_WISHLIST_V2 = "home_wishlist_v2"
    const val SOURCE_HOME_WISHLIST_COLLECTION = "home_wishlist_collection"

    const val HOST_NAVIGATION = "navigation"

    const val INTERNAL_NAVIGATION = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_NAVIGATION"

    const val MAIN_NAVIGATION = "$INTERNAL_NAVIGATION/main"

}