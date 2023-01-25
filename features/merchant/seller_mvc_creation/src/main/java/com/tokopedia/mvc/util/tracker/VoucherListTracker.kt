package com.tokopedia.mvc.util.tracker

import com.tokopedia.mvc.util.constant.MvcTrackerConstant.MVC_BUSINESS_UNIT
import com.tokopedia.mvc.util.constant.MvcTrackerConstant.MVC_CURRENT_SITE
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class VoucherListTracker @Inject constructor(private val userSession: UserSessionInterface) {

    fun sendClickBuatKuponEvent() {
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("click buat kupon")
            .setEventCategory("kupon toko saya - daftar kupon")
            .setEventLabel("kupon aktif")
            .setCustomProperty("trackerId", "39428")
            .setBusinessUnit(MVC_BUSINESS_UNIT)
            .setCurrentSite(MVC_CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickArrowOnJadwalLainEvent() {
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("click arrow on jadwal lain")
            .setEventCategory("kupon toko saya - daftar kupon")
            .setEventLabel("")
            .setCustomProperty("trackerId", "39429")
            .setBusinessUnit(MVC_BUSINESS_UNIT)
            .setCurrentSite(MVC_CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickDotsOnUpperSideEvent() {
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("click 3 dots on upper side")
            .setEventCategory("kupon toko saya - daftar kupon")
            .setEventLabel("")
            .setCustomProperty("trackerId", "39430")
            .setBusinessUnit(MVC_BUSINESS_UNIT)
            .setCurrentSite(MVC_CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickDotsOnEachVoucherEvent() {
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("click 3 dots on each voucher")
            .setEventCategory("kupon toko saya - daftar kupon")
            .setEventLabel("")
            .setCustomProperty("trackerId", "39431")
            .setBusinessUnit(MVC_BUSINESS_UNIT)
            .setCurrentSite(MVC_CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickInfoOnSisaKuotaEvent() {
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("click info on sisa kuota")
            .setEventCategory("kupon toko saya - daftar kupon")
            .setEventLabel("")
            .setCustomProperty("trackerId", "39432")
            .setBusinessUnit(MVC_BUSINESS_UNIT)
            .setCurrentSite(MVC_CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }
}
