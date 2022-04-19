package com.tokopedia.talk.feature.sellersettings.settings.analytics

import com.tokopedia.talk.common.analytics.TalkTrackingConstants
import com.tokopedia.talk.feature.sellersettings.common.analytics.TalkSellerSettingsTrackingConstants
import com.tokopedia.track.TrackApp

object TalkSettingsTracking {

    private val tracker = TrackApp.getInstance().gtm

    fun eventClickOptionSmartReply(shopId: String, userId: String) {
        tracker.sendGeneralEvent(getTrackingMap(shopId, userId, TalkSettingsTrackingConstants.EVENT_ACTION_CLICK_SMART_REPLY_SUBMENU))
    }

    fun eventClickOptionTemplate(shopId: String, userId: String) {
        tracker.sendGeneralEvent(getTrackingMap(shopId, userId, TalkSettingsTrackingConstants.EVENT_ACTION_CLICK_TEMPLATE_SUBMENU))
    }

    private fun getTrackingMap(shopId: String, userId: String, eventAction: String): Map<String, String> {
        return mapOf(
                TalkTrackingConstants.TRACKING_EVENT to TalkSellerSettingsTrackingConstants.EVENT_INBOX_TALK,
                TalkTrackingConstants.TRACKING_SHOP_ID to shopId,
                TalkTrackingConstants.TRACKING_USER_ID to userId,
                TalkTrackingConstants.TRACKING_EVENT_ACTION to eventAction,
                TalkTrackingConstants.TRACKING_BUSINESS_UNIT to TalkTrackingConstants.BUSINESS_UNIT_TALK_INBOX,
                TalkTrackingConstants.TRACKING_CURRENT_SITE to TalkTrackingConstants.CURRENT_SITE_TALK,
                TalkTrackingConstants.TRACKING_EVENT_LABEL to "",
                TalkTrackingConstants.TRACKING_EVENT_CATEGORY to TalkSettingsTrackingConstants.EVENT_CATEGORY_SETTINGS
        )
    }
}