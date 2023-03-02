package com.tokopedia.mvc.util.tracker

import com.tokopedia.mvc.util.constant.TrackerConstant
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class VoucherTypeTracker @Inject constructor(private val userSession: UserSessionInterface) {

    fun sendClickKuponTypeEvent(isVoucherProduct: Boolean) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click jenis kupon")
            .setEventCategory(TrackerConstant.CreationVoucherType.event)
            .setEventLabel(isVoucherProduct.asEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39392")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickLanjutEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click lanjut - first step")
            .setEventCategory(TrackerConstant.CreationVoucherType.event)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39394")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickKembaliArrowEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click kembali arrow - first step")
            .setEventCategory(TrackerConstant.CreationVoucherType.event)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39395")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    private fun Boolean.asEventLabel(): String {
        val label = if (this) {
            "Kupon Produk"
        } else {
            "Kupon Toko"
        }
        return label
    }
}
