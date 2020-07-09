package com.tokopedia.talk.feature.reading.analytics

import com.tokopedia.talk.common.analytics.TalkEventTracking
import com.tokopedia.talk.common.analytics.TalkTrackingConstants
import com.tokopedia.track.TrackApp

object TalkReadingTracking {

    private fun eventTalkReading(action: String, label: String, userId: String, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TalkEventTracking(
                TalkTrackingConstants.EVENT_CATEGORY_TALK, action, label, userId, productId
        ).dataTracking)
    }

    fun eventClickSort(sortOption: String, userId: String, productId: String) {
        with(TalkReadingTrackingConstants) {
            eventTalkReading(EVENT_ACTION_CLICK_SORT_OPTION, String.format(EVENT_LABEL_CLICK_SORT_OPTION, sortOption), userId, productId)
        }
    }

    fun eventClickFilter(filterOption: String, userId: String, productId: String) {
        with(TalkReadingTrackingConstants) {
            eventTalkReading(EVENT_ACTION_CLICK_FILTER_OPTION, String.format(EVENT_LABEL_CLICK_FILTER_OPTION, filterOption), userId, productId)
        }
    }

    fun sendScreen(screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }
}