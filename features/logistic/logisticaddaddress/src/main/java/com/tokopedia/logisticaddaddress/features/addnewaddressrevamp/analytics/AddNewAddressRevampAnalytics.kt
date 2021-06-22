package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.analytics

import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object AddNewAddressRevampAnalytics : BaseTrackerConst() {

    private const val CLICK_SEARCH = "clickSearch"
    private const val CLICK_PINPOINT = "clickPinpoint"
    private const val VIEW_PINPOINT_IRIS = "viewPinPointIris"
    private const val CLICK_ADDRESS = "clickAddress"

    private const val CLICK_FIELD_CARI_LOKASI = "click field cari lokasi"
    private const val CLICK_DROPDOWN_SUGGESTION_ALAMAT = "click dropdown suggestion alamat"
    private const val CLICK_GUNAKAN_LOKASI_SAAT_INI = "click gunakan lokasi saat ini"
    private const val CLICK_OK_ALLOW_LOCATION = "click ok on popup allow location"
    private const val CLICK_DONT_ALLOW_LOCATION = "click dont allow on popup allow location"
    private const val CLICK_AKTIFKAN_LAYANAN_LOKASI_ON_BLOCK_GPS = "click aktifkan layanan lokasi on block gps"
    private const val CLICK_X_ON_BLOCK_GPS = "click x on block gps"
    private const val CLICK_ISI_ALAMAT_MANUAL = "click isi alamat manual"
    private const val CLICK_BACK_ARROW = "click back arrow on top left corner"
    private const val CLICK_BACK_ARROW_BUTTON = "click back arrow button on top left corner"
    private const val IMPRESSION_BOTTOMSHEET_ALAMAT_TIDAK_TERDETEKSI = "impression bottomsheet alamat tidak terdeteksi"
    private const val CLICK_ISI_ALAMAT_MANUAL_FROM_UNDETECTED_LOC_BOTTOMSHEET ="click isi alamat manual from undetected location bottomsheet"
    private const val IMPRESSION_BOTTOMSHEET_OUT_OF_INDO = "impression out of indonesia"
    private const val CLICK_ISI_ALAMAT_MANUAL_FROM_OUT_OF_INDO = "click isi alamat manual from out of indonesia bottomsheet"
    private const val CLICK_CARI_ULANG_ALAMAT = "click cari ulang alamat"
    private const val CLICK_ICON_QUESTION = "click icon ?"
    private const val CLICK_PILIH_LOKASI_DAN_LANJUT_ISI_ALAMAT = "click pilih lokasi dan lanjut isi alamat"
    private const val CLICK_PILIH_LOKASI_DAN_LANJUT_ISI_ALAMAT_NEGATIVE = "click pilih lokasi dan lanjut isi alamat negative"
    private const val VIEW_TOASTER_PASTIKAN_PINPOINT_SESUAI_KOTA_KECAMATAN = "view toaster pastikan pinpoint lokasi sesuai dengan kota dan kecamatanmu"
    private const val CLICK_FIELD_LABEL_ALAMAT = "click field label alamat"
    private const val CLICK_CHIPS_LABEL_ALAMAT = "click chips label alamat"
    private const val CLICK_FIELD_ALAMAT = "click field alamat"
    private const val CLICK_FIELD_CATATAN_KURIR = "click field catatan untuk kurir"
    private const val CLICK_FIELD_NAMA_PENERIMA = "click field nama penerima"
    private const val CLICK_ICON_I_NAMA_PENERIMA = "click icon i nama penerima"
    private const val CLICK_FIELD_NO_HP = "click field nomor hp"
    private const val CLICK_PHONE_BOOK_ICON = "click phone book icon"
    private const val CLICK_BOX_JADIKAN_ALAMAT_UTAMA = "click box jadikan alamat utama"
    private const val CLICK_SIMPAN = "click simpan"
    private const val CLICK_SIMPAN_ERROR = "click simpan button, error in 1 or more field"
    private const val CLICK_FIELD_CARI_KOTA_KECAMATAN = "click field cari kota kecamatan"
    private const val CLICK_FIELD_KOTA_KECAMATAN = "click field kota dan kecamatan"
    private const val CLICK_CHIPS_KOTA_POPULER = "click chips kota populer"
    private const val CLICK_DROPDOWN_SUGGESTION_KOTA_KECAMATAN = "click dropdown suggestion kota kecamatan"
    private const val CLICK_FIELD_KODE_POS = "click field kode pos"
    private const val CLICK_CHIPS_KODE_POS = "click chips kode pos"
    private const val CLICK_PILIH = "click pilih"
    private const val VIEW_ERROR_TOASTER_KODE_POS = "view error toaster kode pos terlalu pendek, min 5 karakter"
    private const val CLICK_ATUR_PINPOINT = "click atur pinpoint"

    private const val SEARCH_PAGE = "search page"
    private const val PINPOINT_PAGE = "pinpoint page"
    private const val ANA_POSITIVE = "add new address positive"
    private const val ANA_NEGATIVE= "add new address negative"
    private const val KOTA_KECAMATAN_PAGE = "kota kecamatan page"
    private const val KODE_POST_PAGE = "kode pos page"

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
                .appendEventAction(CLICK_DROPDOWN_SUGGESTION_ALAMAT)
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
                .appendEventAction(IMPRESSION_BOTTOMSHEET_ALAMAT_TIDAK_TERDETEKSI)
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
                .appendEventAction(IMPRESSION_BOTTOMSHEET_OUT_OF_INDO)
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
                .appendEventAction(CLICK_BACK_ARROW_BUTTON)
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

    fun onClickPilihLokasiPositive(userId: String, eventLabel: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_PINPOINT)
                .appendEventCategory(PINPOINT_PAGE)
                .appendEventAction(CLICK_PILIH_LOKASI_DAN_LANJUT_ISI_ALAMAT)
                .appendEventLabel(eventLabel)
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickPilihLokasiNegative(userId: String, eventLabel: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_PINPOINT)
                .appendEventCategory(PINPOINT_PAGE)
                .appendEventAction(CLICK_PILIH_LOKASI_DAN_LANJUT_ISI_ALAMAT_NEGATIVE)
                .appendEventLabel(eventLabel)
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
                .appendEvent(VIEW_PINPOINT_IRIS)
                .appendEventCategory(PINPOINT_PAGE)
                .appendEventAction(VIEW_TOASTER_PASTIKAN_PINPOINT_SESUAI_KOTA_KECAMATAN)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    /*ANA Positive*/
    fun onClickFieldLabelAlamatPositive(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_POSITIVE)
                .appendEventAction(CLICK_FIELD_LABEL_ALAMAT)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickChipsLabelAlamatPositive(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_POSITIVE)
                .appendEventAction(CLICK_CHIPS_LABEL_ALAMAT)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickFieldAlamatPositive(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_POSITIVE)
                .appendEventAction(CLICK_FIELD_ALAMAT)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickFieldCatatanKurirPositive(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_POSITIVE)
                .appendEventAction(CLICK_FIELD_CATATAN_KURIR)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickFieldNamaPenerimaPositive(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_POSITIVE)
                .appendEventAction(CLICK_FIELD_NAMA_PENERIMA)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickIconNamaPenerimaPositive(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_POSITIVE)
                .appendEventAction(CLICK_ICON_I_NAMA_PENERIMA)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickFieldNomorHpPositive(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_POSITIVE)
                .appendEventAction(CLICK_FIELD_NO_HP)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickIconPhoneBookPositive(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_POSITIVE)
                .appendEventAction(CLICK_PHONE_BOOK_ICON)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickBoxJadikanAlamatUtamaPositive(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_POSITIVE)
                .appendEventAction(CLICK_BOX_JADIKAN_ALAMAT_UTAMA)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickSimpanPositive(userId: String, eventLabel: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_POSITIVE)
                .appendEventAction(CLICK_SIMPAN)
                .appendEventLabel(eventLabel)
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickSimpanErrorPositive(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_POSITIVE)
                .appendEventAction(CLICK_SIMPAN_ERROR)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickBackPositive(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_POSITIVE)
                .appendEventAction(CLICK_BACK_ARROW)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    /*ANA Negative*/
    fun onClickFieldLabelAlamatNegative(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_NEGATIVE)
                .appendEventAction(CLICK_FIELD_LABEL_ALAMAT)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickChipsLabelAlamatNegative(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_NEGATIVE)
                .appendEventAction(CLICK_CHIPS_LABEL_ALAMAT)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickFieldAlamatNegative(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_NEGATIVE)
                .appendEventAction(CLICK_FIELD_ALAMAT)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickFieldCatatanKurirNegative(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_NEGATIVE)
                .appendEventAction(CLICK_FIELD_CATATAN_KURIR)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickFieldNamaPenerimaNegative(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_NEGATIVE)
                .appendEventAction(CLICK_FIELD_NAMA_PENERIMA)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickIconNamaPenerimaNegative(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_NEGATIVE)
                .appendEventAction(CLICK_ICON_I_NAMA_PENERIMA)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickFieldNomorHpNegative(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_NEGATIVE)
                .appendEventAction(CLICK_FIELD_NO_HP)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickIconPhoneBookNegative(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_NEGATIVE)
                .appendEventAction(CLICK_PHONE_BOOK_ICON)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickBoxJadikanAlamatUtamaNegative(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_NEGATIVE)
                .appendEventAction(CLICK_BOX_JADIKAN_ALAMAT_UTAMA)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickSimpanNegative(userId: String, eventLabel: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_NEGATIVE)
                .appendEventAction(CLICK_SIMPAN)
                .appendEventLabel(eventLabel)
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickSimpanErrorNegative(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_NEGATIVE)
                .appendEventAction(CLICK_SIMPAN_ERROR)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickBackNegative(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_NEGATIVE)
                .appendEventAction(CLICK_BACK_ARROW)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickFieldKotaKecamatanNegative(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_NEGATIVE)
                .appendEventAction(CLICK_FIELD_KOTA_KECAMATAN)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickAturPinpointNegative(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_NEGATIVE)
                .appendEventAction(CLICK_ATUR_PINPOINT)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    /*Discom Bottomsheet ANA Negative*/
    fun onClickFieldCariKotaKecamatanNegative(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory("$ANA_NEGATIVE, $KOTA_KECAMATAN_PAGE")
                .appendEventAction(CLICK_FIELD_CARI_KOTA_KECAMATAN)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickChipsKotaKecamatanNegative(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory("$ANA_NEGATIVE, $KOTA_KECAMATAN_PAGE")
                .appendEventAction(CLICK_CHIPS_KOTA_POPULER)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickBackArrowDiscom(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory("$ANA_NEGATIVE, $KOTA_KECAMATAN_PAGE")
                .appendEventAction(CLICK_BACK_ARROW)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickFieldKodePosNegative(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory("$ANA_NEGATIVE, $KODE_POST_PAGE")
                .appendEventAction(CLICK_FIELD_KODE_POS)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickDropDownSuggestionKotaNegative(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory("$ANA_NEGATIVE, $KOTA_KECAMATAN_PAGE")
                .appendEventAction(CLICK_DROPDOWN_SUGGESTION_KOTA_KECAMATAN)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickChipsKodePosNegative(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory("$ANA_NEGATIVE, $KODE_POST_PAGE")
                .appendEventAction(CLICK_CHIPS_KODE_POS)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickPilihKodePos(userId: String, eventLabel: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory("$ANA_NEGATIVE, $KODE_POST_PAGE")
                .appendEventAction(CLICK_PILIH)
                .appendEventLabel(eventLabel)
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onViewErrorToasterPilih(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory("$ANA_NEGATIVE, $KOTA_KECAMATAN_PAGE")
                .appendEventAction(VIEW_ERROR_TOASTER_KODE_POS)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

}