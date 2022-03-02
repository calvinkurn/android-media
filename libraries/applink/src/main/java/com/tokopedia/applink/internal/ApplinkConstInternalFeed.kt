package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalFeed {
    const val HOST_FEED_DETAILS = "feedcommunicationdetail"
    const val HOST_PLAY_LIVE_DETAILS = "feedplaylivedetail"

    const val INTERNAL_FEED_DETAILS = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_FEED_DETAILS}"
    const val INTERNAL_PLAY_LIVE_DETAILS = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_PLAY_LIVE_DETAILS}"
    const val PLAY_LIVE_PARAM_WIDGET_TYPE = "widgetType"
    const val PLAY_UPCOMING_SOURCE_TYPE = "source_type"
    const val PLAY_UPCOMING_SOURCE_ID = "source_id"
    const val PLAY_UPCOMING_FILTER_CATEGORY = "filter_category"
}