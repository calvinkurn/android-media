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
        const val CLICK_SOCIAL_COMMERCE = "clickSocialCommerce"
    }

    private object Category {
        const val CONTENT_FEED_TIMELINE = "content feed timeline"
        const val FEED_DETAIL_PAGE = "feed detail page"

        const val MY_PROFILE_SOCIALCOMMERCE = "my profile socialcommerce"
        const val USER_PROFILE_SOCIALCOMMERCE = "user profile socialcommerce"

        const val CONTENT_FEED_SHOP_PAGE = "content feed - shop page"
    }

    private object Action {
        const val CLICK_HASHTAG = "click hashtag"
        const val CLICK_READ_MORE = "click read more"
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
    fun eventTimelineClickHashtag(activityId: String, activityName: String, mediaType: String, hashtag: String) {
        eventClickHashtag(
                Category.CONTENT_FEED_TIMELINE,
                activityId,
                activityName,
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
    fun eventDetailClickHashtag(activityId: String, activityName: String, mediaType: String, hashtag: String) {
        eventClickHashtag(
                Category.FEED_DETAIL_PAGE,
                activityId,
                activityName,
                mediaType,
                hashtag)
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Row 28 (self-profile)
     * Row 35 (other-profile)
     *
     * @param isOwner - `true` indicates opening self-profile, `false` indicates opening other-profile
     * @param activityName - activity name
     * @param activityId - postId
     * @param mediaType - video or image
     */
    fun eventProfileClickReadMore(isOwner: Boolean, activityId: String, activityName: String, mediaType: String) {
        eventClickReadMore(
                Event.CLICK_SOCIAL_COMMERCE,
                if (isOwner) Category.MY_PROFILE_SOCIALCOMMERCE else Category.USER_PROFILE_SOCIALCOMMERCE,
                activityId,
                activityName,
                mediaType
        )
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Row 29
     *
     * @param activityName - activity name
     * @param activityId - postId
     * @param mediaType - video or image
     */
    fun eventShopPageClickReadMore(activityId: String, activityName: String, mediaType: String) {
        eventClickReadMore(
                Event.CLICK_FEED,
                Category.CONTENT_FEED_SHOP_PAGE,
                activityId,
                activityName,
                mediaType
        )
    }


    /**
     * Base track click read more
     */
    private fun eventClickReadMore(
            eventName: String,
            eventCategory: String,
            activityId: String,
            activityName: String,
            mediaType: String
    ) {
        trackGeneralEvent(
                eventName = eventName,
                eventCategory = eventCategory,
                eventAction = "${Action.CLICK_READ_MORE} - $activityName - $mediaType",
                eventLabel = activityId
        )
    }

    /**
     * Base track click hashtag
     */
    private fun eventClickHashtag(
            eventCategory: String,
            activityId: String,
            activityName: String,
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