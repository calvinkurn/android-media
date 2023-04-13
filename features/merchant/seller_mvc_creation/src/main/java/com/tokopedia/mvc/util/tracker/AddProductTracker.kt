package com.tokopedia.mvc.util.tracker

import com.tokopedia.mvc.util.constant.TrackerConstant
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AddProductTracker @Inject constructor(private val userSession: UserSessionInterface) {
    companion object {
        private const val ZERO : Long = 0
    }

    fun sendClickButtonBackToPreviousPageEvent(voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click kembali button - third step")
            .setEventCategory("kupon toko saya - creation pengaturan kupon")
            .setEventLabel(voucherId.asEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39416")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }
    
    fun sendClickToolbarBackButtonEvent(voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click kembali arrow - third step")
            .setEventCategory("kupon toko saya - creation pengaturan kupon")
            .setEventLabel(voucherId.asEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39417")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }
    
    fun sendClickAddProductButtonEvent(voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click tambah produk")
            .setEventCategory("kupon toko saya - creation daftar produk")
            .setEventLabel(voucherId.asEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39418")
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
