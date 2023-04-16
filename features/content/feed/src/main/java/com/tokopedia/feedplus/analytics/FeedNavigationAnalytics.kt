package com.tokopedia.feedplus.analytics

import com.tokopedia.feedplus.analytics.FeedAnalytics.Companion.BUSINESS_UNIT_CONTENT
import com.tokopedia.feedplus.analytics.FeedAnalytics.Companion.CURRENT_SITE_MARKETPLACE
import com.tokopedia.feedplus.analytics.FeedAnalytics.Companion.KEY_BUSINESS_UNIT_EVENT
import com.tokopedia.feedplus.analytics.FeedAnalytics.Companion.KEY_CURRENT_SITE_EVENT
import com.tokopedia.feedplus.analytics.FeedAnalytics.Companion.KEY_EVENT_USER_ID
import com.tokopedia.feedplus.analytics.FeedAnalytics.Companion.KEY_TRACKER_ID
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 16/04/23
 */
class FeedNavigationAnalytics @Inject constructor(
    private val userSession: UserSessionInterface
) {

    private object Event {
        const val CLICK_CONTENT = "clickContent"
    }

    private object Action {
        const val CLICK_CREATE_BUTTON = "click - creation button"
        const val CLICK_CREATE_VIDEO = "click - buat video"
        const val CLICK_CREATE_POST = "click - buat post"
        const val CLICK_CREATE_LIVE = "click - buat live"
        const val CLICK_FOR_YOU_TAB = "click - untuk kamu tab"
        const val SWIPE_FOR_YOU_TAB = "swipe - right untuk kamu tab"
        const val CLICK_FOLLOWING_TAB = "click - following tab"
        const val SWIPE_FOLLOWING_TAB = "swipe - left following tab"
        const val CLICK_LIVE_BUTTON = "click - live button"
        const val CLICK_PROFILE_BUTTON = "click - user profile entry point"
    }


    fun eventClickCreationButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            generateGeneralTrackerData(
                Action.CLICK_CREATE_BUTTON,
                "${userSession.userId} - {prefix}",
                "41470"
            )
        )
    }

    fun eventClickCreateVideo() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            generateGeneralTrackerData(
                Action.CLICK_CREATE_VIDEO,
                "${userSession.userId} - {prefix}",
                "41471"
            )
        )
    }

    fun eventClickCreatePost() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            generateGeneralTrackerData(
                Action.CLICK_CREATE_POST,
                "${userSession.userId} - {prefix}",
                "41472"
            )
        )
    }

    fun eventClickCreateLive() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            generateGeneralTrackerData(
                Action.CLICK_CREATE_LIVE,
                "${userSession.userId} - {prefix}",
                "41473"
            )
        )
    }

    fun eventClickForYouTab() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            generateGeneralTrackerData(
                Action.CLICK_FOR_YOU_TAB,
                userSession.userId,
                "41474"
            )
        )
    }

    fun eventSwipeForYouTab() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            generateGeneralTrackerData(
                Action.SWIPE_FOR_YOU_TAB,
                userSession.userId,
                "41475"
            )
        )
    }

    fun eventClickFollowingTab() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            generateGeneralTrackerData(
                Action.CLICK_FOLLOWING_TAB,
                userSession.userId,
                "41476"
            )
        )
    }

    fun eventSwipeFollowingTab() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            generateGeneralTrackerData(
                Action.SWIPE_FOLLOWING_TAB,
                userSession.userId,
                "41477"
            )
        )
    }

    fun eventClickLiveButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            generateGeneralTrackerData(
                Action.CLICK_LIVE_BUTTON,
                "${userSession.userId} - {prefix}",
                "41478"
            )
        )
    }

    fun eventClickProfileButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            generateGeneralTrackerData(
                Action.CLICK_PROFILE_BUTTON,
                "${userSession.userId} - {prefix}",
                "41479"
            )
        )
    }

    private fun generateGeneralTrackerData(
        eventAction: String,
        eventLabel: String,
        trackerId: String
    ): Map<String, Any> = mapOf(
        EVENT to Event.CLICK_CONTENT,
        EVENT_CATEGORY to FeedAnalytics.CATEGORY_UNIFIED_FEED,
        EVENT_ACTION to eventAction,
        EVENT_LABEL to eventLabel,
        KEY_EVENT_USER_ID to userSession.userId,
        KEY_BUSINESS_UNIT_EVENT to BUSINESS_UNIT_CONTENT,
        KEY_CURRENT_SITE_EVENT to CURRENT_SITE_MARKETPLACE,
        KEY_TRACKER_ID to trackerId
    )
}
