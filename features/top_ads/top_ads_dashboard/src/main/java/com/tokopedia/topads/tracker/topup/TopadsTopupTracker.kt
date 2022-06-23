package com.tokopedia.topads.tracker.topup

import javax.inject.Inject

class TopadsTopupTracker @Inject constructor(

) {

    fun clickCobaSekarang() {
        sendClickEventFromCreditHistoryPage(
            eventAction = ToupTrackerEventAction.CLICK_COBA_SERANG_AUTO_TOPUP,
            trackerId = "31839"
        )
    }

    fun ClickTambahKredit() {
        sendClickEventFromCreditHistoryPage(
            eventAction = ToupTrackerEventAction.CLICK_TOPUP,
            trackerId = "31840"
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

    fun clickTambahKredit() {
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

    fun clickThresholdCredit(topUpAmount: String) {
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

    //15 end


    fun sendClickEventFromCreditHistoryPage(
        trackerId: String,
        eventAction: String,
        eventLabel: String = "",
    ) {
        sendAnalytics(
            event = TopupTrackerEvent.CLICK_TOP_ADS,
            eventCategory = ToupTrackerEventCategory.TOP_ADS_CREDIT_HISTORY_PAGE,
            eventLabel = eventLabel,
            eventAction = eventAction,
            trackerId = trackerId,
        )
    }

    fun sendAnalytics(
        event: String,
        eventAction: String,
        eventCategory: String,
        eventLabel: String,
        trackerId: String,
        businessUnit: String = "TODO",
        currentSite: String = "TODO",
    ) {

    }

}