package com.tokopedia.scp_rewards.celebration.analytics

import com.tokopedia.scp_rewards.common.constants.TrackerConstants
import com.tokopedia.scp_rewards.common.constants.TrackerConstants.Event.CLICK_EVENT
import com.tokopedia.scp_rewards.common.constants.TrackerConstants.Event.VIEW_EVENT
import com.tokopedia.track.TrackApp
import com.tokopedia.track.constant.TrackerConstant.BUSINESS_UNIT
import com.tokopedia.track.constant.TrackerConstant.CURRENT_SITE
import com.tokopedia.track.constant.TrackerConstant.EVENT
import com.tokopedia.track.constant.TrackerConstant.EVENT_ACTION
import com.tokopedia.track.constant.TrackerConstant.EVENT_CATEGORY
import com.tokopedia.track.constant.TrackerConstant.EVENT_LABEL
import com.tokopedia.track.constant.TrackerConstant.TRACKER_ID

object CelebrationAnalytics {
    private val gtm = TrackApp.getInstance().gtm

    fun sendImpressionMedalCelebration(badgeId: String) {
        val map = mutableMapOf<String, Any>(
            EVENT to VIEW_EVENT,
            EVENT_ACTION to TrackerConstants.General.VIEW_PAGE_EVENT,
            EVENT_CATEGORY to "goto medali - celebration",
            EVENT_LABEL to badgeId,
            TRACKER_ID to "43301",
            BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    fun sendImpressionCelebrationLoading(badgeId: String) {
        val map = mutableMapOf<String, Any>(
            EVENT to VIEW_EVENT,
            EVENT_ACTION to TrackerConstants.General.VIEW_PAGE_EVENT,
            EVENT_CATEGORY to "goto medali - loading asset",
            EVENT_LABEL to badgeId,
            TRACKER_ID to "43896",
            BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    fun sendImpressionCelebrationError(badgeId: String) {
        val map = mutableMapOf<String, Any>(
            EVENT to VIEW_EVENT,
            EVENT_ACTION to TrackerConstants.General.VIEW_PAGE_EVENT,
            EVENT_CATEGORY to "goto medali - internet error",
            EVENT_LABEL to badgeId,
            TRACKER_ID to "43897",
            BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    fun sendClickRetryCelebration(badgeId: String) {
        val map = mutableMapOf<String, Any>(
            EVENT to CLICK_EVENT,
            EVENT_ACTION to "click coba lagi",
            EVENT_CATEGORY to "goto medali - internet error",
            EVENT_LABEL to badgeId,
            TRACKER_ID to "43898",
            BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    fun sendClickGoBackCelebration(badgeId: String) {
        val map = mutableMapOf<String, Any>(
            EVENT to CLICK_EVENT,
            EVENT_ACTION to "click pengaturan",
            EVENT_CATEGORY to "goto medali - internet error",
            EVENT_LABEL to badgeId,
            TRACKER_ID to "43899",
            BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    fun sendImpressionFallbackBadge(badgeId: String) {
        val map = mutableMapOf<String, Any>(
            EVENT to VIEW_EVENT,
            EVENT_ACTION to TrackerConstants.General.VIEW_PAGE_EVENT,
            EVENT_CATEGORY to "goto medali - celebration - with medali asset error",
            EVENT_LABEL to badgeId,
            TRACKER_ID to "43936",
            BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    fun sendImpressionNonWhitelistedError() {
        val map = mutableMapOf<String, Any>(
            EVENT to VIEW_EVENT,
            EVENT_ACTION to TrackerConstants.General.VIEW_PAGE_EVENT,
            EVENT_CATEGORY to TrackerConstants.EventCategory.MEDAL_CELEBRATION_NON_WHITELISTED,
            EVENT_LABEL to "",
            TRACKER_ID to TrackerConstants.Tracker.MEDAL_CELEBRATION_VIEW_PAGE_NON_WHITELISTED,
            BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    fun sendNonWhitelistedUserCtaClick() {
        val map = mutableMapOf<String, Any>(
            EVENT to VIEW_EVENT,
            EVENT_ACTION to TrackerConstants.General.CTA_CLICK,
            EVENT_CATEGORY to TrackerConstants.EventCategory.MEDAL_CELEBRATION_NON_WHITELISTED,
            EVENT_LABEL to "",
            TRACKER_ID to TrackerConstants.Tracker.MEDAL_CELEBRATION_NON_WHITELISTED_CTA_CLICK,
            BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    fun sendNonWhitelistedBackClick() {
        val map = mutableMapOf<String, Any>(
            EVENT to VIEW_EVENT,
            EVENT_ACTION to TrackerConstants.General.BACK_BUTTON_CLICK,
            EVENT_CATEGORY to TrackerConstants.EventCategory.MEDAL_CELEBRATION_NON_WHITELISTED,
            EVENT_LABEL to "",
            TRACKER_ID to TrackerConstants.Tracker.MEDAL_CELEBRATION_NON_WHITELISTED_BACK_CLICK,
            BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }
}
