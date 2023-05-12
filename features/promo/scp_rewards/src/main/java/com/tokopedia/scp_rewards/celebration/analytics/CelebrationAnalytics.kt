package com.tokopedia.scp_rewards.celebration.analytics

import com.tokopedia.scp_rewards.common.constants.TrackerConstants
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
            EVENT_ACTION to "view page",
            EVENT_CATEGORY to "goto medali - celebration",
            EVENT_LABEL to badgeId,
            TRACKER_ID to "43301",
            BUSINESS_UNIT to TrackerConstants.Business.BUSINESS_UNIT,
            CURRENT_SITE to TrackerConstants.Business.CURRENT_SITE
        )
        gtm.sendGeneralEvent(map)
    }
}
