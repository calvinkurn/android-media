package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.analytics

import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object AddNewAddressRevampAnalytics : BaseTrackerConst() {

    private const val CLICK_SEARCH = "clickSearch"
    private const val CLICK_PINPOINT = "clickPinpoint"
    private const val VIEW_PINPOINT_IRIS = "viewPinPointIris"

    private const val CLICK_FIELD_CARI_LOKASI = "click field cari lokasi"
    private const val CLICK_DROPDOWN_SUGGESTION = "click dropdown suggestion"
    private const val CLICK_GUNAKAN_LOKASI_SAAT_INI = "click gunakan lokasi saat ini"
    private const val CLICK_OK_ALLOW_LOCATION = "click ok on popup allow location"
    private const val CLICK_DONT_ALLOW_LOCATION = "click dont allow on popup allow location"
    private const val CLICK_AKTIFKAN_LAYANAN_LOKASI_ON_BLOCK_GPS = "click aktifkan layanan lokasi on block gps"
    private const val CLICK_X_ON_BLOCK_GPS = "click x on block gps"
    private const val CLICK_ISI_ALAMAT_MANUAL = "click isi alamat manual"
    private const val CLICK_BACK_ARROW = "click back arrrow on top left corner"
    private const val IMPRESS_ON_BOTTOMSHEET_ALAMAT_TIDAK_TERDETEKSI = "impressi on bottomsheet alamat tidak terdeteksi"
    private const val CLICK_ISI_ALAMAT_MANUAL_FROM_UNDETECTED_LOC_BOTTOMSHEET ="click isi alamat manual from undetected location bottomsheet"
    private const val IMPRESS_ON_BOTTOMSHEET_OUT_OF_INDO = "impressi on out of indonesia"
    private const val CLICK_ISI_ALAMAT_MANUAL_FROM_OUT_OF_INDO = "click isi alamat manual from out of indonesia bottomsheet"
    private const val CLICK_CARI_ULANG_ALAMAT = "click cari ulang alamat"
    private const val CLICK_ICON_QUESTION = "click icon ?"
    private const val CLICK_PILIH_LOKASI_DAN_LANJUT_ISI_ALAMAT = "click pilih lokasi dan lanjut isi alamat"
    private const val CLICK_PILIH_LOKASI_DAN_LANJUT_ISI_ALAMAT_NEGATIVE = "click pilih lokasi dan lanjut isi alamat negative"
    private const val VIEW_TOASTER_PASTIKAN_PINPOINT_SESUAI_KOTA_KECAMATAN = "view toaster pastikan pinpoint lokasi sesuai dengan kota dan kecamatanmu"


    private const val SEARCH_PAGE = "search page"
    private const val PINPOINT_PAGE = "pinpoint page"

    private const val BUSINESS_UNIT_LOGISTIC = "logistics & fulfillment"

    /*Search Page*/
    fun onClickFieldCariLokasi(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_SEARCH)
                .appendEventCategory(SEARCH_PAGE)
                .appendEventAction(CLICK_FIELD_CARI_LOKASI)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickDropdownSuggestion(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_SEARCH)
                .appendEventCategory(SEARCH_PAGE)
                .appendEventAction(CLICK_DROPDOWN_SUGGESTION)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickGunakanLokasiSaatIniSearch(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_SEARCH)
                .appendEventCategory(SEARCH_PAGE)
                .appendEventAction(CLICK_GUNAKAN_LOKASI_SAAT_INI)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickAllowLocationSearch(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_SEARCH)
                .appendEventCategory(SEARCH_PAGE)
                .appendEventAction(CLICK_OK_ALLOW_LOCATION)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickDontAllowLocationSearch(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_SEARCH)
                .appendEventCategory(SEARCH_PAGE)
                .appendEventAction(CLICK_DONT_ALLOW_LOCATION)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickAktifkanLayananLokasiSearch(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_SEARCH)
                .appendEventCategory(SEARCH_PAGE)
                .appendEventAction(CLICK_AKTIFKAN_LAYANAN_LOKASI_ON_BLOCK_GPS)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickXOnBlockGpsSearch(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_SEARCH)
                .appendEventCategory(SEARCH_PAGE)
                .appendEventAction(CLICK_X_ON_BLOCK_GPS)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickIsiAlamatManualSearch(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_SEARCH)
                .appendEventCategory(SEARCH_PAGE)
                .appendEventAction(CLICK_ISI_ALAMAT_MANUAL)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickBackArrowSearch(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_SEARCH)
                .appendEventCategory(SEARCH_PAGE)
                .appendEventAction(CLICK_BACK_ARROW)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    /*Pinpoint Page*/
    fun onImpressBottomSheetAlamatTidakTerdeteksi(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(VIEW_PINPOINT_IRIS)
                .appendEventCategory(PINPOINT_PAGE)
                .appendEventAction(IMPRESS_ON_BOTTOMSHEET_ALAMAT_TIDAK_TERDETEKSI)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickIsiAlamatManualUndetectedLocation(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_PINPOINT)
                .appendEventCategory(PINPOINT_PAGE)
                .appendEventAction(CLICK_ISI_ALAMAT_MANUAL_FROM_UNDETECTED_LOC_BOTTOMSHEET)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onImpressBottomSheetOutOfIndo(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(VIEW_PINPOINT_IRIS)
                .appendEventCategory(PINPOINT_PAGE)
                .appendEventAction(IMPRESS_ON_BOTTOMSHEET_OUT_OF_INDO)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickIsiAlamatOutOfIndo(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_PINPOINT)
                .appendEventCategory(PINPOINT_PAGE)
                .appendEventAction(CLICK_ISI_ALAMAT_MANUAL_FROM_OUT_OF_INDO)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickBackArrowPinpoint(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_PINPOINT)
                .appendEventCategory(PINPOINT_PAGE)
                .appendEventAction(CLICK_BACK_ARROW)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickCariUlangAlamat(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_PINPOINT)
                .appendEventCategory(PINPOINT_PAGE)
                .appendEventAction(CLICK_CARI_ULANG_ALAMAT)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickGunakanLokasiSaatIniPinpoint(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_PINPOINT)
                .appendEventCategory(PINPOINT_PAGE)
                .appendEventAction(CLICK_GUNAKAN_LOKASI_SAAT_INI)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickAllowLocationPinpoint(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_PINPOINT)
                .appendEventCategory(PINPOINT_PAGE)
                .appendEventAction(CLICK_OK_ALLOW_LOCATION)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickDontAllowLocationPinpoint(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_PINPOINT)
                .appendEventCategory(PINPOINT_PAGE)
                .appendEventAction(CLICK_DONT_ALLOW_LOCATION)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }


    fun onClickAktifkanLayananLokasiPinpoint(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_PINPOINT)
                .appendEventCategory(PINPOINT_PAGE)
                .appendEventAction(CLICK_AKTIFKAN_LAYANAN_LOKASI_ON_BLOCK_GPS)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickXOnBlockGpsPinpoint(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_PINPOINT)
                .appendEventCategory(PINPOINT_PAGE)
                .appendEventAction(CLICK_X_ON_BLOCK_GPS)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickIconQuestion(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_PINPOINT)
                .appendEventCategory(PINPOINT_PAGE)
                .appendEventAction(CLICK_ICON_QUESTION)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickPilihLokasiPositive(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_PINPOINT)
                .appendEventCategory(PINPOINT_PAGE)
                .appendEventAction(CLICK_PILIH_LOKASI_DAN_LANJUT_ISI_ALAMAT)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickPilihLokasiNegative(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_PINPOINT)
                .appendEventCategory(PINPOINT_PAGE)
                .appendEventAction(CLICK_PILIH_LOKASI_DAN_LANJUT_ISI_ALAMAT_NEGATIVE)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickIsiAlamatManual(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_PINPOINT)
                .appendEventCategory(PINPOINT_PAGE)
                .appendEventAction(CLICK_ISI_ALAMAT_MANUAL)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onViewToasterPinpointTidakSesuai(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_PINPOINT)
                .appendEventCategory(PINPOINT_PAGE)
                .appendEventAction(VIEW_TOASTER_PASTIKAN_PINPOINT_SESUAI_KOTA_KECAMATAN)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

}