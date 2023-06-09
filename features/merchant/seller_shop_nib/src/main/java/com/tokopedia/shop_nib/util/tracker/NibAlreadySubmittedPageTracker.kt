package com.tokopedia.shop_nib.util.tracker

import com.tokopedia.shop_nib.util.constant.TrackerConstant
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/3933
 */
class NibAlreadySubmittedPageTracker @Inject constructor(private val userSession: UserSessionInterface) {

    fun sendPageImpression() {
        Tracker.Builder()
            .setEvent("viewPGIris")
            .setEventAction("view nib already submit page")
            .setEventCategory("nib submission")
            .setEventLabel("")
            .setCustomProperty("trackerId", "43314")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }
}
