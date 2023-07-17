package com.tokopedia.scp_rewards_touchpoints.touchpoints.analytics

import com.tokopedia.scp_rewards_touchpoints.common.CLICK_EVENT
import com.tokopedia.scp_rewards_touchpoints.common.EVENT_ACTION_CLICK_TOASTER
import com.tokopedia.scp_rewards_touchpoints.common.EVENT_ACTION_VIEW_TOASTER
import com.tokopedia.scp_rewards_touchpoints.common.EVENT_CATEGORY_MEDALI_CELEBRATION_TOASTER
import com.tokopedia.scp_rewards_touchpoints.common.SCP_BUSINESS_UNIT
import com.tokopedia.scp_rewards_touchpoints.common.SCP_CURRENT_SITE
import com.tokopedia.scp_rewards_touchpoints.common.TRACKER_ID_CLICK_TOASTER
import com.tokopedia.scp_rewards_touchpoints.common.TRACKER_ID_VIEW_TOASTER
import com.tokopedia.scp_rewards_touchpoints.common.VIEW_EVENT
import com.tokopedia.track.builder.Tracker
import com.tokopedia.track.constant.TrackerConstant.TRACKER_ID


/**
 * SCP Rewards Toaster Tracker
 * https://mynakama.tokopedia.com/datatracker/requestdetail/view/4023
 */

object ScpRewardsToasterAnalytics {

    // Tracker ID: 44704
    fun sendViewToasterEvent (badgeId: String) {
        Tracker.Builder()
            .setEvent(VIEW_EVENT)
            .setEventAction(EVENT_ACTION_VIEW_TOASTER)
            .setEventCategory(EVENT_CATEGORY_MEDALI_CELEBRATION_TOASTER)
            .setEventLabel(badgeId)
            .setCustomProperty(TRACKER_ID, TRACKER_ID_VIEW_TOASTER)
            .setBusinessUnit(SCP_BUSINESS_UNIT)
            .setCurrentSite(SCP_CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker ID: 44705
    fun sendClickToasterEvent(badgeId: String) {
        Tracker.Builder()
            .setEvent(CLICK_EVENT)
            .setEventAction(EVENT_ACTION_CLICK_TOASTER)
            .setEventCategory(EVENT_CATEGORY_MEDALI_CELEBRATION_TOASTER)
            .setEventLabel(badgeId)
            .setCustomProperty(TRACKER_ID, TRACKER_ID_CLICK_TOASTER)
            .setBusinessUnit(SCP_BUSINESS_UNIT)
            .setCurrentSite(SCP_CURRENT_SITE)
            .build()
            .send()
    }
}
