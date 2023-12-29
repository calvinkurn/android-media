package com.tokopedia.scp_rewards_touchpoints.bottomsheet.analytics

import com.tokopedia.scp_rewards_touchpoints.common.BADGE_ID
import com.tokopedia.scp_rewards_touchpoints.common.CELEBRATION_BOTTOMSHEET_CATEGORY
import com.tokopedia.scp_rewards_touchpoints.common.CLICK_EVENT
import com.tokopedia.scp_rewards_touchpoints.common.COUPON_PROMO_CODE
import com.tokopedia.scp_rewards_touchpoints.common.SCP_BUSINESS_UNIT
import com.tokopedia.scp_rewards_touchpoints.common.SCP_CURRENT_SITE
import com.tokopedia.scp_rewards_touchpoints.common.VIEW_EVENT
import com.tokopedia.track.TrackApp
import com.tokopedia.track.constant.TrackerConstant.BUSINESS_UNIT
import com.tokopedia.track.constant.TrackerConstant.CURRENT_SITE
import com.tokopedia.track.constant.TrackerConstant.EVENT
import com.tokopedia.track.constant.TrackerConstant.EVENT_ACTION
import com.tokopedia.track.constant.TrackerConstant.EVENT_CATEGORY
import com.tokopedia.track.constant.TrackerConstant.EVENT_LABEL
import com.tokopedia.track.constant.TrackerConstant.TRACKER_ID
import org.json.JSONObject

object CelebrationBottomSheetAnalytics {

    private val gtm = TrackApp.getInstance().gtm

    fun impressionCelebrationBottomSheet(badgeId: String, couponCode: String) {
        val label = JSONObject().apply {
            put(BADGE_ID, badgeId)
            put(COUPON_PROMO_CODE, couponCode)
        }
        val map = mutableMapOf<String, Any>(
            EVENT to VIEW_EVENT,
            EVENT_ACTION to "view bottom sheet",
            EVENT_CATEGORY to CELEBRATION_BOTTOMSHEET_CATEGORY,
            EVENT_LABEL to label.toString(),
            TRACKER_ID to "44709"

        ).apply { fillCommonMap(this) }
        gtm.sendGeneralEvent(map)
    }

    fun clickCloseBottomSheet(badgeId: String, couponCode: String) {
        val label = JSONObject().apply {
            put(BADGE_ID, badgeId)
            put(COUPON_PROMO_CODE, couponCode)
        }
        val map = mutableMapOf<String, Any>(
            EVENT to CLICK_EVENT,
            EVENT_ACTION to "click close",
            EVENT_CATEGORY to CELEBRATION_BOTTOMSHEET_CATEGORY,
            EVENT_LABEL to label.toString(),
            TRACKER_ID to "44710"

        ).apply { fillCommonMap(this) }
        gtm.sendGeneralEvent(map)
    }

    fun clickPrimaryCta(badgeId: String, couponCode: String) {
        val label = JSONObject().apply {
            put(BADGE_ID, badgeId)
            put(COUPON_PROMO_CODE, couponCode)
        }
        val map = mutableMapOf<String, Any>(
            EVENT to CLICK_EVENT,
            EVENT_ACTION to "click main CTA",
            EVENT_CATEGORY to CELEBRATION_BOTTOMSHEET_CATEGORY,
            EVENT_LABEL to label.toString(),
            TRACKER_ID to "44711"

        ).apply { fillCommonMap(this) }
        gtm.sendGeneralEvent(map)
    }

    fun clickSecondaryCta(badgeId: String, couponCode: String) {
        val label = JSONObject().apply {
            put(BADGE_ID, badgeId)
            put(COUPON_PROMO_CODE, couponCode)
        }
        val map = mutableMapOf<String, Any>(
            EVENT to CLICK_EVENT,
            EVENT_ACTION to "click secondary CTA",
            EVENT_CATEGORY to CELEBRATION_BOTTOMSHEET_CATEGORY,
            EVENT_LABEL to label.toString(),
            TRACKER_ID to "44712"

        ).apply { fillCommonMap(this) }
        gtm.sendGeneralEvent(map)
    }

    private fun fillCommonMap(map: MutableMap<String, Any>) {
        map.apply {
            put(BUSINESS_UNIT, SCP_BUSINESS_UNIT)
            put(CURRENT_SITE, SCP_CURRENT_SITE)
        }
    }
}
