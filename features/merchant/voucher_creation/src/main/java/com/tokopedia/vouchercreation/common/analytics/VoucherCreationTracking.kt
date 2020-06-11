package com.tokopedia.vouchercreation.common.analytics

import com.tokopedia.track.TrackApp

object VoucherCreationTracking {

    fun sendOpenScreenTracking(screenName: String,
                               isLoggedIn: Boolean,
                               userId: String) {
        val map = mapOf(
                VoucherCreationAnalyticConstant.Event.OPEN_SCREEN to screenName,
                VoucherCreationAnalyticConstant.Key.IS_LOGGED_IN_STATUS to isLoggedIn,
                VoucherCreationAnalyticConstant.Key.CURRENT_SITE to VoucherCreationAnalyticConstant.Values.TOKOPEDIA_SELLER,
                VoucherCreationAnalyticConstant.Key.USER_ID to userId,
                VoucherCreationAnalyticConstant.Key.BUSINESS_UNIT to VoucherCreationAnalyticConstant.Values.PHYSICAL_GOODS
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

}