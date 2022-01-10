package com.tokopedia.logisticCommon.ui.userconsent.analytic

import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object TncAnalytics : BaseTrackerConst() {
    private const val BUSINESS_UNIT_LOGISTIC = "logistics & fulfillment"
    private const val CLICK_ADDRESS = "clickAddress"
    private const val CLICK_TNC = "click syarat dan ketentuan"
    private const val VIEW_ADDRESS = "viewAddressIris"
    private const val VIEW_TNC = "view syarat dan ketentuan"

    fun onClickTnC(userId: String, category: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(category)
                .appendEventAction(CLICK_TNC)
                .appendEventLabel("")
                .appendUserId(userId)
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .build())
    }

    fun onViewTnC(userId: String, category: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(VIEW_ADDRESS)
                .appendEventCategory(category)
                .appendEventAction(VIEW_TNC)
                .appendEventLabel("")
                .appendUserId(userId)
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .build())
    }
}