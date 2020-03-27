package com.tokopedia.applink.feed

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalFeed.INTERNAL_FEED_DETAILS

object DeepLinkMapperFeed {
    fun getRegisteredFeed(deepLink: String): String {
        if (deepLink.startsWith(ApplinkConst.FEED_DETAILS)) {
            return deepLink.replace(ApplinkConst.FEED_DETAILS, INTERNAL_FEED_DETAILS)
        }
        return deepLink
    }
}