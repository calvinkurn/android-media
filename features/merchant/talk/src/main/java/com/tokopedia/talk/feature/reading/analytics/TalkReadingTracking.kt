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
            eventTalkReading(EVENT_ACTION_CLICK_FILTER_OPTION, String.format(EVENT_LABEL_CLICK_FILTER_OPTION, filterOption.substringBefore(" (")), userId, productId)
        }
    }

    fun sendScreen(screenName: String, productId: String, isLoggedIn: Boolean, userId: String) {
        with(TalkTrackingConstants) {
            TrackApp.getInstance().gtm.sendGeneralEvent(
                    mapOf(
                            TRACKING_EVENT to EVENT_OPEN_SCREEN,
                            TRACKING_SCREEN_NAME to screenName,
                            TRACKING_PRODUCT_ID to productId,
                            TRACKING_IS_LOGGED_IN to isLoggedIn.toString(),
                            TRACKING_CURRENT_SITE to CURRENT_SITE_TALK,
                            TRACKING_USER_ID to userId,
                            TRACKING_BUSINESS_UNIT to BUSINESS_UNIT_TALK
                    )
            )
        }
    }

    fun eventClickThread(categories: String, userId: String, productId: String, talkId: String) {
        with(TalkTrackingConstants) {
            TrackApp.getInstance().gtm.sendGeneralEvent(
                    mapOf(
                            TRACKING_EVENT to EVENT_TALK,
                            TRACKING_EVENT_CATEGORY to EVENT_CATEGORY_TALK,
                            TRACKING_EVENT_ACTION to TalkReadingTrackingConstants.EVENT_ACTION_GO_TO_REPLY,
                            TRACKING_EVENT_LABEL to String.format(TalkReadingTrackingConstants.EVENT_LABEL_GO_TO_REPLY, talkId, categories),
                            TRACKING_SCREEN_NAME to SCREEN_NAME_TALK,
                            TRACKING_CURRENT_SITE to CURRENT_SITE_TALK,
                            TRACKING_USER_ID to userId,
                            TRACKING_BUSINESS_UNIT to BUSINESS_UNIT_TALK,
                            TRACKING_PRODUCT_ID to productId
                    )
            )
        }
    }

    fun eventLoadData(page: String, threadCount: String, userId: String, productId: String) {
        with(TalkTrackingConstants) {
            TrackApp.getInstance().gtm.sendGeneralEvent(
                    mapOf(
                            TRACKING_EVENT to EVENT_TALK,
                            TRACKING_EVENT_CATEGORY to EVENT_CATEGORY_TALK,
                            TRACKING_EVENT_ACTION to TalkReadingTrackingConstants.EVENT_ACTION_LOAD_DATA,
                            TRACKING_EVENT_LABEL to String.format(TalkReadingTrackingConstants.EVENT_LABEL_LOAD_DATA, page, threadCount),
                            TRACKING_SCREEN_NAME to SCREEN_NAME_TALK,
                            TRACKING_CURRENT_SITE to CURRENT_SITE_TALK,
                            TRACKING_USER_ID to userId,
                            TRACKING_BUSINESS_UNIT to BUSINESS_UNIT_TALK,
                            TRACKING_PRODUCT_ID to productId
                    )
            )
        }
    }

    fun eventAskNewQuestion(userId: String, productId: String) {
        with(TalkReadingTrackingConstants) {
            eventTalkReading(EVENT_ACTION_CLICK_CREATE_NEW_QUESTION, "", userId, productId)
        }
    }
}
