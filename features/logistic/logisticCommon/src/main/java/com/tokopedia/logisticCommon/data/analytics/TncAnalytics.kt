package com.tokopedia.logisticCommon.data.analytics

import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object TncAnalytics : BaseTrackerConst() {
    private const val BUSINESS_UNIT_LOGISTIC_FULFILLMENT = "logistics & fulfillment"
    private const val BUSINESS_UNIT_LOGISTIC = "logistic"
    private const val CLICK_ADDRESS = "clickAddress"
    private const val CLICK_LOGISTIC = "clickLogistic"
    private const val CLICK_TNC = "click syarat dan ketentuan"
    private const val VIEW_ADDRESS = "viewAddressIris"
    private const val VIEW_LOGISTIC = "viewLogisticIris"
    private const val VIEW_TNC = "view syarat dan ketentuan"
    private const val CATEGORY_EDIT_ADDRESS = "edit address page"

    fun onClickTnC(userId: String, category: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(if (category == CATEGORY_EDIT_ADDRESS) CLICK_LOGISTIC else CLICK_ADDRESS)
                .appendEventCategory(category)
                .appendEventAction(CLICK_TNC)
                .appendEventLabel("")
                .appendUserId(userId)
                .appendBusinessUnit(if (category == CATEGORY_EDIT_ADDRESS) BUSINESS_UNIT_LOGISTIC else BUSINESS_UNIT_LOGISTIC_FULFILLMENT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .build()
        )
    }

    fun onViewTnC(userId: String, category: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(if (category == CATEGORY_EDIT_ADDRESS) VIEW_LOGISTIC else VIEW_ADDRESS)
                .appendEventCategory(category)
                .appendEventAction(VIEW_TNC)
                .appendEventLabel("")
                .appendUserId(userId)
                .appendBusinessUnit(if (category == CATEGORY_EDIT_ADDRESS) BUSINESS_UNIT_LOGISTIC else BUSINESS_UNIT_LOGISTIC_FULFILLMENT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .build()
        )
    }
}
