package com.tokopedia.applink.feed

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalFeed.FEED_UNIFIED_DETAILS
import com.tokopedia.applink.internal.ApplinkConstInternalFeed.INTERNAL_FEED_DETAILS
import com.tokopedia.applink.internal.ApplinkConstInternalFeed.INTERNAL_UNIFIED_FEED_DETAILS

object DeepLinkMapperFeed {
    fun getRegisteredFeed(deepLink: String): String {
        var returnedDeeplink = deepLink

        when {
            deepLink.startsWith(ApplinkConst.FEED_DETAILS) -> returnedDeeplink = deepLink.replace(
                ApplinkConst.FEED_DETAILS,
                INTERNAL_FEED_DETAILS
            )
            deepLink.startsWith(FEED_UNIFIED_DETAILS) -> returnedDeeplink = deepLink.replace(
                FEED_UNIFIED_DETAILS,
                INTERNAL_UNIFIED_FEED_DETAILS
            )
        }

        return returnedDeeplink
    }
}
