package com.tokopedia.inbox.universalinbox.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.BUSINESS_UNIT
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.CLICK_DISKUSI
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.CLICK_PDP
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.CURRENT_SITE
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.EVENT
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.EVENT_ACTION
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.EVENT_CATEGORY
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.EVENT_LABEL
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.INBOX_PAGE
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.INBOX_TALK
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.PG
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.SCREEN_NAME
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.TOKOPEDIA_MARKETPLACE
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.USER_ID
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
            EVENT, CLICK_PDP,
            EVENT_CATEGORY, INBOX_PAGE,
            EVENT_ACTION, CLICK_DISKUSI,
            EVENT_LABEL, "unread message:$unreadCount;",
            SCREEN_NAME, INBOX_TALK,
            CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
            USER_ID, userId,
            BUSINESS_UNIT, PG
        )
        tracker.sendGeneralEvent(map)
    }
}
