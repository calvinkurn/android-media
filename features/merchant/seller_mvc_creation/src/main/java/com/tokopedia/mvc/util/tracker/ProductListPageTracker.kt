package com.tokopedia.mvc.util.tracker

import com.tokopedia.mvc.util.constant.TrackerConstant
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ProductListPageTracker @Inject constructor(private val userSession: UserSessionInterface) {

    //31
    fun sendClickToolbarBackButtonEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click kembali arrow - fourth step")
            .setEventCategory("kupon toko saya - creation daftar produk")
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39419")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }


    //32
    fun sendButtonContinueClickEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click lanjut - fourth step")
            .setEventCategory("kupon toko saya - creation daftar produk")
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39420")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    //33
    fun sendClickToolbarBackButtonWithSelectionEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click kembali arrow - product selected - fourth step")
            .setEventCategory("kupon toko saya - creation daftar produk")
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39421")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }
}
