package com.tokopedia.topads.trackers

import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.util.BaseTrackerConst

object SeePerformanceTopadsTracker : BaseTrackerConst() {
    private val trackApp = TrackApp.getInstance().gtm

    private fun fillCommonData(currentSite: String, event: String = ""): MutableMap<String, Any> {
        val map = mutableMapOf<String, Any>()
        map[Event.KEY] = event.ifEmpty { SeePerformanceTopadsTrackerConst.EVENT_CLICK }
        map[Category.KEY] =
            if (currentSite == SeePerformanceTopadsTrackerConst.CURRENT_SITE_MANAGE_PRODUCT_PAGE) {
                SeePerformanceTopadsTrackerConst.EVENT_CATEGORY_MP
            } else {
                SeePerformanceTopadsTrackerConst.EVENT_CATEGORY_PDP
            }
        map[Label.KEY] = SeePerformanceTopadsTrackerConst.EVENT_LABEL
        map[BusinessUnit.KEY] = SeePerformanceTopadsTrackerConst.BUSINESS_UNIT
        map[CurrentSite.KEY] = currentSite
        return map
    }

    fun clickGroupIklan(currentSite: String) {
        val map = fillCommonData(currentSite)
        map[Action.KEY] = SeePerformanceTopadsTrackerConst.Action.CLICK_GROUP_IKLAN
        map[TrackerId.KEY] = SeePerformanceTopadsTrackerConst.getTrackerId(SeePerformanceTopadsTrackerConst.Action.CLICK_GROUP_IKLAN, currentSite)
        trackApp.sendGeneralEvent(map)
    }

    fun clickIklanOtomatis(currentSite: String) {
        val map = fillCommonData(currentSite)
        map[Action.KEY] = SeePerformanceTopadsTrackerConst.Action.CLICK_IKLAN_OTOMATIS
        map[TrackerId.KEY] = SeePerformanceTopadsTrackerConst.getTrackerId(SeePerformanceTopadsTrackerConst.Action.CLICK_IKLAN_OTOMATIS, currentSite)
        trackApp.sendGeneralEvent(map)
    }

    fun clickDateRange(currentSite: String) {
        val map = fillCommonData(currentSite)
        map[Action.KEY] = SeePerformanceTopadsTrackerConst.Action.CLICK_DATE_RANGE
        map[TrackerId.KEY] = SeePerformanceTopadsTrackerConst.getTrackerId(SeePerformanceTopadsTrackerConst.Action.CLICK_DATE_RANGE, currentSite)
        trackApp.sendGeneralEvent(map)
    }

    fun viewErrorFetching(currentSite: String) {
        val map = fillCommonData(currentSite, SeePerformanceTopadsTrackerConst.EVENT_VIEW)
        map[Action.KEY] = SeePerformanceTopadsTrackerConst.Action.VIEW_ERROR_FETCHING
        map[TrackerId.KEY] = SeePerformanceTopadsTrackerConst.getTrackerId(SeePerformanceTopadsTrackerConst.Action.VIEW_ERROR_FETCHING, currentSite)
        trackApp.sendGeneralEvent(map)
    }

    fun clickMuatUlang(currentSite: String) {
        val map = fillCommonData(currentSite)
        map[Action.KEY] = SeePerformanceTopadsTrackerConst.Action.CLICK_MUAT_ULANG
        map[TrackerId.KEY] = SeePerformanceTopadsTrackerConst.getTrackerId(SeePerformanceTopadsTrackerConst.Action.CLICK_MUAT_ULANG, currentSite)
        trackApp.sendGeneralEvent(map)
    }

    fun clickCreditTopads(currentSite: String) {
        val map = fillCommonData(currentSite)
        map[Action.KEY] = SeePerformanceTopadsTrackerConst.Action.CLICK_CREDIT_TOPADS
        map[TrackerId.KEY] = SeePerformanceTopadsTrackerConst.getTrackerId(SeePerformanceTopadsTrackerConst.Action.CLICK_CREDIT_TOPADS, currentSite)
        trackApp.sendGeneralEvent(map)
    }

    fun clickExpandGroupSettings(currentSite: String) {
        val map = fillCommonData(currentSite)
        map[Action.KEY] = SeePerformanceTopadsTrackerConst.Action.CLICK_EXPAND_GROUP_SETTINGS
        map[TrackerId.KEY] = SeePerformanceTopadsTrackerConst.getTrackerId(SeePerformanceTopadsTrackerConst.Action.CLICK_EXPAND_GROUP_SETTINGS, currentSite)
        trackApp.sendGeneralEvent(map)
    }

    fun clickPenempatanIklan(currentSite: String) {
        val map = fillCommonData(currentSite)
        map[Action.KEY] = SeePerformanceTopadsTrackerConst.Action.CLICK_PENEMPATAN_IKLAN
        map[TrackerId.KEY] = SeePerformanceTopadsTrackerConst.getTrackerId(SeePerformanceTopadsTrackerConst.Action.CLICK_PENEMPATAN_IKLAN, currentSite)
        trackApp.sendGeneralEvent(map)
    }

    fun clickIklanPencarian(currentSite: String) {
        val map = fillCommonData(currentSite)
        map[Action.KEY] = SeePerformanceTopadsTrackerConst.Action.CLICK_IKLAN_PENCARIAN
        map[TrackerId.KEY] = SeePerformanceTopadsTrackerConst.getTrackerId(SeePerformanceTopadsTrackerConst.Action.CLICK_IKLAN_PENCARIAN, currentSite)
        trackApp.sendGeneralEvent(map)
    }

    fun clickIklanRekomendasi(currentSite: String) {
        val map = fillCommonData(currentSite)
        map[Action.KEY] = SeePerformanceTopadsTrackerConst.Action.CLICK_IKLAN_REKOMENDASI
        map[TrackerId.KEY] = SeePerformanceTopadsTrackerConst.getTrackerId(SeePerformanceTopadsTrackerConst.Action.CLICK_IKLAN_REKOMENDASI, currentSite)
        trackApp.sendGeneralEvent(map)
    }

    fun clickIklanSemuaPenempatan(currentSite: String) {
        val map = fillCommonData(currentSite)
        map[Action.KEY] = SeePerformanceTopadsTrackerConst.Action.CLICK_IKLAN_SEMUA_PENEMPATAN
        map[TrackerId.KEY] = SeePerformanceTopadsTrackerConst.getTrackerId(SeePerformanceTopadsTrackerConst.Action.CLICK_IKLAN_SEMUA_PENEMPATAN, currentSite)
        trackApp.sendGeneralEvent(map)
    }

    fun clickStatusIklan(currentSite: String) {
        val map = fillCommonData(currentSite)
        map[Action.KEY] = SeePerformanceTopadsTrackerConst.Action.CLICK_STATUS_IKLAN
        map[TrackerId.KEY] = SeePerformanceTopadsTrackerConst.getTrackerId(SeePerformanceTopadsTrackerConst.Action.CLICK_STATUS_IKLAN, currentSite)
        trackApp.sendGeneralEvent(map)
    }

    fun clickStatusIklanAktif(currentSite: String) {
        val map = fillCommonData(currentSite)
        map[Action.KEY] = SeePerformanceTopadsTrackerConst.Action.CLICK_STATUS_IKLAN_AKTIF
        map[TrackerId.KEY] = SeePerformanceTopadsTrackerConst.getTrackerId(SeePerformanceTopadsTrackerConst.Action.CLICK_STATUS_IKLAN_AKTIF, currentSite)
        trackApp.sendGeneralEvent(map)
    }

    fun clickStatusIklanTidakAktif(currentSite: String) {
        val map = fillCommonData(currentSite)
        map[Action.KEY] = SeePerformanceTopadsTrackerConst.Action.CLICK_STATUS_IKLAN_TIDAK_AKTIF
        map[TrackerId.KEY] = SeePerformanceTopadsTrackerConst.getTrackerId(SeePerformanceTopadsTrackerConst.Action.CLICK_STATUS_IKLAN_TIDAK_AKTIF, currentSite)
        trackApp.sendGeneralEvent(map)
    }

    fun viewKreditPage(currentSite: String) {
        val map = fillCommonData(currentSite, SeePerformanceTopadsTrackerConst.EVENT_VIEW)
        map[Action.KEY] = SeePerformanceTopadsTrackerConst.Action.VIEW_KREDIT_PAGE
        map[TrackerId.KEY] = SeePerformanceTopadsTrackerConst.getTrackerId(SeePerformanceTopadsTrackerConst.Action.VIEW_KREDIT_PAGE, currentSite)
        trackApp.sendGeneralEvent(map)
    }

    fun clickTambahKreditPage(currentSite: String) {
        val map = fillCommonData(currentSite)
        map[Action.KEY] = SeePerformanceTopadsTrackerConst.Action.CLICK_TAMBAH_KREDIT_PAGE
        map[TrackerId.KEY] = SeePerformanceTopadsTrackerConst.getTrackerId(SeePerformanceTopadsTrackerConst.Action.CLICK_TAMBAH_KREDIT_PAGE, currentSite)
        trackApp.sendGeneralEvent(map)
    }

    fun clickTambahKredit(currentSite: String) {
        val map = fillCommonData(currentSite)
        map[Action.KEY] = SeePerformanceTopadsTrackerConst.Action.CLICK_TAMBAH_KREDIT
        map[TrackerId.KEY] = SeePerformanceTopadsTrackerConst.getTrackerId(SeePerformanceTopadsTrackerConst.Action.CLICK_TAMBAH_KREDIT, currentSite)
        trackApp.sendGeneralEvent(map)
    }

    fun clickExpandTips(currentSite: String) {
        val map = fillCommonData(currentSite)
        map[Action.KEY] = SeePerformanceTopadsTrackerConst.Action.CLICK_EXPAND_TIPS
        map[TrackerId.KEY] = SeePerformanceTopadsTrackerConst.getTrackerId(SeePerformanceTopadsTrackerConst.Action.CLICK_EXPAND_TIPS, currentSite)
        trackApp.sendGeneralEvent(map)
    }

    fun clickIklanLihatSelengkapnya(currentSite: String) {
        val map = fillCommonData(currentSite)
        map[Action.KEY] = SeePerformanceTopadsTrackerConst.Action.CLICK_IKLAN_LIHAT_SELENGKAPNYA
        map[TrackerId.KEY] = SeePerformanceTopadsTrackerConst.getTrackerId(SeePerformanceTopadsTrackerConst.Action.CLICK_IKLAN_LIHAT_SELENGKAPNYA, currentSite)
        trackApp.sendGeneralEvent(map)
    }
}
