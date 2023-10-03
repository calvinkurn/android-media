package com.tokopedia.common.topupbills.analytics

import android.os.Bundle
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.common_digital.common.constant.DigitalTrackingConst
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class CommonMultiCheckoutAnalytics {

    fun onCloseMultiCheckoutCoachmark(categoryName: String, loyaltyStatus: String) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, DigitalTrackingConst.Action.CLOSE_COACHMARK)
            putString(
                TrackAppUtils.EVENT_LABEL,
                String.format(
                    "%s_%s",
                    categoryName,
                    loyaltyStatus
                )
            )
            putString(TrackAppUtils.EVENT, DigitalTrackingConst.Event.CLICK_DIGITAL)
            putString(TrackAppUtils.EVENT_CATEGORY, RechargeAnalytics.DIGITAL_HOMEPAGE)
            putString(
                DigitalTrackingConst.Other.KEY_TRACKER_ID,
                DigitalTrackingConst.Id.CLOSE_COACHMARK_ID
            )
            putString(DigitalTrackingConst.Label.BUSINESS_UNIT, DigitalTrackingConst.Value.RECHARGE_BU)
            putString(DigitalTrackingConst.Label.CURRENTSITE, DigitalTrackingConst.Value.RECHARGE_SITE)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DigitalTrackingConst.Event.CLICK_DIGITAL, eventDataLayer)
    }
}
