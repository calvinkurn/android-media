package com.tokopedia.mvc.util.tracker

import com.tokopedia.mvc.util.constant.MvcTrackerConstant.MVC_BUSINESS_UNIT
import com.tokopedia.mvc.util.constant.MvcTrackerConstant.MVC_CURRENT_SITE
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
            .setBusinessUnit(MVC_BUSINESS_UNIT)
            .setCurrentSite(MVC_CURRENT_SITE)
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
            .setBusinessUnit(MVC_BUSINESS_UNIT)
            .setCurrentSite(MVC_CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }
}
