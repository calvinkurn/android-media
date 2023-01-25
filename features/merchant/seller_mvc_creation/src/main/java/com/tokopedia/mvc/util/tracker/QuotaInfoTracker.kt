package com.tokopedia.mvc.util.tracker

import com.tokopedia.mvc.util.constant.MvcTrackerConstant.BUSINESS_UNIT
import com.tokopedia.mvc.util.constant.MvcTrackerConstant.CURRENT_SITE
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class QuotaInfoTracker @Inject constructor(private val userSession: UserSessionInterface) {

    fun sendClickCloseEvent() {
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("click close")
            .setEventCategory("kupon toko saya - sumber kuota")
            .setEventLabel("")
            .setCustomProperty("trackerId", "39433")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickButtonUpgradeEvent(buttonName: String) {
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("click button upgrade")
            .setEventCategory("kupon toko saya - sumber kuota")
            .setEventLabel(buttonName)
            .setCustomProperty("trackerId", "39434")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }
}
