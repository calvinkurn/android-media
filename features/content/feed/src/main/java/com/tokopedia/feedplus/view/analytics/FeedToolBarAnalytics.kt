package com.tokopedia.feedplus.view.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.track.TrackApp
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
private const val CONTENT_FEED_TIMELINE = "content feed timeline"
private const val ACTION_CLICK_SEARCH_ICON = "click search icon"
private const val ACTION_CLICK_CHAT_ICON = "click chat icon"
private const val ACTION_CLICK_NOTIFICATION_ICON = "click notification icon"
private const val CLICK_BUAT_POST = "click buat post"

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

    fun sendClickBuatFeedPostEvent() {
        getTracker().sendGeneralEvent(
                DataLayer.mapOf(
                        EVENT_NAME, EVENT_CLICK_FEED,
                        EVENT_CATEGORY, CATEGORY_FEED,
                        EVENT_ACTION, CLICK_BUAT_POST,
                        EVENT_LABEL, "",
                        EVENT_BUSINESSUNIT, "content",
                        EVENT_CURRENTSITE, "tokopediamarketplace"
                )
        )
    }
}