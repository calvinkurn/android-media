package com.tokopedia.mvc.util.tracker

import com.tokopedia.mvc.util.constant.TrackerConstant
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ProductListPageTracker @Inject constructor(private val userSession: UserSessionInterface) {

    companion object {
        private const val ZERO : Long = 0
    }

    fun sendClickBackButtonEvent(voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click kembali arrow - fourth step")
            .setEventCategory("kupon toko saya - creation daftar produk")
            .setEventLabel(voucherId.asEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39419")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }


    fun sendButtonContinueClickEvent(voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click lanjut - fourth step")
            .setEventCategory("kupon toko saya - creation daftar produk")
            .setEventLabel(voucherId.asEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39420")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickToolbarBackButtonWithProductSelectedEvent(voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click kembali arrow - product selected - fourth step")
            .setEventCategory("kupon toko saya - creation daftar produk")
            .setEventLabel(voucherId.asEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39421")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    private fun Long.asEventLabel(): String {
        return if (this == ZERO) {
            "voucher_step: create - voucher_id: "
        } else {
            "voucher_step: edit - voucher_id: $this"
        }
    }
}
