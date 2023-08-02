package com.tokopedia.scp_rewards.detail.analytics

import com.tokopedia.scp_rewards.common.constants.TrackerConstants
import com.tokopedia.track.TrackApp
import com.tokopedia.track.constant.TrackerConstant
import org.json.JSONObject

object MedalDetailAnalyticsImpl : MedalDetailAnalytics {

    private val gtm = TrackApp.getInstance().gtm

    override fun sendImpressionMDP(badgeId: String) {
        val eventLabel =
            JSONObject().apply { put(TrackerConstants.EventLabelProperties.BADGE_ID, badgeId) }
                .toString()

        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.VIEW_EVENT,
            TrackerConstant.EVENT_ACTION to TrackerConstants.General.VIEW_PAGE_EVENT,
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.General.MDP_EVENT_ACTION,
            TrackerConstant.EVENT_LABEL to eventLabel,
            TrackerConstant.TRACKER_ID to TrackerConstants.Tracker.MDP_VIEW_PAGE,
            TrackerConstant.BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            TrackerConstant.CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    override fun sendClickBackButton(badgeId: String) {
        val eventLabel =
            JSONObject().apply { put(TrackerConstants.EventLabelProperties.BADGE_ID, badgeId) }
                .toString()

        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.CLICK_EVENT,
            TrackerConstant.EVENT_ACTION to TrackerConstants.General.BACK_BUTTON_CLICK,
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.General.MDP_EVENT_ACTION,
            TrackerConstant.EVENT_LABEL to eventLabel,
            TrackerConstant.TRACKER_ID to TrackerConstants.Tracker.MDP_CLICK_BACK,
            TrackerConstant.BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            TrackerConstant.CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    override fun sendClickTncCta(badgeId: String) {
        val eventLabel =
            JSONObject().apply { put(TrackerConstants.EventLabelProperties.BADGE_ID, badgeId) }
                .toString()

        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.CLICK_EVENT,
            TrackerConstant.EVENT_ACTION to "click s&k",
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.General.MDP_EVENT_ACTION,
            TrackerConstant.EVENT_LABEL to eventLabel,
            TrackerConstant.TRACKER_ID to TrackerConstants.Tracker.MDP_CLICK_TNC,
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
            put(
                TrackerConstants.EventLabelProperties.MEDALI_STATUS,
                if (isLocked) "locked" else "unlock"
            )
            put(TrackerConstants.EventLabelProperties.POWERED_BY, poweredBy)
            put(TrackerConstants.EventLabelProperties.MEDAL_TITLE, medalTitle)
            put(TrackerConstants.EventLabelProperties.MEDAL_DESCRIPTION, medalDescription)
        }
        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.VIEW_EVENT,
            TrackerConstant.EVENT_ACTION to "view medali",
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.General.MDP_EVENT_ACTION,
            TrackerConstant.EVENT_LABEL to eventLabel.toString(),
            TrackerConstant.TRACKER_ID to TrackerConstants.Tracker.MDP_VIEW_MEDAL,
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
            put(
                TrackerConstants.EventLabelProperties.MEDALI_STATUS,
                if (isLocked) "locked" else "unlock"
            )
            put(TrackerConstants.EventLabelProperties.POWERED_BY, poweredBy)
            put(TrackerConstants.EventLabelProperties.MEDAL_TITLE, medalTitle)
            put(TrackerConstants.EventLabelProperties.MEDAL_DESCRIPTION, medalDescription)
        }
        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.CLICK_EVENT,
            TrackerConstant.EVENT_ACTION to "click medali",
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.General.MDP_EVENT_ACTION,
            TrackerConstant.EVENT_LABEL to eventLabel,
            TrackerConstant.TRACKER_ID to TrackerConstants.Tracker.MDP_CLICK_MEDAL,
            TrackerConstant.BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            TrackerConstant.CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    override fun sendImpressionProgressSection(
        badgeId: String,
        isLocked: Boolean,
        taskProgressPercent: String,
        noOfTasksCompleted: String
    ) {
        val eventLabel = JSONObject().apply {
            put(TrackerConstants.EventLabelProperties.BADGE_ID, badgeId)
            put(
                TrackerConstants.EventLabelProperties.MEDALI_STATUS,
                if (isLocked) "locked" else "unlock"
            )
            put(
                TrackerConstants.EventLabelProperties.MEDAL_PROGRESS_PERCENTAGE,
                taskProgressPercent
            )
            put(
                TrackerConstants.EventLabelProperties.MEDAL_PROGRESS_TASK_COMPLETED,
                noOfTasksCompleted
            )
        }
        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.VIEW_EVENT,
            TrackerConstant.EVENT_ACTION to "view progress section",
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.General.MDP_EVENT_ACTION,
            TrackerConstant.EVENT_LABEL to eventLabel.toString(),
            TrackerConstant.TRACKER_ID to TrackerConstants.Tracker.MDP_VIEW_MEDAL_PROGRESS,
            TrackerConstant.BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            TrackerConstant.CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    override fun sendImpressionBonusCoupon(
        badgeId: String,
        promoCode: String,
        couponStatus: String,
        couponNotes: String
    ) {
        val eventLabel = JSONObject().apply {
            put(TrackerConstants.EventLabelProperties.BADGE_ID, badgeId)
            put(TrackerConstants.EventLabelProperties.MEDAL_COUPON_PROMO_CODE, promoCode)
            put(TrackerConstants.EventLabelProperties.MEDAL_COUPON_STATUS, couponStatus)
            put(TrackerConstants.EventLabelProperties.MEDAL_COUPON_NOTES, couponNotes)
        }
        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.VIEW_EVENT,
            TrackerConstant.EVENT_ACTION to "view bonus coupon",
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.General.MDP_EVENT_ACTION,
            TrackerConstant.EVENT_LABEL to eventLabel,
            TrackerConstant.TRACKER_ID to TrackerConstants.Tracker.MDP_VIEW_MEDAL_BONUS,
            TrackerConstant.BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            TrackerConstant.CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    override fun sendImpressionCabinetCta(
        badgeId: String,
        promoCode: String,
        couponStatus: String,
        couponNotes: String
    ) {
        val eventLabel = JSONObject().apply {
            put(TrackerConstants.EventLabelProperties.BADGE_ID, badgeId)
            put(TrackerConstants.EventLabelProperties.MEDAL_COUPON_PROMO_CODE, promoCode)
            put(TrackerConstants.EventLabelProperties.MEDAL_COUPON_STATUS, couponStatus)
            put(TrackerConstants.EventLabelProperties.MEDAL_COUPON_NOTES, couponNotes)
        }
        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.VIEW_EVENT,
            TrackerConstant.EVENT_ACTION to "view button lihat koleksi",
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.General.MDP_EVENT_ACTION,
            TrackerConstant.EVENT_LABEL to eventLabel,
            TrackerConstant.TRACKER_ID to TrackerConstants.Tracker.MDP_VIEW_SEE_CABINET_CTA,
            TrackerConstant.BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            TrackerConstant.CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    override fun sendClickCabinetCta(
        badgeId: String,
        promoCode: String,
        couponStatus: String,
        couponNotes: String
    ) {
        val eventLabel = JSONObject().apply {
            put(TrackerConstants.EventLabelProperties.BADGE_ID, badgeId)
            put(TrackerConstants.EventLabelProperties.MEDAL_COUPON_PROMO_CODE, promoCode)
            put(TrackerConstants.EventLabelProperties.MEDAL_COUPON_STATUS, couponStatus)
            put(TrackerConstants.EventLabelProperties.MEDAL_COUPON_NOTES, couponNotes)
        }
        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.CLICK_EVENT,
            TrackerConstant.EVENT_ACTION to "click button lihat koleksi",
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.General.MDP_EVENT_ACTION,
            TrackerConstant.EVENT_LABEL to eventLabel,
            TrackerConstant.TRACKER_ID to TrackerConstants.Tracker.MDP_CLICK_SEE_CABINET_CTA,
            TrackerConstant.BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            TrackerConstant.CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    override fun sendImpressionShopCta(
        badgeId: String,
        promoCode: String,
        couponStatus: String,
        couponNotes: String,
        ctaText: String
    ) {
        val eventLabel = JSONObject().apply {
            put(TrackerConstants.EventLabelProperties.BADGE_ID, badgeId)
            put(TrackerConstants.EventLabelProperties.MEDAL_COUPON_PROMO_CODE, promoCode)
            put(TrackerConstants.EventLabelProperties.MEDAL_COUPON_STATUS, couponStatus)
            put(TrackerConstants.EventLabelProperties.MEDAL_COUPON_NOTES, couponNotes)
            put(TrackerConstants.EventLabelProperties.MEDAL_CTA_TEXT, ctaText)
        }
        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.VIEW_EVENT,
            TrackerConstant.EVENT_ACTION to "view button cta",
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.General.MDP_EVENT_ACTION,
            TrackerConstant.EVENT_LABEL to eventLabel,
            TrackerConstant.TRACKER_ID to TrackerConstants.Tracker.MDP_VIEW_PRIMARY_CTA,
            TrackerConstant.BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            TrackerConstant.CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    override fun sendClickShopCta(
        badgeId: String,
        promoCode: String,
        couponStatus: String,
        couponNotes: String,
        ctaText: String
    ) {
        val eventLabel = JSONObject().apply {
            put(TrackerConstants.EventLabelProperties.BADGE_ID, badgeId)
            put(TrackerConstants.EventLabelProperties.MEDAL_COUPON_PROMO_CODE, promoCode)
            put(TrackerConstants.EventLabelProperties.MEDAL_COUPON_STATUS, couponStatus)
            put(TrackerConstants.EventLabelProperties.MEDAL_COUPON_NOTES, couponNotes)
            put(TrackerConstants.EventLabelProperties.MEDAL_CTA_TEXT, ctaText)
        }
        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.CLICK_EVENT,
            TrackerConstant.EVENT_ACTION to "click button cta",
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.General.MDP_EVENT_ACTION,
            TrackerConstant.EVENT_LABEL to eventLabel,
            TrackerConstant.TRACKER_ID to TrackerConstants.Tracker.MDP_CLICK_PRIMARY_CTA,
            TrackerConstant.BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            TrackerConstant.CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    override fun sendImpressionAutoApplyToaster(
        badgeId: String,
        promoCode: String,
        couponStatus: String,
        couponNotes: String,
        isAutoApplySuccess: Boolean
    ) {
        val eventLabel = JSONObject().apply {
            put(TrackerConstants.EventLabelProperties.BADGE_ID, badgeId)
            put(TrackerConstants.EventLabelProperties.MEDAL_COUPON_PROMO_CODE, promoCode)
            put(TrackerConstants.EventLabelProperties.MEDAL_COUPON_STATUS, couponStatus)
            put(TrackerConstants.EventLabelProperties.MEDAL_COUPON_NOTES, couponNotes)
            put(
                TrackerConstants.EventLabelProperties.MEDAL_TICKER_STATUS,
                if (isAutoApplySuccess) "success" else "failed"
            )
        }
        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.VIEW_EVENT,
            TrackerConstant.EVENT_ACTION to "view ticker",
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.General.MDP_EVENT_ACTION,
            TrackerConstant.EVENT_LABEL to eventLabel,
            TrackerConstant.TRACKER_ID to TrackerConstants.Tracker.MDP_VIEW_COUPON_SUCCESS_TOASTER,
            TrackerConstant.BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            TrackerConstant.CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    override fun sendImpressionCouponError(
        badgeId: String,
        promoCode: String
    ) {
        val eventLabel = JSONObject().apply {
            put(TrackerConstants.EventLabelProperties.BADGE_ID, badgeId)
            put(TrackerConstants.EventLabelProperties.MEDAL_COUPON_PROMO_CODE, promoCode)
        }
        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.VIEW_EVENT,
            TrackerConstant.EVENT_ACTION to "view bonus coupon error",
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.General.MDP_EVENT_ACTION,
            TrackerConstant.EVENT_LABEL to eventLabel,
            TrackerConstant.TRACKER_ID to TrackerConstants.Tracker.MDP_VIEW_COUPON_LOAD_ERROR,
            TrackerConstant.BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            TrackerConstant.CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    override fun sendImpressionPageShimmer(badgeId: String) {
        val eventLabel = JSONObject().apply {
            put(TrackerConstants.EventLabelProperties.BADGE_ID, badgeId)
        }
        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.VIEW_EVENT,
            TrackerConstant.EVENT_ACTION to "view page skeleton",
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.General.MDP_EVENT_ACTION,
            TrackerConstant.EVENT_LABEL to eventLabel,
            TrackerConstant.TRACKER_ID to TrackerConstants.Tracker.MDP_VIEW_PAGE_SHIMMER,
            TrackerConstant.BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            TrackerConstant.CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    override fun sendImpressionNonWhitelistedError() {
        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.VIEW_EVENT,
            TrackerConstant.EVENT_ACTION to TrackerConstants.General.VIEW_PAGE_EVENT,
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.EventCategory.MDP_NON_WHITELISTED,
            TrackerConstant.EVENT_LABEL to "",
            TrackerConstant.TRACKER_ID to TrackerConstants.Tracker.MDP_VIEW_PAGE_NON_WHITELISTED,
            TrackerConstant.BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            TrackerConstant.CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    override fun sendNonWhitelistedUserCtaClick() {
        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.VIEW_EVENT,
            TrackerConstant.EVENT_ACTION to TrackerConstants.General.CTA_CLICK,
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.EventCategory.MDP_NON_WHITELISTED,
            TrackerConstant.EVENT_LABEL to "",
            TrackerConstant.TRACKER_ID to TrackerConstants.Tracker.MDP_NON_WHITELISTED_CTA_CLICK,
            TrackerConstant.BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            TrackerConstant.CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }

    override fun sendNonWhitelistedBackClick() {
        val map = mutableMapOf<String, Any>(
            TrackerConstant.EVENT to TrackerConstants.Event.VIEW_EVENT,
            TrackerConstant.EVENT_ACTION to TrackerConstants.General.BACK_BUTTON_CLICK,
            TrackerConstant.EVENT_CATEGORY to TrackerConstants.EventCategory.MDP_NON_WHITELISTED,
            TrackerConstant.EVENT_LABEL to "",
            TrackerConstant.TRACKER_ID to TrackerConstants.Tracker.MDP_NON_WHITELISTED_BACK_CLICK,
            TrackerConstant.BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            TrackerConstant.CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }
}
