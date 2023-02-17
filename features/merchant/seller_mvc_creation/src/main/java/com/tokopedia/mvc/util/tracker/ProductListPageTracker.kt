package com.tokopedia.mvc.util.tracker

import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.util.constant.TrackerConstant
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ProductListPageTracker @Inject constructor(private val userSession: UserSessionInterface) {

    fun sendClickBackButtonEvent(pageMode: PageMode, voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click kembali arrow - fourth step")
            .setEventCategory("kupon toko saya - creation daftar produk")
            .setEventLabel(pageMode.asEventLabel(voucherId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39419")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }


    fun sendButtonContinueClickEvent(pageMode: PageMode, voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click lanjut - fourth step")
            .setEventCategory("kupon toko saya - creation daftar produk")
            .setEventLabel(pageMode.asEventLabel(voucherId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39420")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickToolbarBackButtonWithProductSelectedEvent(pageMode: PageMode, voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click kembali arrow - product selected - fourth step")
            .setEventCategory("kupon toko saya - creation daftar produk")
            .setEventLabel(pageMode.asEventLabel(voucherId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39421")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    private fun PageMode.asEventLabel(voucherId: Long): String {
        return if (this == PageMode.CREATE || this == PageMode.DUPLICATE) {
            "voucher_step: create - voucher_id: "
        } else {
            "voucher_step: edit - voucher_id: $voucherId"
        }
    }
}
