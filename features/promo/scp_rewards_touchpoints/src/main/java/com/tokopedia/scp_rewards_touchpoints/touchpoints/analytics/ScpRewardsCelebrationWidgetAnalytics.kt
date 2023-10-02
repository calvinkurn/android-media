package com.tokopedia.scp_rewards_touchpoints.touchpoints.analytics

import com.tokopedia.scp_rewards_touchpoints.common.BADGE_ID
import com.tokopedia.scp_rewards_touchpoints.common.CLICK_EVENT
import com.tokopedia.scp_rewards_touchpoints.common.EVENT_ACTION_CLICK_CELEBRATION_WIDGET
import com.tokopedia.scp_rewards_touchpoints.common.EVENT_ACTION_VIEW_CELEBRATION_WIDGET
import com.tokopedia.scp_rewards_touchpoints.common.EVENT_CATEGORY_MEDALI_CELEBRATION_WIDGET
import com.tokopedia.scp_rewards_touchpoints.common.ORDER_ID
import com.tokopedia.scp_rewards_touchpoints.common.PAGE_PATH
import com.tokopedia.scp_rewards_touchpoints.common.PAGE_TYPE
import com.tokopedia.scp_rewards_touchpoints.common.SCP_BUSINESS_UNIT
import com.tokopedia.scp_rewards_touchpoints.common.SCP_CURRENT_SITE
import com.tokopedia.scp_rewards_touchpoints.common.TRACKER_ID_CLICK_CELEBRATION_WIDGET
import com.tokopedia.scp_rewards_touchpoints.common.TRACKER_ID_VIEW_CELEBRATION_WIDGET
import com.tokopedia.scp_rewards_touchpoints.common.VIEW_EVENT
import com.tokopedia.track.builder.Tracker
import com.tokopedia.track.constant.TrackerConstant
import org.json.JSONObject

/**
 * SCP Rewards Toaster Tracker
 * https://mynakama.tokopedia.com/datatracker/requestdetail/view/4109
 */

object ScpRewardsCelebrationWidgetAnalytics {

    // Tracker ID: 45561
    fun impressionCelebrationWidget(badgeId: String, orderId: String, pagePath: String = "", pageType: String = "") {
        val label = JSONObject().apply {
            put(BADGE_ID, badgeId)
            put(ORDER_ID, orderId)
            put(PAGE_PATH, pagePath)
            put(PAGE_TYPE, pageType)
        }
        Tracker.Builder()
            .setEvent(VIEW_EVENT)
            .setEventAction(EVENT_ACTION_VIEW_CELEBRATION_WIDGET)
            .setEventCategory(EVENT_CATEGORY_MEDALI_CELEBRATION_WIDGET)
            .setEventLabel(label.toString())
            .setCustomProperty(TrackerConstant.TRACKER_ID, TRACKER_ID_VIEW_CELEBRATION_WIDGET)
            .setBusinessUnit(SCP_BUSINESS_UNIT)
            .setCurrentSite(SCP_CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker ID: 45562
    fun clickCelebrationWidget(badgeId: String, orderId: String, pagePath: String = "", pageType: String = "") {
        val label = JSONObject().apply {
            put(BADGE_ID, badgeId)
            put(ORDER_ID, orderId)
            put(PAGE_PATH, pagePath)
            put(PAGE_TYPE, pageType)
        }
        Tracker.Builder()
            .setEvent(CLICK_EVENT)
            .setEventAction(EVENT_ACTION_CLICK_CELEBRATION_WIDGET)
            .setEventCategory(EVENT_CATEGORY_MEDALI_CELEBRATION_WIDGET)
            .setEventLabel(label.toString())
            .setCustomProperty(TrackerConstant.TRACKER_ID, TRACKER_ID_CLICK_CELEBRATION_WIDGET)
            .setBusinessUnit(SCP_BUSINESS_UNIT)
            .setCurrentSite(SCP_CURRENT_SITE)
            .build()
            .send()
    }
}
