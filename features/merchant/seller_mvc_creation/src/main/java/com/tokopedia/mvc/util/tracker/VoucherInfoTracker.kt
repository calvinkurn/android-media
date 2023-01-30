package com.tokopedia.mvc.util.tracker

import com.tokopedia.mvc.util.constant.TrackerConstant
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class VoucherInfoTracker @Inject constructor(private val userSession: UserSessionInterface) {

    fun sendClickTargetKuponEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click target kupon")
            .setEventCategory(TrackerConstant.CreationVoucherInfo.event)
            .setEventLabel(eventLabel)
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39396")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickFieldKodeKuponEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click field kode kupon")
            .setEventCategory(TrackerConstant.CreationVoucherInfo.event)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39399")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickFieldDatePickerMulaiEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click datepicker mulai - pilih")
            .setEventCategory(TrackerConstant.RecurringVoucher.event)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39400")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickFieldDatePickerBerakhirEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click datepicker berakhir - pilih")
            .setEventCategory(TrackerConstant.RecurringVoucher.event)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39401")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickCheckBoxEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click chechbox")
            .setEventCategory(TrackerConstant.RecurringVoucher.event)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39402")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickDropdownEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click dropdown ulangi selama")
            .setEventCategory(TrackerConstant.RecurringVoucher.event)
            .setEventLabel(eventLabel)
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39403")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickLanjutEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click lanjut - second step")
            .setEventCategory(TrackerConstant.CreationVoucherInfo.event)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39404")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickKembaliButtonEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click kembali button - second step")
            .setEventCategory(TrackerConstant.CreationVoucherInfo.event)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39405")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickKembaliArrowEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click kembali arrow - second step")
            .setEventCategory(TrackerConstant.CreationVoucherInfo.event)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39406")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }
}
