package com.tokopedia.talk.feature.reading.analytics

import com.tokopedia.talk.common.analytics.TalkTrackingConstants

object TalkReadingTrackingConstants {
    const val EVENT_ACTION_CLICK_SORT_OPTION = "${TalkTrackingConstants.EVENT_ACTION_CLICK} sort options on bottomsheet"
    const val EVENT_LABEL_CLICK_SORT_OPTION = "sort value:%s;"
    const val EVENT_ACTION_CLICK_FILTER_OPTION = "${TalkTrackingConstants.EVENT_ACTION_CLICK} quick filter options"
    const val EVENT_LABEL_CLICK_FILTER_OPTION = "filter value:%s;"
    const val EVENT_ACTION_GO_TO_REPLY = "${TalkTrackingConstants.EVENT_ACTION_CLICK} question to view thread"
    const val EVENT_LABEL_GO_TO_REPLY = "talk id:%s;talk category:%s;"
    const val EVENT_ACTION_LOAD_DATA = "${TalkTrackingConstants.EVENT_ACTION_CLICK} lazy load on talk detail"
    const val EVENT_LABEL_LOAD_DATA = "count page load:%s;count thread:%s;"
    const val EVENT_ACTION_CREATE_NEW_QUESTION = "click - create new question"
}