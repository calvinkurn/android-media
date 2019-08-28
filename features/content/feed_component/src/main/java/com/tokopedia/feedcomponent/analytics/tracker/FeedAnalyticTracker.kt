package com.tokopedia.feedcomponent.analytics.tracker

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.*
import javax.inject.Inject

/**
 * Created by jegul on 2019-08-28.
 */
class FeedAnalyticTracker @Inject constructor() {

    private object Event {
        const val CLICK_FEED = "clickFeed"
    }

    private object Category {
        const val CONTENT_FEED_TIMELINE = "content feed timeline"
        const val FEED_DETAIL_PAGE = "feed detail page"
    }

    private object Action {
        const val CLICK_HASHTAG = "click hashtag"
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Row 3
     *
     * @param activityName - activity name
     * @param activityId - postId
     * @param mediaType - video or image
     * @param hashtag - the hashtag name
     */
    fun eventTimelineClickHashtag(activityName: String, activityId: String, mediaType: String, hashtag: String) {
        eventClickHashtag(
                Category.CONTENT_FEED_TIMELINE,
                activityName,
                activityId,
                mediaType,
                hashtag)
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Row 8
     *
     * @param activityName - activity name
     * @param activityId - postId
     * @param mediaType - video or image
     * @param hashtag - the hashtag name
     */
    fun eventDetailClickHashtag(activityName: String, activityId: String, mediaType: String, hashtag: String) {
        eventClickHashtag(
                Category.FEED_DETAIL_PAGE,
                activityName,
                activityId,
                mediaType,
                hashtag)
    }

    private fun eventClickHashtag(
            eventCategory: String,
            activityName: String,
            activityId: String,
            mediaType: String,
            hashtag: String) {
        trackGeneralEvent(
                eventName = Event.CLICK_FEED,
                eventCategory = eventCategory,
                eventAction = "${Action.CLICK_HASHTAG} - $activityName - $mediaType",
                eventLabel = "$activityId - $hashtag"
        )
    }

    private fun trackGeneralEvent(
            eventName: String,
            eventCategory: String,
            eventAction: String,
            eventLabel: String,
            additionalData: Map<String, Any> = emptyMap()) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                        EVENT, eventName,
                        EVENT_CATEGORY, eventCategory,
                        EVENT_ACTION, eventAction,
                        EVENT_LABEL, eventLabel
                ).plus(additionalData)
        )
    }
}