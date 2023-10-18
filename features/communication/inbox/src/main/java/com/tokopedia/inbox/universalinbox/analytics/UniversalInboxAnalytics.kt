package com.tokopedia.inbox.universalinbox.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.BUSINESS_UNIT
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.CLICK_CHAT_DRIVER
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.CLICK_CHAT_PEMBELI
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.CLICK_CHAT_PENJUAL
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.CLICK_COMMUNICATION
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.CLICK_DISCUSSION
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.CLICK_DISKUSI
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.CLICK_HELP
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.CLICK_NOTIF_CENTER
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.CLICK_PDP
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.CLICK_REVIEW
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.COMMUNICATION
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.CURRENT_SITE
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.EVENT
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.EVENT_ACTION
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.EVENT_CATEGORY
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.EVENT_LABEL
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.INBOX_PAGE
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.INBOX_TALK
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.NEW_INBOX_PAGE
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.PG
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.SCREEN_NAME
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.TOKOPEDIA_MARKETPLACE
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.TRACKER_ID
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.TRACKER_ID_44352
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.TRACKER_ID_44353
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.TRACKER_ID_44354
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.TRACKER_ID_44355
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.TRACKER_ID_44356
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.TRACKER_ID_44357
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.TRACKER_ID_44358
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.TRACKER_ID_44367
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.USER_ID
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.VIEW_COMMUNICATION_IRIS
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.VIEW_ON_INBOX_PAGE
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics
import javax.inject.Inject

class UniversalInboxAnalytics @Inject constructor() {

    private val tracker: ContextAnalytics by lazy(LazyThreadSafetyMode.NONE) {
        TrackApp.getInstance().gtm
    }

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

    /**
     * New trackers
     */
    fun viewOnInboxPage(
        abVariant: String,
        userRole: String,
        shopId: String,
        sellerChatCounter: String,
        buyerChatCounter: String,
        discussionCounter: String,
        reviewCounter: String,
        notifCenterCounter: String,
        driverCounter: String,
        helpCounter: String
    ) {
        val mapData = mapOf(
            EVENT to VIEW_COMMUNICATION_IRIS,
            EVENT_ACTION to VIEW_ON_INBOX_PAGE,
            EVENT_CATEGORY to NEW_INBOX_PAGE,
            EVENT_LABEL to "$abVariant - $userRole - $shopId - $sellerChatCounter - $buyerChatCounter - $discussionCounter - $reviewCounter - $notifCenterCounter - $driverCounter - $helpCounter",
            TRACKER_ID to TRACKER_ID_44352,
            BUSINESS_UNIT to COMMUNICATION,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE
        )
        tracker.sendGeneralEvent(mapData)
    }

    // Chat Penjual
    fun clickOnBuyerChat(
        abVariant: String,
        userRole: String,
        shopId: String,
        buyerChatCounter: String
    ) {
        val mapData = mapOf(
            EVENT to CLICK_COMMUNICATION,
            EVENT_ACTION to CLICK_CHAT_PENJUAL,
            EVENT_CATEGORY to NEW_INBOX_PAGE,
            EVENT_LABEL to "$abVariant - $userRole - $shopId - $buyerChatCounter",
            TRACKER_ID to TRACKER_ID_44354,
            BUSINESS_UNIT to COMMUNICATION,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE
        )
        tracker.sendGeneralEvent(mapData)
    }

    // Chat Pembeli
    fun clickOnSellerChat(
        abVariant: String,
        userRole: String,
        shopId: String,
        sellerChatCounter: String
    ) {
        val mapData = mapOf(
            EVENT to CLICK_COMMUNICATION,
            EVENT_ACTION to CLICK_CHAT_PEMBELI,
            EVENT_CATEGORY to NEW_INBOX_PAGE,
            EVENT_LABEL to "$abVariant - $userRole - $shopId - $sellerChatCounter",
            TRACKER_ID to TRACKER_ID_44353,
            BUSINESS_UNIT to COMMUNICATION,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE
        )
        tracker.sendGeneralEvent(mapData)
    }

    fun clickOnDiscussion(
        abVariant: String,
        userRole: String,
        shopId: String,
        discussionCounter: String
    ) {
        val mapData = mapOf(
            EVENT to CLICK_COMMUNICATION,
            EVENT_ACTION to CLICK_DISCUSSION,
            EVENT_CATEGORY to NEW_INBOX_PAGE,
            EVENT_LABEL to "$abVariant - $userRole - $shopId - $discussionCounter",
            TRACKER_ID to TRACKER_ID_44355,
            BUSINESS_UNIT to COMMUNICATION,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE
        )
        tracker.sendGeneralEvent(mapData)
    }

    fun clickOnReview(
        abVariant: String,
        userRole: String,
        shopId: String,
        reviewCounter: String
    ) {
        val mapData = mapOf(
            EVENT to CLICK_COMMUNICATION,
            EVENT_ACTION to CLICK_REVIEW,
            EVENT_CATEGORY to NEW_INBOX_PAGE,
            EVENT_LABEL to "$abVariant - $userRole - $shopId - $reviewCounter",
            TRACKER_ID to TRACKER_ID_44356,
            BUSINESS_UNIT to COMMUNICATION,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE
        )
        tracker.sendGeneralEvent(mapData)
    }

    fun clickOnHelp(
        abVariant: String,
        userRole: String,
        shopId: String,
        helpCounter: String
    ) {
        val mapData = mapOf(
            EVENT to CLICK_COMMUNICATION,
            EVENT_ACTION to CLICK_HELP,
            EVENT_CATEGORY to NEW_INBOX_PAGE,
            EVENT_LABEL to "$abVariant - $userRole - $shopId - $helpCounter",
            TRACKER_ID to TRACKER_ID_44357,
            BUSINESS_UNIT to COMMUNICATION,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE
        )
        tracker.sendGeneralEvent(mapData)
    }

    fun clickOnChatDriver(
        abVariant: String,
        userRole: String,
        shopId: String,
        chatDriverCounter: String
    ) {
        val mapData = mapOf(
            EVENT to CLICK_COMMUNICATION,
            EVENT_ACTION to CLICK_CHAT_DRIVER,
            EVENT_CATEGORY to NEW_INBOX_PAGE,
            EVENT_LABEL to "$abVariant - $userRole - $shopId - $chatDriverCounter",
            TRACKER_ID to TRACKER_ID_44358,
            BUSINESS_UNIT to COMMUNICATION,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE
        )
        tracker.sendGeneralEvent(mapData)
    }

    fun clickOnNotifCenter(
        abVariant: String,
        userRole: String,
        shopId: String,
        notifCenterCounter: String
    ) {
        val mapData = mapOf(
            EVENT to CLICK_COMMUNICATION,
            EVENT_ACTION to CLICK_NOTIF_CENTER,
            EVENT_CATEGORY to NEW_INBOX_PAGE,
            EVENT_LABEL to "$abVariant - $userRole - $shopId - $notifCenterCounter",
            TRACKER_ID to TRACKER_ID_44367,
            BUSINESS_UNIT to COMMUNICATION,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE
        )
        tracker.sendGeneralEvent(mapData)
    }
}
