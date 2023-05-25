package com.tokopedia.inbox.universalinbox.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.track.TrackApp
import javax.inject.Inject

class UniversalInboxAnalytics @Inject constructor() {

    private val tracker = TrackApp.getInstance().gtm

    /**
     * Legacy trackers
     * Tracker from old inbox for discussion
     */
    fun sendNewPageInboxTalkTracking(userId: String, unreadCount: String) {
        val map: Map<String, Any> = DataLayer.mapOf(
            "event", "clickPDP",
            "eventCategory", "inbox page",
            "eventAction", "click - Diskusi",
            "eventLabel", "unread message:$unreadCount;",
            "screenName", "/inbox - talk",
            "currentSite", "tokopediamarketplace",
            "userId", userId,
            "businessUnit", "physical goods"
        )
        tracker.sendGeneralEvent(map)
    }
}
