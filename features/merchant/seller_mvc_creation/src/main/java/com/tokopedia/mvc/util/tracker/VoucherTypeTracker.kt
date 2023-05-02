package com.tokopedia.mvc.util.tracker

import com.tokopedia.mvc.util.constant.TrackerConstant
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class VoucherTypeTracker @Inject constructor(private val userSession: UserSessionInterface) {
    companion object {
        private const val ZERO: Long = 0
    }

    fun sendClickKuponTypeEvent(isVoucherProduct: Boolean, voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click jenis kupon")
            .setEventCategory(TrackerConstant.CreationVoucherType.event)
            .setEventLabel(isVoucherProduct.asEventLabel(voucherId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39392")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickLanjutEvent(voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click lanjut - first step")
            .setEventCategory(TrackerConstant.CreationVoucherType.event)
            .setEventLabel(voucherId.asEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39394")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickKembaliArrowEvent(voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click kembali arrow - first step")
            .setEventCategory(TrackerConstant.CreationVoucherType.event)
            .setEventLabel(voucherId.asEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39395")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    private fun Boolean.asEventLabel(voucherId: Long): String {
        val label = if (this) {
            if (voucherId == ZERO) {
                "voucher_step: create - voucher_id: - jenis_kupon: Kupon Produk"
            } else {
                "voucher_step: edit - voucher_id: $voucherId - jenis_kupon: Kupon Produk"
            }
        } else {
            if (voucherId == ZERO) {
                "voucher_step: create - voucher_id: - jenis_kupon: Kupon Toko"
            } else {
                "voucher_step: edit - voucher_id: $voucherId - jenis_kupon: Kupon Toko"
            }
        }
        return label
    }

    private fun Long.asEventLabel(): String {
        return if (this == ZERO) {
            "voucher_step: create - voucher_id: "
        } else {
            "voucher_step: edit - voucher_id: $this"
        }
    }
}
