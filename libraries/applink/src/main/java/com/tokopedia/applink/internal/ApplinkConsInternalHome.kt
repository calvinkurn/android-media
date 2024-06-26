package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConsInternalHome {
    private const val HOST_HOME = "home"
    const val INTERNAL_HOME = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_HOME"

    const val DEFAULT_HOME_RECOMMENDATION = "$INTERNAL_HOME/rekomendasi"

    const val AUTHORITY_DISCOVERY = "${ApplinkConstInternalDiscovery.HOST_DISCOVERY}"

    const val PATH_REKOMENDASI = "rekomendasi"

    const val HOME_SIMILAR_PRODUCT = "$INTERNAL_HOME/rekomendasi/d/.*"

    const val HOME_RECOMMENDATION = "$INTERNAL_HOME/rekomendasi/.*\\/"

    const val EXPLORE = "$INTERNAL_HOME/jump"

    const val HOME_WISHLIST = "$INTERNAL_HOME/wishlist"

    const val HOME_INBOX = "$INTERNAL_HOME/inbox"

    const val HOME_NAVIGATION = "$INTERNAL_HOME/navigation"

    const val HOME_NAVIGATION_OLD = "$INTERNAL_HOME/navigation-old"
}
