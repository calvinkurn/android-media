package com.tokopedia.talk.feature.reading.analytics

import com.tokopedia.talk.common.analytics.TalkTrackingConstants

object TalkReadingTrackingConstants {
    const val EVENT_ACTION_CLICK_SORT_OPTION = "${TalkTrackingConstants.EVENT_ACTION_CLICK} sort options on bottomsheet"
    const val EVENT_LABEL_CLICK_SORT_OPTION = "sort value:%s;"
    const val EVENT_ACTION_CLICK_FILTER_OPTION = "${TalkTrackingConstants.EVENT_ACTION_CLICK} quick filter options"
    const val EVENT_LABEL_CLICK_FILTER_OPTION = "filter value:%s;"
}