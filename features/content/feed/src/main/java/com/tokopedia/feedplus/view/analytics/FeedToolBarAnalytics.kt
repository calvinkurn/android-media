package com.tokopedia.feedplus.view.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.explore.analytics.ContentExloreEventTracking.Event.*
import com.tokopedia.explore.analytics.ContentExloreEventTracking.Screen.*
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics
import javax.inject.Inject

private const val EVENT_NAME = "event"
private const val EVENT_CATEGORY = "eventCategory"
private const val EVENT_ACTION = "eventAction"
private const val EVENT_LABEL = "eventLabel"
private const val EVENT_BUSINESSUNIT = "businessUnit"
private const val EVENT_CURRENTSITE = "currentSite"
private const val CATEGORY_FEED = "Feed"
private const val EVENT_CLICK_FEED = "clickFeed"
private const val CLICK_HOMEPAGE = "clickHomepage"
private const val ACTION_CLICK_SEARCH_ICON = "click search icon"
private const val ACTION_CLICK_CHAT_ICON = "click chat icon"
private const val ACTION_CLICK_NOTIFICATION_ICON = "click notification icon"
private const val CLICK_BUAT_POST = "click buat post"
const val CLICK_FEED_TAB = "click - feed tab"

const val CONTENT_FEED_CREATION = "content feed creation"
const val CONTENT_FEED_TIMELINE = "content feed timeline"
const val CLICK_RETRY_ON_FEED_TO_POST = "click on retry button"
const val FORMAT_TWO_PARAM = "%s - %s"
const val CONTENT = "content"
const val MARKETPLACE = "tokopediamarketplace"

private object EventLabel {
    const val UPDATE = "update"
    const val EXPLORE = "explore"
    const val VIDEO = "video"
}



class FeedToolBarAnalytics @Inject constructor() {

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    fun eventClickSearch() {
        getTracker().sendGeneralEvent(EVENT_CLICK_FEED, CONTENT_FEED_TIMELINE, ACTION_CLICK_SEARCH_ICON, "")
    }

    fun eventClickInbox() {
        getTracker().sendGeneralEvent(EVENT_CLICK_FEED, CONTENT_FEED_TIMELINE, ACTION_CLICK_CHAT_ICON, "")
    }

    fun eventClickNotification() {
        getTracker().sendGeneralEvent(EVENT_CLICK_FEED, CONTENT_FEED_TIMELINE, ACTION_CLICK_NOTIFICATION_ICON, "")
    }

    fun eventClickRetryToPostOnProgressBar(shopId: String) {
        getTracker().sendGeneralEvent(
            DataLayer.mapOf(
                EVENT_NAME, EVENT_CLICK_FEED,
                EVENT_CATEGORY, CONTENT_FEED_CREATION,
                EVENT_ACTION, CLICK_RETRY_ON_FEED_TO_POST,
                EVENT_LABEL, shopId,
                EVENT_BUSINESSUNIT, CONTENT,
                EVENT_CURRENTSITE, MARKETPLACE
            )
        )
    }
    fun clickOnVideoTabOnFeedPage(position: Int) {
        getTracker().sendGeneralEvent(
                DataLayer.mapOf(
                        EVENT_NAME, CLICK_HOMEPAGE,
                        EVENT_CATEGORY, CONTENT_FEED_TIMELINE,
                        EVENT_ACTION, CLICK_FEED_TAB,
                        EVENT_LABEL, when (position) {
                    0 -> EventLabel.UPDATE
                    1 -> EventLabel.EXPLORE
                    else -> EventLabel.VIDEO
                },
                        EVENT_BUSINESSUNIT, CONTENT,
                        EVENT_CURRENTSITE, MARKETPLACE
                )
        )
    }
    fun userVisitsFeed(isLoggedInStatus: String, userID: String) {
        val generalData = mapOf(
            TrackAppUtils.EVENT to OPEN_SCREEN,
            EVENT_BUSINESSUNIT to CONTENT,
            EVENT_CURRENTSITE to MARKETPLACE,
            USER_ID to userID,
            IS_LOGGED_IN to isLoggedInStatus,
            SCREEN_NAME to "/feed"
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(generalData)

    }
     fun createAnalyticsForOpenScreen(
            position: Int,
            isLoggedInStatus: String,
            userId: String
    ) {
        val generalData = mapOf(
                TrackAppUtils.EVENT to OPEN_SCREEN,
                EVENT_BUSINESSUNIT to CONTENT,
                EVENT_CURRENTSITE to MARKETPLACE,
                USER_ID to userId,
                IS_LOGGED_IN to isLoggedInStatus,
                SCREEN_NAME to when (position) {
                    0 -> SCREEN_NAME_UPDATE
                    1 -> SCREEN_NAME_EXPLORE
                    else -> SCREEN_NAME_VIDEO
                }
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(generalData)

    }
}