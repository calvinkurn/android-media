package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalFeed {
    const val HOST_FEED_DETAILS = "feedcommunicationdetail"

    const val INTERNAL_FEED_DETAILS = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_FEED_DETAILS}"
}