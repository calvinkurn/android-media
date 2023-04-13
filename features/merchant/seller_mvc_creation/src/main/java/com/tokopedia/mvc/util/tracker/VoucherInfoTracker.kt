package com.tokopedia.mvc.util.tracker

import com.tokopedia.mvc.util.constant.TrackerConstant
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class VoucherInfoTracker @Inject constructor(private val userSession: UserSessionInterface) {

    companion object {
        private const val ZERO: Long = 0
    }

    fun sendClickTargetKuponEvent(isPublic: Boolean, voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click target kupon")
            .setEventCategory(TrackerConstant.CreationVoucherInfo.event)
            .setEventLabel(isPublic.asEventLabel(voucherId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39396")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickFieldNamaKuponEvent(voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click field nama kupon")
            .setEventCategory(TrackerConstant.CreationVoucherInfo.event)
            .setEventLabel(voucherId.asEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39398")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickFieldKodeKuponEvent(voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click field kode kupon")
            .setEventCategory(TrackerConstant.CreationVoucherInfo.event)
            .setEventLabel(voucherId.asEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39399")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickFieldDatePickerMulaiEvent(voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click datepicker mulai - pilih")
            .setEventCategory(TrackerConstant.RecurringVoucher.event)
            .setEventLabel(voucherId.asEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39400")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickFieldDatePickerBerakhirEvent(voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click datepicker berakhir - pilih")
            .setEventCategory(TrackerConstant.RecurringVoucher.event)
            .setEventLabel(voucherId.asEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39401")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickCheckBoxEvent(voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click chechbox")
            .setEventCategory(TrackerConstant.RecurringVoucher.event)
            .setEventLabel(voucherId.asEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39402")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickDropdownEvent(eventLabel: String, voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click dropdown ulangi selama")
            .setEventCategory(TrackerConstant.RecurringVoucher.event)
            .setEventLabel(eventLabel.asEventLabel(voucherId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39403")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickLanjutEvent(voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click lanjut - second step")
            .setEventCategory(TrackerConstant.CreationVoucherInfo.event)
            .setEventLabel(voucherId.asEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39404")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickKembaliButtonEvent(voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click kembali button - second step")
            .setEventCategory(TrackerConstant.CreationVoucherInfo.event)
            .setEventLabel(voucherId.asEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39405")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickKembaliArrowEvent(voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click kembali arrow - second step")
            .setEventCategory(TrackerConstant.CreationVoucherInfo.event)
            .setEventLabel(voucherId.asEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39406")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    private fun Boolean.asEventLabel(voucherId: Long): String {
        val label = if (this) {
            if (voucherId == ZERO) {
                "voucher_step: create - voucher_id: - target_kupon: Publik"
            } else {
                "voucher_step: edit - voucher_id: $voucherId - target_kupon: Publik"
            }
        } else {
            if (voucherId == ZERO) {
                "voucher_step: create - voucher_id: - target_kupon: Khusus"
            } else {
                "voucher_step: edit - voucher_id: $voucherId - target_kupon: Khusus"
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

    private fun String.asEventLabel(voucherId: Long): String {
        return if (voucherId == ZERO) {
            "voucher_step: create - voucher_id: - ulangi_selama: $this"
        } else {
            "voucher_step: edit - voucher_id: $voucherId - ulangi_selama: $this"
        }
    }
}
