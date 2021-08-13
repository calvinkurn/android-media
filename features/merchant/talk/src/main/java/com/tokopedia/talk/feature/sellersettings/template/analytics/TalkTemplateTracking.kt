package com.tokopedia.talk.feature.sellersettings.template.analytics

import com.tokopedia.talk.common.analytics.TalkTrackingConstants
import com.tokopedia.talk.feature.sellersettings.common.analytics.TalkSellerSettingsTrackingConstants
import com.tokopedia.track.TrackApp

object TalkTemplateTracking {

    private val tracker = TrackApp.getInstance().gtm

    fun eventClickAddTemplate(shopId: String, userId: String) {
        tracker.sendGeneralEvent(getTrackingMap(shopId, userId, TalkTemplateTrackingConstants.EVENT_ACTION_ADD_TEMPLATE, TalkTemplateTrackingConstants.EVENT_CATEGORY_TEMPLATE_LIST))
    }

    fun eventClickAddTemplateInBottomSheet(shopId: String, userId: String) {
        tracker.sendGeneralEvent(getTrackingMap(shopId, userId, TalkTemplateTrackingConstants.EVENT_ACTION_SAVE_TEMPLATE, TalkTemplateTrackingConstants.EVENT_CATEGORY_TEMPLATE_BOTTOM_SHEET))
    }

    fun eventActivateTemplates(shopId: String, userId: String, isActive: Boolean) {
        tracker.sendGeneralEvent(getTrackingMap(shopId, userId, TalkTemplateTrackingConstants.EVENT_ACTION_ACTIVATE_TEMPLATE, TalkTemplateTrackingConstants.EVENT_CATEGORY_TEMPLATE_LIST, isActive.toString()))
    }

    private fun getTrackingMap(shopId: String, userId: String, eventAction: String, eventCategory: String, eventLabel: String = ""): Map<String, String> {
        return mapOf(
                TalkTrackingConstants.TRACKING_EVENT to TalkSellerSettingsTrackingConstants.EVENT_INBOX_TALK,
                TalkTrackingConstants.TRACKING_SHOP_ID to shopId,
                TalkTrackingConstants.TRACKING_USER_ID to userId,
                TalkTrackingConstants.TRACKING_EVENT_ACTION to eventAction,
                TalkTrackingConstants.TRACKING_BUSINESS_UNIT to TalkTrackingConstants.BUSINESS_UNIT_TALK_INBOX,
                TalkTrackingConstants.TRACKING_CURRENT_SITE to TalkTrackingConstants.CURRENT_SITE_TALK,
                TalkTrackingConstants.TRACKING_EVENT_CATEGORY to eventCategory,
                TalkTrackingConstants.TRACKING_EVENT_LABEL to eventLabel
        )
    }
}