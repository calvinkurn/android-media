package com.tokopedia.mvc.util.tracker

import com.tokopedia.mvc.util.constant.TrackerConstant
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class DownloadVoucherImageTracker @Inject constructor(private val userSession: UserSessionInterface) {


    fun sendClickCheckboxEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click checkbox")
            .setEventCategory("kupon toko saya - detail kupon - download kupon")
            .setEventLabel(eventLabel)
            .setCustomProperty(TrackerConstant.TRACKER_ID, "40646")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }


    fun sendClickDownloadPopUpUkuranKuponEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click download - pop up ukuran kupon")
            .setEventCategory("kupon toko saya - detail kupon - download kupon")
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, "40647")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }


    fun sendClickCancelEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click cancel")
            .setEventCategory("kupon toko saya - detail kupon - download kupon")
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, "40648")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }
}
