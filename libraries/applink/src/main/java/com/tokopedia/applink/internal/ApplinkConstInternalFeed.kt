package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalFeed {
    const val HOST_FEED_DETAILS = "feedcommunicationdetail"

    val INTERNAL_FEED_DETAILS = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_FEED_DETAILS}"

    const val FEED_PLUS_CONTAINER_FRAGMENT = "com.tokopedia.feedplus.view.fragment.FeedPlusContainerFragment"
}