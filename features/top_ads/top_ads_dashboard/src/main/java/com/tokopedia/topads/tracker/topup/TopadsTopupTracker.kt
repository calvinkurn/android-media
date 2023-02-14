package com.tokopedia.topads.tracker.topup

import com.tokopedia.topads.tracker.topup.TopadsToupTrackerConstants.DEFAULT_BUSINESS_UNIT
import com.tokopedia.topads.tracker.topup.TopadsToupTrackerConstants.DEFAULT_CURRENT_SITE
import com.tokopedia.topads.tracker.topup.ToupTrackerEventCategory.TOP_ADS_HOME_PAGE_DASHBOARD
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.track.builder.util.BaseTrackerConst

object TopadsTopupTracker : BaseTrackerConst() {

    fun clickCobaSekarang() {
        sendClickEventFromCreditHistoryPage(
            eventAction = ToupTrackerEventAction.CLICK_COBA_SERANG_AUTO_TOPUP,
            trackerId = "31839"
        )
    }

    fun clickTambahKreditTopup(topUpAmount: String) {
        sendClickEventFromCreditHistoryPage(
            eventAction = ToupTrackerEventAction.CLICK_TOPUP,
            trackerId = "31840",
            eventLabel = topUpAmount
        )
    }

    fun clickDateRange() {
        sendClickEventFromCreditHistoryPage(
            eventAction = ToupTrackerEventAction.CLICK_DATE_RANGE,
            trackerId = "31841"
        )
    }

    fun clickHariIni() {
        sendClickEventFromCreditHistoryPage(
            eventAction = ToupTrackerEventAction.CLICK_HARI_INI,
            trackerId = "31842"
        )
    }

    fun click7HariTerakhir() {
        sendClickEventFromCreditHistoryPage(
            eventAction = ToupTrackerEventAction.CLICK_7_HARI_KEMARIN,
            trackerId = "31843"
        )
    }

    fun click30HariTerakhir() {
        sendClickEventFromCreditHistoryPage(
            eventAction = ToupTrackerEventAction.CLICK_30_HARI_KEMARIN,
            trackerId = "31844"
        )
    }

    fun clickBulanIni() {
        sendClickEventFromCreditHistoryPage(
            eventAction = ToupTrackerEventAction.CLICK_BULAN_INI,
            trackerId = "31845"
        )
    }

    fun clickCustom() {
        sendClickEventFromCreditHistoryPage(
            eventAction = ToupTrackerEventAction.CLICK_CUSTOM_DATE_RANGE,
            trackerId = "31846"
        )
    }

    fun clickTambahKreditOtomatis() {
        sendClickEventFromCreditHistoryPage(
            eventAction = ToupTrackerEventAction.CLICK_TAMBAH_KREDIT_OTOMATIS,
            trackerId = "31847"
        )
    }

    fun clickTambahKreditHistoryPage() {
        sendClickEventFromCreditHistoryPage(
            eventAction = ToupTrackerEventAction.CLICK_TAMBAH_KREDIT,
            trackerId = "31848"
        )
    }

    fun clickToggleOnOff(on: Boolean) {
        sendClickEventFromCreditHistoryPage(
            eventAction = ToupTrackerEventAction.CLICK_TOGGLE_AUTO_TOPUP,
            trackerId = "31851",
            eventLabel = if (on) TopadsToupTrackerConstants.ACTIVATE else TopadsToupTrackerConstants.DEACTIVATE
        )
    }

    fun clickBatalkan() {
        sendClickEventFromCreditHistoryPage(
            eventAction = ToupTrackerEventAction.CLICK_BATALKAN_AUTO_TOPUP,
            trackerId = "31852"
        )
    }

    fun clickAktifkan(topUpAmount: String) {
        sendClickEventFromCreditHistoryPage(
            eventAction = ToupTrackerEventAction.CLICK_YA_AKTIFKAN_AUTO_TOPUP,
            trackerId = "31853",
            eventLabel = topUpAmount
        )
    }

    fun clickThresholdCredit() {
        sendClickEventFromCreditHistoryPage(
            eventAction = ToupTrackerEventAction.CLICK_THRESHOLD_AUTO_TOPUP,
            trackerId = "31854"
        )
    }

    fun clickSaldoDropdownList() {
        sendClickEventFromCreditHistoryPage(
            eventAction = ToupTrackerEventAction.CLICK_SALDO_DROPDOWN_LIST,
            trackerId = "31855"
        )
    }

    fun clickSimpan(topUpAmount: String) {
        sendClickEventFromCreditHistoryPage(
            eventAction = ToupTrackerEventAction.CLICK_SIMPAN,
            trackerId = "31856",
            eventLabel = topUpAmount
        )
    }

    fun clickTetapGunakan() {
        sendClickEventFromCreditHistoryPage(
            eventAction = ToupTrackerEventAction.CLICK_CANCEL_DEACTIVATION_AUTO_TOPUP,
            trackerId = "31857"
        )
    }

    fun clickYaNonaktifkan() {
        sendClickEventFromCreditHistoryPage(
            eventAction = ToupTrackerEventAction.CLICK_DEACTIVATION_AUTO_TOPUP,
            trackerId = "31858"
        )
    }

    fun clickIncreaseQuantity() {
        sendClickEventFromTaCart(
            eventAction = ToupTrackerEventAction.CLICK_ADD_CHOICE,
            trackerId = "31864"
        )
    }

    fun clickDecreaseQuantity() {
        sendClickEventFromTaCart(
            eventAction = ToupTrackerEventAction.CLICK_REMOVE_CHOICE,
            trackerId = "31865"
        )
    }

    fun clickCekPromo() {
        sendClickEventFromTaCart(
            eventAction = ToupTrackerEventAction.CLICK_CEK_PROMO,
            trackerId = "31866"
        )
    }

    fun clickPilihMetodePembayaran() {
        sendClickEventFromTaCart(
            eventAction = ToupTrackerEventAction.CLICK_METODE_PEMBAYARAN,
            trackerId = "31867"
        )
    }

    fun clickBayar() {
        sendAnalytics(
            eventAction = ToupTrackerEventAction.CLICK_BAYAR,
            trackerId = "31868",
            eventCategory = ToupTrackerEventCategory.PAYMENT_PAGE,
            eventLabel = "TODO"
        )
    }

    fun clickKemarin() {
        sendClickEventFromCreditHistoryPage(
            eventAction = ToupTrackerEventAction.CLICK_KEMARIN,
            trackerId = "31989"
        )
    }

    private fun sendClickEventFromTaCart(
        trackerId: String,
        eventAction: String,
        eventLabel: String = ""
    ) {
        sendAnalytics(
            eventCategory = ToupTrackerEventCategory.TA_CART,
            eventLabel = eventLabel,
            eventAction = eventAction,
            trackerId = trackerId
        )
    }

    private fun sendClickEventFromCreditHistoryPage(
        trackerId: String,
        eventAction: String,
        eventLabel: String = ""
    ) {
        sendAnalytics(
            eventCategory = ToupTrackerEventCategory.TOP_ADS_CREDIT_HISTORY_PAGE,
            eventLabel = eventLabel,
            eventAction = eventAction,
            trackerId = trackerId
        )
    }

    private fun sendAnalytics(
        event: String = TopupTrackerEvent.CLICK_TOP_ADS,
        eventAction: String,
        eventCategory: String,
        eventLabel: String = "",
        trackerId: String,
        businessUnit: String = DEFAULT_BUSINESS_UNIT,
        currentSite: String = DEFAULT_CURRENT_SITE
    ) {
        val analyticsBundle = mapOf(
            Event.KEY to event,
            Action.KEY to eventAction,
            Category.KEY to eventCategory,
            Label.KEY to eventLabel,
            TrackerId.KEY to trackerId,
            BusinessUnit.KEY to businessUnit,
            CurrentSite.KEY to currentSite
        )
        TrackApp.getInstance().gtm.apply {
            sendGeneralEvent(analyticsBundle)
        }
    }

    fun sendClickTambahKreditEvent() {
        Tracker.Builder()
            .setEvent(TopupTrackerEvent.CLICK_TOP_ADS)
            .setEventAction("click - tambah kredit")
            .setEventCategory(TOP_ADS_HOME_PAGE_DASHBOARD)
            .setEventLabel("")
            .setCustomProperty(TrackerId.KEY, "40587")
            .setBusinessUnit(DEFAULT_BUSINESS_UNIT)
            .setCurrentSite(DEFAULT_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickCobaSekarangKreditOtomatisEvent() {
        Tracker.Builder()
            .setEvent(TopupTrackerEvent.CLICK_TOP_ADS)
            .setEventAction("click - coba sekarang kredit otomatis")
            .setEventCategory(ToupTrackerEventCategory.TOP_ADS_CREDIT_HISTORY_PAGE)
            .setEventLabel("")
            .setCustomProperty(TrackerId.KEY, "40588")
            .setBusinessUnit(DEFAULT_BUSINESS_UNIT)
            .setCurrentSite(DEFAULT_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickTambahKreditManualEvent() {
        Tracker.Builder()
            .setEvent(TopupTrackerEvent.CLICK_TOP_ADS)
            .setEventAction("click - tambah kredit manual")
            .setEventCategory(ToupTrackerEventCategory.TOP_ADS_CREDIT_HISTORY_PAGE)
            .setEventLabel("")
            .setCustomProperty(TrackerId.KEY, "40589")
            .setBusinessUnit(DEFAULT_BUSINESS_UNIT)
            .setCurrentSite(DEFAULT_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickPelajariSelengkapnyaEvent() {
        Tracker.Builder()
            .setEvent(TopupTrackerEvent.CLICK_TOP_ADS)
            .setEventAction("click - pelajari selengkapnya")
            .setEventCategory(ToupTrackerEventCategory.TOP_ADS_CREDIT_HISTORY_PAGE)
            .setEventLabel("")
            .setCustomProperty(TrackerId.KEY, "40590")
            .setBusinessUnit(DEFAULT_BUSINESS_UNIT)
            .setCurrentSite(DEFAULT_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickPilihTambahKreditManualEvent() {
        Tracker.Builder()
            .setEvent(TopupTrackerEvent.CLICK_TOP_ADS)
            .setEventAction("click - pilih tambah kredit manual")
            .setEventCategory(ToupTrackerEventCategory.TOP_ADS_CREDIT_HISTORY_PAGE)
            .setEventLabel("")
            .setCustomProperty(TrackerId.KEY, "40591")
            .setBusinessUnit(DEFAULT_BUSINESS_UNIT)
            .setCurrentSite(DEFAULT_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickPilihTambahKreditOtomatisEvent() {
        Tracker.Builder()
            .setEvent(TopupTrackerEvent.CLICK_TOP_ADS)
            .setEventAction("click - pilih tambah kredit otomatis")
            .setEventCategory(ToupTrackerEventCategory.TOP_ADS_CREDIT_HISTORY_PAGE)
            .setEventLabel("")
            .setCustomProperty(TrackerId.KEY, "40592")
            .setBusinessUnit(DEFAULT_BUSINESS_UNIT)
            .setCurrentSite(DEFAULT_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickInformasiKreditOtomatisEvent() {
        Tracker.Builder()
            .setEvent(TopupTrackerEvent.CLICK_TOP_ADS)
            .setEventAction("click - informasi kredit otomatis")
            .setEventCategory(ToupTrackerEventCategory.TOP_ADS_CREDIT_HISTORY_PAGE)
            .setEventLabel("")
            .setCustomProperty(TrackerId.KEY, "40593")
            .setBusinessUnit(DEFAULT_BUSINESS_UNIT)
            .setCurrentSite(DEFAULT_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickAmountOfManualTopupDiModalTambahKreditEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TopupTrackerEvent.CLICK_TOP_ADS)
            .setEventAction("click - amount of manual topup di modal tambah kredit")
            .setEventCategory(ToupTrackerEventCategory.TOP_ADS_CREDIT_HISTORY_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(TrackerId.KEY, "40594")
            .setBusinessUnit(DEFAULT_BUSINESS_UNIT)
            .setCurrentSite(DEFAULT_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickAktifkanTambahKreditManualDiModalTambahKreditEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TopupTrackerEvent.CLICK_TOP_ADS)
            .setEventAction("click - aktifkan tambah kredit manual di modal tambah kredit")
            .setEventCategory(ToupTrackerEventCategory.TOP_ADS_CREDIT_HISTORY_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(TrackerId.KEY, "40595")
            .setBusinessUnit(DEFAULT_BUSINESS_UNIT)
            .setCurrentSite(DEFAULT_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickFrekuensiKreditOtomatisDiModalTambahKreditEvent(
        isFromEditAutoTopUp: Boolean,
        eventLabel: String
    ) {
        Tracker.Builder()
            .setEvent(TopupTrackerEvent.CLICK_TOP_ADS)
            .setEventAction(if (isFromEditAutoTopUp) "click - frekuensi kredit otomatis" else "click - frekuensi kredit otomatis di modal tambah kredit")
            .setEventCategory(ToupTrackerEventCategory.TOP_ADS_CREDIT_HISTORY_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(TrackerId.KEY, "40596")
            .setBusinessUnit(DEFAULT_BUSINESS_UNIT)
            .setCurrentSite(DEFAULT_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickChecklistTncKreditOtomatisDiModalTambahKreditEvent(isShowEditHistory: Boolean) {
        Tracker.Builder()
            .setEvent(TopupTrackerEvent.CLICK_TOP_ADS)
            .setEventAction(if (isShowEditHistory) "click - checklist tnc kredit otomatis" else "click - checklist tnc kredit otomatis di modal tambah kredit")
            .setEventCategory(ToupTrackerEventCategory.TOP_ADS_CREDIT_HISTORY_PAGE)
            .setEventLabel("")
            .setCustomProperty(TrackerId.KEY, "40597")
            .setBusinessUnit(DEFAULT_BUSINESS_UNIT)
            .setCurrentSite(DEFAULT_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickAktifkanTambahKreditOtomatisDiModalTambahKreditEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TopupTrackerEvent.CLICK_TOP_ADS)
            .setEventAction("click - aktifkan tambah kredit otomatis di modal tambah kredit")
            .setEventCategory(ToupTrackerEventCategory.TOP_ADS_CREDIT_HISTORY_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(TrackerId.KEY, "40598")
            .setBusinessUnit(DEFAULT_BUSINESS_UNIT)
            .setCurrentSite(DEFAULT_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickUbahPengaturanKreditOtomatisDiModalTambahKreditEvent() {
        Tracker.Builder()
            .setEvent(TopupTrackerEvent.CLICK_TOP_ADS)
            .setEventAction("click - ubah pengaturan kredit otomatis di modal tambah kredit")
            .setEventCategory(ToupTrackerEventCategory.TOP_ADS_CREDIT_HISTORY_PAGE)
            .setEventLabel("")
            .setCustomProperty(TrackerId.KEY, "40599")
            .setBusinessUnit(DEFAULT_BUSINESS_UNIT)
            .setCurrentSite(DEFAULT_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickInfoMinimalTresholdKreditOtomatisEvent() {
        Tracker.Builder()
            .setEvent(TopupTrackerEvent.CLICK_TOP_ADS)
            .setEventAction("click - info minimal treshold kredit otomatis")
            .setEventCategory(ToupTrackerEventCategory.TOP_ADS_CREDIT_HISTORY_PAGE)
            .setEventLabel("")
            .setCustomProperty(TrackerId.KEY, "40600")
            .setBusinessUnit(DEFAULT_BUSINESS_UNIT)
            .setCurrentSite(DEFAULT_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickInfoFrekuensiTambahKreditOtomatisEvent() {
        Tracker.Builder()
            .setEvent(TopupTrackerEvent.CLICK_TOP_ADS)
            .setEventAction("click - info frekuensi tambah kredit otomatis")
            .setEventCategory(ToupTrackerEventCategory.TOP_ADS_CREDIT_HISTORY_PAGE)
            .setEventLabel("")
            .setCustomProperty(TrackerId.KEY, "40601")
            .setBusinessUnit(DEFAULT_BUSINESS_UNIT)
            .setCurrentSite(DEFAULT_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickAmountOfAutoTopupEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TopupTrackerEvent.CLICK_TOP_ADS)
            .setEventAction("click - amount of auto topup")
            .setEventCategory(ToupTrackerEventCategory.TOP_ADS_CREDIT_HISTORY_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(TrackerId.KEY, "40602")
            .setBusinessUnit(DEFAULT_BUSINESS_UNIT)
            .setCurrentSite(DEFAULT_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickAktifkanTambahKreditOtomatisEvent() {
        Tracker.Builder()
            .setEvent(TopupTrackerEvent.CLICK_TOP_ADS)
            .setEventAction("click - aktifkan tambah kredit otomatis")
            .setEventCategory(ToupTrackerEventCategory.TOP_ADS_CREDIT_HISTORY_PAGE)
            .setEventLabel("")
            .setCustomProperty(TrackerId.KEY, "40605")
            .setBusinessUnit(DEFAULT_BUSINESS_UNIT)
            .setCurrentSite(DEFAULT_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickBatalPerubahanPengaturanKreditOtomatisEvent() {
        Tracker.Builder()
            .setEvent(TopupTrackerEvent.CLICK_TOP_ADS)
            .setEventAction("click - batal perubahan pengaturan kredit otomatis")
            .setEventCategory(ToupTrackerEventCategory.TOP_ADS_CREDIT_HISTORY_PAGE)
            .setEventLabel("")
            .setCustomProperty(TrackerId.KEY, "40606")
            .setBusinessUnit(DEFAULT_BUSINESS_UNIT)
            .setCurrentSite(DEFAULT_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickClosePopupKreditOtomatisEvent() {
        Tracker.Builder()
            .setEvent(TopupTrackerEvent.CLICK_TOP_ADS)
            .setEventAction("click - close popup kredit otomatis")
            .setEventCategory(ToupTrackerEventCategory.TOP_ADS_CREDIT_HISTORY_PAGE)
            .setEventLabel("")
            .setCustomProperty(TrackerId.KEY, "40607")
            .setBusinessUnit(DEFAULT_BUSINESS_UNIT)
            .setCurrentSite(DEFAULT_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickBalanceEvent() {
        Tracker.Builder()
            .setEvent(TopupTrackerEvent.CLICK_TOP_ADS)
            .setEventAction("click - balance")
            .setEventCategory(TOP_ADS_HOME_PAGE_DASHBOARD)
            .setEventLabel("")
            .setCustomProperty(TrackerId.KEY, "40608")
            .setBusinessUnit(DEFAULT_BUSINESS_UNIT)
            .setCurrentSite(DEFAULT_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickEntryPointKreditOtomatisEvent() {
        Tracker.Builder()
            .setEvent(TopupTrackerEvent.CLICK_TOP_ADS)
            .setEventAction("click - entry point kredit otomatis")
            .setEventCategory(ToupTrackerEventCategory.TOP_ADS_CREDIT_HISTORY_PAGE)
            .setEventLabel("")
            .setCustomProperty(TrackerId.KEY, "40609")
            .setBusinessUnit(DEFAULT_BUSINESS_UNIT)
            .setCurrentSite(DEFAULT_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickToggleOnKreditOtomatisEvent(isChecked: Boolean) {
        Tracker.Builder()
            .setEvent(TopupTrackerEvent.CLICK_TOP_ADS)
            .setEventAction(if (isChecked) "click - toggle on kredit otomatis" else "click - toggle off kredit otomatis")
            .setEventCategory(ToupTrackerEventCategory.TOP_ADS_CREDIT_HISTORY_PAGE)
            .setEventLabel("")
            .setCustomProperty(TrackerId.KEY, "40610")
            .setBusinessUnit(DEFAULT_BUSINESS_UNIT)
            .setCurrentSite(DEFAULT_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickKembaliKeEntryPointKreditOtomatisEvent() {
        Tracker.Builder()
            .setEvent(TopupTrackerEvent.CLICK_TOP_ADS)
            .setEventAction("click - kembali ke entry point kredit otomatis")
            .setEventCategory(ToupTrackerEventCategory.TOP_ADS_CREDIT_HISTORY_PAGE)
            .setEventLabel("")
            .setCustomProperty(TrackerId.KEY, "40611")
            .setBusinessUnit(DEFAULT_BUSINESS_UNIT)
            .setCurrentSite(DEFAULT_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickTetapGunakanKreditOtomatisEvent() {
        Tracker.Builder()
            .setEvent(TopupTrackerEvent.CLICK_TOP_ADS)
            .setEventAction("click - tetap gunakan kredit otomatis")
            .setEventCategory(ToupTrackerEventCategory.TOP_ADS_CREDIT_HISTORY_PAGE)
            .setEventLabel("")
            .setCustomProperty(TrackerId.KEY, "40613")
            .setBusinessUnit(DEFAULT_BUSINESS_UNIT)
            .setCurrentSite(DEFAULT_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickNonaktifkanKreditOtomatisEvent() {
        Tracker.Builder()
            .setEvent(TopupTrackerEvent.CLICK_TOP_ADS)
            .setEventAction("click - nonaktifkan kredit otomatis")
            .setEventCategory(ToupTrackerEventCategory.TOP_ADS_CREDIT_HISTORY_PAGE)
            .setEventLabel("")
            .setCustomProperty(TrackerId.KEY, "40614")
            .setBusinessUnit(DEFAULT_BUSINESS_UNIT)
            .setCurrentSite(DEFAULT_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickAmountOfAutoTopupDiModalTambahKreditEvent() {
        Tracker.Builder()
            .setEvent(TopupTrackerEvent.CLICK_TOP_ADS)
            .setEventAction("click - amount of auto topup di modal tambah kredit")
            .setEventCategory(ToupTrackerEventCategory.TOP_ADS_CREDIT_HISTORY_PAGE)
            .setEventLabel("")
            .setCustomProperty(TrackerId.KEY, "40856")
            .setBusinessUnit(DEFAULT_BUSINESS_UNIT)
            .setCurrentSite(DEFAULT_CURRENT_SITE)
            .build()
            .send()
    }
}
