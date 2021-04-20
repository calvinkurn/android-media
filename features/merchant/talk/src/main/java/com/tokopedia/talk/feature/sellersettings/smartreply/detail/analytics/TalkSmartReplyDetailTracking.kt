package com.tokopedia.talk.feature.sellersettings.smartreply.detail.analytics

import com.tokopedia.talk.common.analytics.TalkTrackingConstants
import com.tokopedia.talk.feature.sellersettings.common.analytics.TalkSellerSettingsTrackingConstants
import com.tokopedia.track.TrackApp

object TalkSmartReplyDetailTracking {

    private val tracker = TrackApp.getInstance().gtm

    fun eventClickSwitch(shopId: String, userId: String, isActive: Boolean) {
        tracker.sendGeneralEvent(getTrackingMap(shopId, userId, TalkSmartReplyDetailTrackingConstants.EVENT_ACTION_CLICK_ACTIVATE_SMART_REPLY, isActive.toString()))
    }

    fun eventClickSave(shopId: String, userId: String) {
        tracker.sendGeneralEvent(getTrackingMap(shopId, userId, TalkSmartReplyDetailTrackingConstants.EVENT_ACTION_CLICK_SAVE, ""))
    }

    private fun getTrackingMap(shopId: String, userId: String, eventAction: String, eventLabel: String): Map<String, String> {
        return mapOf(
                TalkTrackingConstants.TRACKING_EVENT to TalkSellerSettingsTrackingConstants.EVENT_INBOX_TALK,
                TalkTrackingConstants.TRACKING_SHOP_ID to shopId,
                TalkTrackingConstants.TRACKING_USER_ID to userId,
                TalkTrackingConstants.TRACKING_EVENT_ACTION to eventAction,
                TalkTrackingConstants.TRACKING_EVENT_LABEL to eventLabel,
                TalkTrackingConstants.TRACKING_BUSINESS_UNIT to TalkTrackingConstants.BUSINESS_UNIT_TALK_INBOX,
                TalkTrackingConstants.TRACKING_CURRENT_SITE to TalkTrackingConstants.CURRENT_SITE_TALK,
                TalkTrackingConstants.TRACKING_EVENT_CATEGORY to TalkSmartReplyDetailTrackingConstants.EVENT_CATEGORY
        )
    }
}