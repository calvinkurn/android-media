package com.tokopedia.scp_rewards.detail.analytics

import android.util.Log
import com.tokopedia.scp_rewards.common.constants.TrackerConstants
import com.tokopedia.track.TrackApp
import com.tokopedia.track.constant.TrackerConstant
import org.json.JSONObject

object MedalDetailAnalyticsImpl : MedalDetailAnalytics {

    private val gtm = TrackApp.getInstance().gtm

    override fun sendImpressionMDP(badgeId: String) {
        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.VIEW_EVENT,
            TrackerConstant.EVENT_ACTION to TrackerConstants.General.VIEW_PAGE_EVENT,
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.General.MDP_EVENT_ACTION,
            TrackerConstant.EVENT_LABEL to badgeId,
            TrackerConstant.TRACKER_ID to "43979",
            TrackerConstant.BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            TrackerConstant.CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    override fun sendClickBackButton(badgeId: String) {
        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.CLICK_EVENT,
            TrackerConstant.EVENT_ACTION to TrackerConstants.General.BACK_BUTTON_CLICK,
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.General.MDP_EVENT_ACTION,
            TrackerConstant.EVENT_LABEL to badgeId,
            TrackerConstant.TRACKER_ID to "43980",
            TrackerConstant.BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            TrackerConstant.CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    override fun sendClickTncCta(badgeId: String) {
        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.CLICK_EVENT,
            TrackerConstant.EVENT_ACTION to "click s&k",
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.General.MDP_EVENT_ACTION,
            TrackerConstant.EVENT_LABEL to badgeId,
            TrackerConstant.TRACKER_ID to "43981",
            TrackerConstant.BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            TrackerConstant.CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    override fun sendImpressionMedali(
        badgeId: String,
        isLocked: Boolean,
        poweredBy: String,
        medalTitle: String,
        medalDescription: String
    ) {
        val eventLabel = JSONObject().apply {
            put(TrackerConstants.EventLabelProperties.BADGE_ID, badgeId)
            put(TrackerConstants.EventLabelProperties.MEDALI_STATUS, if(isLocked) "locked" else "unlock")
            put(TrackerConstants.EventLabelProperties.POWERED_BY, poweredBy)
            put(TrackerConstants.EventLabelProperties.MEDAL_TITLE, medalTitle)
            put(TrackerConstants.EventLabelProperties.MEDAL_DESCRIPTION, medalDescription)
        }
        Log.e("TAG-eventLabel", eventLabel.toString())
        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.VIEW_EVENT,
            TrackerConstant.EVENT_ACTION to "view medali",
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.General.MDP_EVENT_ACTION,
            TrackerConstant.EVENT_LABEL to eventLabel.toString(),
            TrackerConstant.TRACKER_ID to "43982",
            TrackerConstant.BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            TrackerConstant.CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    override fun sendClickMedali(
        badgeId: String,
        isLocked: Boolean,
        poweredBy: String,
        medalTitle: String,
        medalDescription: String
    ) {

        val eventLabel = JSONObject().apply {
            put(TrackerConstants.EventLabelProperties.BADGE_ID, badgeId)
            put(TrackerConstants.EventLabelProperties.MEDALI_STATUS, if(isLocked) "locked" else "unlock")
            put(TrackerConstants.EventLabelProperties.POWERED_BY, poweredBy)
            put(TrackerConstants.EventLabelProperties.MEDAL_TITLE, medalTitle)
            put(TrackerConstants.EventLabelProperties.MEDAL_DESCRIPTION, medalDescription)
        }
        Log.e("TAG-eventLabel", eventLabel.toString())
        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.CLICK_EVENT,
            TrackerConstant.EVENT_ACTION to "click medali",
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.General.MDP_EVENT_ACTION,
            TrackerConstant.EVENT_LABEL to eventLabel,
            TrackerConstant.TRACKER_ID to "43983",
            TrackerConstant.BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            TrackerConstant.CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    override fun sendImpressionProgressSection(badgeId: String) {
        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.VIEW_EVENT,
            TrackerConstant.EVENT_ACTION to "view progress section",
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.General.MDP_EVENT_ACTION,
            TrackerConstant.EVENT_LABEL to badgeId,
            TrackerConstant.TRACKER_ID to "43987",
            TrackerConstant.BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            TrackerConstant.CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    override fun sendImpressionBonusCoupon(badgeId: String) {
        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.VIEW_EVENT,
            TrackerConstant.EVENT_ACTION to "view bonus coupon",
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.General.MDP_EVENT_ACTION,
            TrackerConstant.EVENT_LABEL to badgeId,
            TrackerConstant.TRACKER_ID to "43988",
            TrackerConstant.BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            TrackerConstant.CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    override fun sendImpressionCabinetCta(badgeId: String) {
        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.VIEW_EVENT,
            TrackerConstant.EVENT_ACTION to "view button lihat koleksi",
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.General.MDP_EVENT_ACTION,
            TrackerConstant.EVENT_LABEL to badgeId,
            TrackerConstant.TRACKER_ID to "43990",
            TrackerConstant.BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            TrackerConstant.CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    override fun sendClickCabinetCta(badgeId: String) {
        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.CLICK_EVENT,
            TrackerConstant.EVENT_ACTION to "click button lihat koleksi",
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.General.MDP_EVENT_ACTION,
            TrackerConstant.EVENT_LABEL to badgeId,
            TrackerConstant.TRACKER_ID to "43991",
            TrackerConstant.BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            TrackerConstant.CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    override fun sendImpressionShopCta(badgeId: String) {
        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.VIEW_EVENT,
            TrackerConstant.EVENT_ACTION to "view button cta",
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.General.MDP_EVENT_ACTION,
            TrackerConstant.EVENT_LABEL to badgeId,
            TrackerConstant.TRACKER_ID to "43992",
            TrackerConstant.BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            TrackerConstant.CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    override fun sendClickShopCta(badgeId: String) {
        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.CLICK_EVENT,
            TrackerConstant.EVENT_ACTION to "click button cta",
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.General.MDP_EVENT_ACTION,
            TrackerConstant.EVENT_LABEL to badgeId,
            TrackerConstant.TRACKER_ID to "43993",
            TrackerConstant.BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            TrackerConstant.CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    override fun sendImpressionAutoApplySuccessToaster(badgeId: String) {
        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.VIEW_EVENT,
            TrackerConstant.EVENT_ACTION to "view ticker",
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.General.MDP_EVENT_ACTION,
            TrackerConstant.EVENT_LABEL to badgeId,
            TrackerConstant.TRACKER_ID to "43996",
            TrackerConstant.BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            TrackerConstant.CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    override fun sendImpressionPageShimmer(badgeId: String) {
        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.VIEW_EVENT,
            TrackerConstant.EVENT_ACTION to "view page skeleton",
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.General.MDP_EVENT_ACTION,
            TrackerConstant.EVENT_LABEL to badgeId,
            TrackerConstant.TRACKER_ID to "43999",
            TrackerConstant.BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            TrackerConstant.CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    override fun sendImpressionCouponError(badgeId: String) {
        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.VIEW_EVENT,
            TrackerConstant.EVENT_ACTION to "view bonus coupon error",
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.General.MDP_EVENT_ACTION,
            TrackerConstant.EVENT_LABEL to badgeId,
            TrackerConstant.TRACKER_ID to "43998",
            TrackerConstant.BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            TrackerConstant.CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    override fun sendImpressionCoachmark(badgeId: String) {
        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.VIEW_EVENT,
            TrackerConstant.EVENT_ACTION to "view coachmark",
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.General.MDP_EVENT_ACTION,
            TrackerConstant.EVENT_LABEL to badgeId,
            TrackerConstant.TRACKER_ID to "44000",
            TrackerConstant.BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            TrackerConstant.CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }
}
