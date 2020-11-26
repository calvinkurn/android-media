package com.tokopedia.feedplus.view.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics
import javax.inject.Inject

private const val EVENT_CLICK_FEED = "clickFeed"
private const val CONTENT_FEED_TIMELINE = "content feed timeline"
private const val ACTION_CLICK_SEARCH_ICON = "click search icon"
private const val ACTION_CLICK_CHAT_ICON = "click chat icon"
private const val ACTION_CLICK_NOTIFICATION_ICON = "click notification icon"

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

}