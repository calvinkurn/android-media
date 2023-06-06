package com.tokopedia.applink.feed

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalFeed.FEED_UNIFIED_DETAILS
import com.tokopedia.applink.internal.ApplinkConstInternalFeed.INTERNAL_FEED_DETAILS
import com.tokopedia.applink.internal.ApplinkConstInternalFeed.INTERNAL_UNIFIED_FEED_DETAILS

object DeepLinkMapperFeed {
    fun getRegisteredFeed(deepLink: String): String =
        when {
            deepLink.startsWith(ApplinkConst.FEED_DETAILS) -> deepLink.replace(
                ApplinkConst.FEED_DETAILS,
                INTERNAL_FEED_DETAILS
            )
            deepLink.startsWith(FEED_UNIFIED_DETAILS) -> deepLink.replace(
                FEED_UNIFIED_DETAILS,
                INTERNAL_UNIFIED_FEED_DETAILS
            )
            else -> deepLink
        }
}
