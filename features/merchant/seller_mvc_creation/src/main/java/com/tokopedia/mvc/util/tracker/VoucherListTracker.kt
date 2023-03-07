package com.tokopedia.mvc.util.tracker

import com.tokopedia.mvc.util.constant.TrackerConstant.EVENT
import com.tokopedia.mvc.util.constant.TrackerConstant.TRACKER_ID
import com.tokopedia.mvc.util.constant.TrackerConstant.BUSINESS_UNIT
import com.tokopedia.mvc.util.constant.TrackerConstant.CURRENT_SITE
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class VoucherListTracker @Inject constructor(private val userSession: UserSessionInterface) {
    companion object {
        private const val EC_VOUCHER_LIST_PAGE = "kupon toko saya - daftar kupon"
    }

    fun sendClickBuatKuponEvent() {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click buat kupon")
            .setEventCategory(EC_VOUCHER_LIST_PAGE)
            .setEventLabel("kupon aktif")
            .setCustomProperty(TRACKER_ID, "39428")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickArrowOnJadwalLainEvent() {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click arrow on jadwal lain")
            .setEventCategory(EC_VOUCHER_LIST_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, "39429")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickDotsOnUpperSideEvent() {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click 3 dots on upper side")
            .setEventCategory(EC_VOUCHER_LIST_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, "39430")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickDotsOnEachVoucherEvent() {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click 3 dots on each voucher")
            .setEventCategory(EC_VOUCHER_LIST_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, "39431")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickInfoOnSisaKuotaEvent() {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click info on sisa kuota")
            .setEventCategory(EC_VOUCHER_LIST_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, "39432")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }
}
