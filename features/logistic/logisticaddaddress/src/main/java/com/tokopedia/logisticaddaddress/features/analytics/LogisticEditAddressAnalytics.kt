package com.tokopedia.logisticaddaddress.features.analytics

import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object LogisticEditAddressAnalytics : BaseTrackerConst() {
    //    event name
    private const val EVENT_VIEW = "viewLogisticIris"
    private const val EVENT_CLICK = "clickLogistic"

    //    event label
    private const val LABEL_SUCCESS = "success"
    private const val LABEL_NOT_SUCCESS = "not success"

    //    event action
    private const val ACTION_CLICK_BACK_ARROW = "click back arrow on top left corner"
    private const val ACTION_CLICK_FIELD_CARI_KOTA_KECAMATAN = "click field cari kota kecamatan"
    private const val ACTION_CLICK_CHIPS_KOTA_POPULER = "click chips kota populer"
    private const val ACTION_CLICK_DROPDOWN_SUGGESTION_KOTA_KECAMATAN = "click dropdown suggestion kota kecamatan"
    private const val ACTION_CLICK_GUNAKAN_LOKASI_INI = "click gunakan lokasi ini"
    private const val ACTION_CLICK_FIELD_KODE_POS = "click field kode pos"
    private const val ACTION_CLICK_CHIPS_KODE_POS = "click chips kode pos"
    private const val ACTION_CLICK_PILIH = "click pilih"
    private const val ACTION_VIEW_ERROR_TOASTER_KODE_POS = "view error toaster kode pos terlalu pendek, min 5 karakter"
    private const val ACTION_CLICK_GUNAKAN_LOKASI_SAAT_INI = "click gunakan lokasi saat ini"
    private const val ACTION_CLICK_CARI_ULANG_ALAMAT = "click cari ulang alamat"
    private const val ACTION_CLICK_PILIH_LOKASI_INI = "click pilih lokasi ini"
    private const val ACTION_IMPRESSION_BOTTOMSHEET_OUT_OF_INDO = "impression out of indonesia"
    private const val ACTION_IMPRESSION_BOTTOMSHEET_ALAMAT_TIDAK_TERDETEKSI = "impression bottomsheet alamat tidak terdeteksi"
    private const val ACTION_CLICK_FIELD_CARI_LOKASI = "click field cari lokasi"
    private const val ACTION_CLICK_DROPDOWN_SUGGESTION_ALAMAT = "click dropdown suggestion alamat"

    //    event category
    private const val CATEGORY_EDIT_KOTA_KECAMATAN_PAGE = "edit address, kota kecamatan page"
    private const val CATEGORY_EDIT_KODE_POS_PAGE = "edit address, kode pos page"
    private const val CATEGORY_EDIT_PINPOINT_PAGE = "edit address, pinpoint page"
    private const val CATEGORY_EDIT_SEARCH_PAGE = "edit address, search page"

    //    business unit
    private const val BUSINESS_UNIT_LOGISTIC = "logistic"

    fun onClickFieldCariKotaKecamatan(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK)
                .appendEventCategory(CATEGORY_EDIT_KOTA_KECAMATAN_PAGE)
                .appendEventAction(ACTION_CLICK_FIELD_CARI_KOTA_KECAMATAN)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickChipsKotaKecamatan(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK)
                .appendEventCategory(CATEGORY_EDIT_KOTA_KECAMATAN_PAGE)
                .appendEventAction(ACTION_CLICK_CHIPS_KOTA_POPULER)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickDropDownSuggestionKota(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK)
                .appendEventCategory(CATEGORY_EDIT_KOTA_KECAMATAN_PAGE)
                .appendEventAction(ACTION_CLICK_DROPDOWN_SUGGESTION_KOTA_KECAMATAN)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickGunakanLokasiIni(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK)
                .appendEventCategory(CATEGORY_EDIT_KOTA_KECAMATAN_PAGE)
                .appendEventAction(ACTION_CLICK_GUNAKAN_LOKASI_INI)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickBackArrowDiscom(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK)
                .appendEventCategory(CATEGORY_EDIT_KOTA_KECAMATAN_PAGE)
                .appendEventAction(ACTION_CLICK_BACK_ARROW)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickFieldKodePos(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK)
                .appendEventCategory(CATEGORY_EDIT_KODE_POS_PAGE)
                .appendEventAction(ACTION_CLICK_FIELD_KODE_POS)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickChipsKodePos(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK)
                .appendEventCategory(CATEGORY_EDIT_KODE_POS_PAGE)
                .appendEventAction(ACTION_CLICK_CHIPS_KODE_POS)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickPilihKodePos(userId: String, success: Boolean) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK)
                .appendEventCategory(CATEGORY_EDIT_KODE_POS_PAGE)
                .appendEventAction(ACTION_CLICK_PILIH)
                .appendEventLabel(if (success) LABEL_SUCCESS else LABEL_NOT_SUCCESS)
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onViewErrorToaster(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_VIEW)
                .appendEventCategory(CATEGORY_EDIT_KODE_POS_PAGE)
                .appendEventAction(ACTION_VIEW_ERROR_TOASTER_KODE_POS)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickBackArrowKodePos(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK)
                .appendEventCategory(CATEGORY_EDIT_KODE_POS_PAGE)
                .appendEventAction(ACTION_CLICK_BACK_ARROW)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickGunakanLokasiSaatIniPinpoint(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK)
                .appendEventCategory(CATEGORY_EDIT_PINPOINT_PAGE)
                .appendEventAction(ACTION_CLICK_GUNAKAN_LOKASI_SAAT_INI)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickCariUlangAlamat(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK)
                .appendEventCategory(CATEGORY_EDIT_PINPOINT_PAGE)
                .appendEventAction(ACTION_CLICK_CARI_ULANG_ALAMAT)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickPilihLokasiIni(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK)
                .appendEventCategory(CATEGORY_EDIT_PINPOINT_PAGE)
                .appendEventAction(ACTION_CLICK_PILIH_LOKASI_INI)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onImpressBottomSheetOutOfIndo(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_VIEW)
                .appendEventCategory(CATEGORY_EDIT_PINPOINT_PAGE)
                .appendEventAction(ACTION_IMPRESSION_BOTTOMSHEET_OUT_OF_INDO)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onImpressBottomSheetAlamatTidakTerdeteksi(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_VIEW)
                .appendEventCategory(CATEGORY_EDIT_PINPOINT_PAGE)
                .appendEventAction(ACTION_IMPRESSION_BOTTOMSHEET_ALAMAT_TIDAK_TERDETEKSI)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickBackPinpoint(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK)
                .appendEventCategory(CATEGORY_EDIT_PINPOINT_PAGE)
                .appendEventAction(ACTION_CLICK_BACK_ARROW)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickFieldCariLokasi(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK)
                .appendEventCategory(CATEGORY_EDIT_SEARCH_PAGE)
                .appendEventAction(ACTION_CLICK_FIELD_CARI_LOKASI)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickDropdownSuggestionAlamat(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK)
                .appendEventCategory(CATEGORY_EDIT_SEARCH_PAGE)
                .appendEventAction(ACTION_CLICK_DROPDOWN_SUGGESTION_ALAMAT)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickGunakanLokasiSaatIniSearch(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK)
                .appendEventCategory(CATEGORY_EDIT_SEARCH_PAGE)
                .appendEventAction(ACTION_CLICK_GUNAKAN_LOKASI_SAAT_INI)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickBackArrowSearch(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK)
                .appendEventCategory(CATEGORY_EDIT_SEARCH_PAGE)
                .appendEventAction(ACTION_CLICK_BACK_ARROW)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }
}
