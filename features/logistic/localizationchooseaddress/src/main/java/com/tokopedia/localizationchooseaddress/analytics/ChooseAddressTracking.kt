package com.tokopedia.localizationchooseaddress.analytics

import android.content.Context
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object ChooseAddressTracking : BaseTrackerConst() {

    private const val CLICK_ADDRESS = "clickAddress"

    private const val WIDGET_CHOOSE_ADDRESS = "widget choose address"
    private const val BOTTOMSHEET_CHOOSE_ADDRESS = "bottomsheet choose address"
    private const val ADDRESS_LIST_PAGE = "address list page"
    private const val KOTA_KECAMATAN_PAGE = "kota atau kecamatan page"

    private const val CLICK_WIDGET_CHOOSE_ADDRESS_TRIBE = "click widget choose address"
    private const val CLICK_ALLOW_LOCATION = "click ok allow location"
    private const val CLICK_DONT_ALLOW_LOCATION = "click dont allow location"
    private const val CLICK_AVAILABLE_ADDRESS_CARD = "click available adddress card"
    private const val CLICK_CEK_ALAMAT_LAINNYA = "click cek alamat lainnya"
    private const val IMPRESS_ADDRESS_LIST = "impress address list"
    private const val CLICK_BUTTON_PILIH_ALAMAT = "click button pilih alamat"
    private const val CLICK_TAMBAH_ALAMAT = "click tambah alamat"
    private const val CLICK_UBAH_ALAMAT = "click ubah alamat"
    private const val CLICK_TAMBAH_ALAMAT_PENGIRIMANMU = "click tambah alamat pengirimanmu"
    private const val CLICK_MASUK = "click masuk"
    private const val CLICK_PILIH_KOTA_ATAU_KECAMATAN = "click pilih kota atau kecamatan"
    private const val CLICK_CHIPS_KOTA_POPULER = "clik chips kota populer"
    private const val CLICK_FIELD_SEARCH = "click field search"
    private const val CLICK_SUGGESTION_FROM_DROPDOWN = "click sugesstion from the dropdown"
    private const val CLICK_GUNAKAN_LOKASI_INI = "click gunakan lokasi ini"
    private const val CLICK_CLOSE = "click x"

    private const val BUSINESS_UNIT_LOGISTIC = "logistics & fulfillment"

    fun onClickWidget(source: String, userId: String, eventLabel: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(WIDGET_CHOOSE_ADDRESS)
                .appendEventAction("$CLICK_WIDGET_CHOOSE_ADDRESS_TRIBE $source")
                .appendEventLabel(eventLabel)
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickAllowLocation(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(WIDGET_CHOOSE_ADDRESS)
                .appendEventAction(CLICK_ALLOW_LOCATION)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickDontAllowLocation(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(WIDGET_CHOOSE_ADDRESS)
                .appendEventAction(CLICK_DONT_ALLOW_LOCATION)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickAvailableAddress(userId: String, state: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(BOTTOMSHEET_CHOOSE_ADDRESS)
                .appendEventAction(CLICK_AVAILABLE_ADDRESS_CARD)
                .appendEventLabel(state)
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickCekAlamatLainnya(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(BOTTOMSHEET_CHOOSE_ADDRESS)
                .appendEventAction(CLICK_CEK_ALAMAT_LAINNYA)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickButtonTambahAlamatBottomSheet(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(BOTTOMSHEET_CHOOSE_ADDRESS)
                .appendEventAction(CLICK_TAMBAH_ALAMAT_PENGIRIMANMU)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickMasukBottomSheet(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(BOTTOMSHEET_CHOOSE_ADDRESS)
                .appendEventAction(CLICK_MASUK)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickCloseBottomSheet(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(BOTTOMSHEET_CHOOSE_ADDRESS)
                .appendEventAction(CLICK_CLOSE)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }


    /**
     * Address List Page - ManageAddressFragment
     */
    fun impressAddressListPage(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ADDRESS_LIST_PAGE)
                .appendEventAction(IMPRESS_ADDRESS_LIST)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickAvailableAddressAddressList(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ADDRESS_LIST_PAGE)
                .appendEventAction(CLICK_AVAILABLE_ADDRESS_CARD)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickButtonPilihAlamat(userId: String, state: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ADDRESS_LIST_PAGE)
                .appendEventAction(CLICK_BUTTON_PILIH_ALAMAT)
                .appendEventLabel(state)
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickButtonTambahAlamat(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ADDRESS_LIST_PAGE)
                .appendEventAction(CLICK_TAMBAH_ALAMAT)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickButtonUbahAlamat(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ADDRESS_LIST_PAGE)
                .appendEventAction(CLICK_UBAH_ALAMAT)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    /**
     * Kota Kecamatan Page - DiscomFragment
     */
    fun onClickPilihKotaKecamatan(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(BOTTOMSHEET_CHOOSE_ADDRESS)
                .appendEventAction(CLICK_PILIH_KOTA_ATAU_KECAMATAN)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickChipsKotaPopuler(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(KOTA_KECAMATAN_PAGE)
                .appendEventAction(CLICK_CHIPS_KOTA_POPULER)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickFieldSearchKotaKecamatan(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(KOTA_KECAMATAN_PAGE)
                .appendEventAction(CLICK_FIELD_SEARCH)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickSuggestionKotaKecamatan(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(KOTA_KECAMATAN_PAGE)
                .appendEventAction(CLICK_SUGGESTION_FROM_DROPDOWN)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickGunakanLokasiIni(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(KOTA_KECAMATAN_PAGE)
                .appendEventAction(CLICK_GUNAKAN_LOKASI_INI)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickAllowLocationKotaKecamatan(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(KOTA_KECAMATAN_PAGE)
                .appendEventAction(CLICK_ALLOW_LOCATION)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickDontAllowLocationKotaKecamatan(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(KOTA_KECAMATAN_PAGE)
                .appendEventAction(CLICK_DONT_ALLOW_LOCATION)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

    fun onClickCloseKotaKecamatan(userId: String) {
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(KOTA_KECAMATAN_PAGE)
                .appendEventAction(CLICK_CLOSE)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build())
    }

}