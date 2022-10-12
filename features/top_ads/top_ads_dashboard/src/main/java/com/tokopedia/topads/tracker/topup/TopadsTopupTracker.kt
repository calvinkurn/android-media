package com.tokopedia.topads.tracker.topup

import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.util.BaseTrackerConst

object TopadsTopupTracker : BaseTrackerConst(){

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
            trackerId = "31852",
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
            trackerId = "31854",
        )
    }

    fun clickSaldoDropdownList() {
        sendClickEventFromCreditHistoryPage(
            eventAction = ToupTrackerEventAction.CLICK_SALDO_DROPDOWN_LIST,
            trackerId = "31855",
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
            trackerId = "31857",
        )
    }

    fun clickYaNonaktifkan() {
        sendClickEventFromCreditHistoryPage(
            eventAction = ToupTrackerEventAction.CLICK_DEACTIVATION_AUTO_TOPUP,
            trackerId = "31858",
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
            trackerId = "31989",
        )
    }

    private fun sendClickEventFromTaCart(
        trackerId: String,
        eventAction: String,
        eventLabel: String = "",
    ) {
        sendAnalytics(
            eventCategory = ToupTrackerEventCategory.TA_CART,
            eventLabel = eventLabel,
            eventAction = eventAction,
            trackerId = trackerId,
        )
    }

    private fun sendClickEventFromCreditHistoryPage(
        trackerId: String,
        eventAction: String,
        eventLabel: String = "",
    ) {
        sendAnalytics(
            eventCategory = ToupTrackerEventCategory.TOP_ADS_CREDIT_HISTORY_PAGE,
            eventLabel = eventLabel,
            eventAction = eventAction,
            trackerId = trackerId,
        )
    }

    private fun sendAnalytics(
        event: String = TopupTrackerEvent.CLICK_TOP_ADS,
        eventAction: String,
        eventCategory: String,
        eventLabel: String = "",
        trackerId: String,
        businessUnit: String = TopadsToupTrackerConstants.DEFAULT_BUSINESS_UNIT,
        currentSite: String = TopadsToupTrackerConstants.DEFAULT_CURRENT_SITE,
    ) {
        val analyticsBundle = mapOf(
            Event.KEY to event,
            Action.KEY to eventAction,
            Category.KEY to eventCategory,
            Label.KEY to eventLabel,
            TrackerId.KEY to trackerId,
            BusinessUnit.KEY to businessUnit,
            CurrentSite.KEY to currentSite,
        )
        TrackApp.getInstance().gtm.apply {
            sendGeneralEvent(analyticsBundle)
        }
    }

}