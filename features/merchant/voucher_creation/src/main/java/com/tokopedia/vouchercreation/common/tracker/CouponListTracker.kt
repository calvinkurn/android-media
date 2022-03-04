package com.tokopedia.vouchercreation.common.tracker

import com.tokopedia.track.TrackApp
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import javax.inject.Inject

class CouponListTracker{

    fun sendClickFilterImpression() {
        sendGeneralTracking(
            event = "clickPG",
            action = "click filter",
            category = "mvc locked product - list",
            label = ""
        )
    }

    private fun sendGeneralTracking(
        event: String,
        action: String,
        category: String,
        label: String,
        businessUnit: String = VoucherCreationAnalyticConstant.Values.PHYSICAL_GOODS,
        currentSite: String = VoucherCreationAnalyticConstant.Values.TOKOPEDIA_SELLER
    ) {
        val payload = mapOf(
            VoucherCreationAnalyticConstant.Key.EVENT to event,
            VoucherCreationAnalyticConstant.Key.EVENT_ACTION to action,
            VoucherCreationAnalyticConstant.Key.EVENT_CATEGORY to category,
            VoucherCreationAnalyticConstant.Key.EVENT_LABEL to label,
            VoucherCreationAnalyticConstant.Key.BUSINESS_UNIT to businessUnit,
            VoucherCreationAnalyticConstant.Key.CURRENT_SITE to currentSite
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(payload)
    }
}