package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalFeed {
    const val HOST_FEED_DETAILS = "feedcommunicationdetail"
    const val HOST_PLAY_LIVE_DETAILS = "feedplaylivedetail"

    const val INTERNAL_FEED_DETAILS = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_FEED_DETAILS}"
    const val INTERNAL_PLAY_LIVE_DETAILS = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_PLAY_LIVE_DETAILS}"
}