package com.tokopedia.purchase_platform.common.analytics

import javax.inject.Inject

/**
 * @author anggaprasetiyo on 05/06/18.
 */
class CheckoutAnalyticsChangeAddress @Inject constructor() : TransactionAnalytics() {
    fun eventClickAtcCartChangeAddressClickTambahAlamatBaruFromGantiAlamat() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.CLICK_TAMBAH_ALAMAT_BARU_FROM_GANTI_ALAMAT
        )
    }

    fun eventClickAtcCartChangeAddressClickUbahFromPilihAlamatLainnya() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.CLICK_UBAH_FROM_PILIH_ALAMAT_LAINNYA
        )
    }

    fun eventClickAtcCartChangeAddressClickChecklistAlamatFromPilihAlamatLainnya() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.CLICK_CHECKLIST_ALAMAT_FROM_PILIH_ALAMAT_LAINNYA
        )
    }

    fun eventViewAtcCartChangeAddressImpressionChangeAddress() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.VIEW_ATC,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.IMPRESSION_CHANGE_ADDRESS
        )
    }

    fun eventClickAtcCartChangeAddressCartChangeAddressSubmitSearchFromPilihAlamatLainnya() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.SUBMIT_SEARCH_FROM_PILIH_ALAMAT_LAINNYA
        )
    }

    fun eventClickAtcCartChangeAddressClickArrowBackFromGantiAlamat() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.CLICK_ARROW_BACK_FROM_GANTI_ALAMAT
        )
    }

    fun eventClickShippingCartChangeAddressClickTambahFromAlamatPengiriman() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.CLICK_TAMBAH_FROM_ALAMAT_PENGIRIMAN
        )
    }

    fun eventClickShippingCartChangeAddressClickKotaAtauKecamatanPadaTambahAddress() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.CLICK_KOTA_ATAU_KECAMATAN_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickShippingCartChangeAddressClickKodePosPadaTambahAddress() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.CLICK_KODE_POS_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickShippingCartChangeAddressClickTandaiLokasiPadaTambahAddress() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.CLICK_TANDAI_LOKASI_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickShippingCartChangeAddressClickChecklistKotaAtauKecamatanPadaTambahAddress(districtName: String) {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.CLICK_CHECKLIST_KOTA_ATAU_KECAMATAN_PADA_TAMBAH_ADDRESS,
            districtName
        )
    }

    fun eventClickShippingCartChangeAddressClickXPojokKananKotaAtauKecamatanPadaTambahAddress() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.CLICK_X_POJOK_KANAN_KOTA_ATAU_KECAMATAN_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickShippingCartChangeAddressClickXPojokKiriKotaAtauKecamatanPadaTambahAddress() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.CLICK_X_POJOK_KIRI_KOTA_ATAU_KECAMATAN_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickShippingCartChangeAddressClickChecklistKodePosPAdaTambahAddress() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.CLICK_CHECKLIST_KODE_POS_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickShippingCartChangeAddressClickFillKodePosPadaTambahAddress(eventLabel: String) {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.CLICK_FILL_KODE_POS_PADA_TAMBAH_ADDRESS,
            eventLabel
        )
    }

    fun eventViewShippingCartChangeAddressViewValidationErrorNotFill(errorMessage: String) {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.VIEW_SHIPPING,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.VIEW_VALIDATION_ERROR_NOT_FILL,
            errorMessage
        )
    }

    fun eventClickShippingCartChangeAddressClickDropdownSuggestionTandaiLokasiPadaTambahAddress() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.CLICK_DROPDOWN_SUGGESTION_TANDAI_LOKASI_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickShippingCartChangeAddressClickVTandaiLokasiPadaTambahAddress() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.CLICK_V_TANDAI_LOKASI_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickShippingCartChangeAddressClickBackArrowTandaiLokasiPadaTambahAddress() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.CLICK_BACK_ARROW_TANDAI_LOKASI_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickShippingCartChangeAddressClickPinButtonFromTandaiLokasi() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.CLICK_PIN_BUTTON_FROM_TANDAI_LOKASI
        )
    }

    fun eventViewShippingCartChangeAddressViewValidationErrorTandaiLokasi(errorMessage: String) {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.VIEW_SHIPPING,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.VIEW_VALIDATION_ERROR_TANDAI_LOKASI,
            errorMessage
        )
    }

    fun eventClickShippingCartChangeAddressClickRadioButtonFromPilihAlamatLainnya() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.CLICK_RADIO_BUTTON_FROM_PILIH_ALAMAT_LAINNYA
        )
    }

    fun eventClickCourierCartChangeAddressClickAlamatSebagaiPadaTambahAddress() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_COURIER,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.CLICK_ALAMAT_SEBAGAI_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickCourierCartChangeAddressClickChecklistAlamatSebagaiPadaTambahAddress() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_COURIER,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.CLICK_CHECKLIST_ALAMAT_SEBAGAI_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickCourierCartChangeAddressClickNamaPadaTambahAddress() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_COURIER,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.CLICK_NAMA_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickCourierCartChangeAddressClickTeleponPadaTambahAddress() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_COURIER,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.CLICK_TELEPON_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickCourierCartChangeAddressClickAlamatPadaTambahAddress() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_COURIER,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.CLICK_ALAMAT_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickCourierCartChangeAddressErrorValidationAlamatSebagaiPadaTambahAddress() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_COURIER,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.VALIDATION_ERROR_ALAMAT_SEBAGAI_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickCourierCartChangeAddressErrorValidationNamaPadaTambahAddress() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_COURIER,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.VALIDATION_ERROR_NAMA_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickCourierCartChangeAddressErrorValidationTeleponPadaTambahAddress() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_COURIER,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.VALIDATION_ERROR_TELEPON_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickCourierCartChangeAddressErrorValidationKotaKecamatanPadaTambahAddress() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_COURIER,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.VALIDATION_ERROR_KOTA_KECAMATAN_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickCourierCartChangeAddressErrorValidationKodePosPadaTambahAddress() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_COURIER,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.VALIDATION_ERROR_KODE_POS_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickCourierCartChangeAddressErrorValidationAlamatPadaTambahAddress() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_COURIER,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.VALIDATION_ERROR_ALAMAT_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickCourierCartChangeAddressErrorValidationAlamatSebagaiPadaTambahSuccess() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_COURIER,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.CLICK_TAMBAH_ALAMAT_FROM_TAMBAH,
            ConstantTransactionAnalytics.EventLabel.SUCCESS
        )
    }

    fun eventClickCourierCartChangeAddressErrorValidationAlamatSebagaiPadaTambahNotSuccess() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_COURIER,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.CLICK_TAMBAH_ALAMAT_FROM_TAMBAH,
            ConstantTransactionAnalytics.EventLabel.NOT_SUCCESS
        )
    }

    fun eventSearchResultNotFound(query: String) {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ADDRESS,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.SEARCH_NOT_FOUND,
            query
        )
    }

    // PHASE 2

    fun eventClickAddressCartChangeAddressCartChangeAddressSubmitSearchFromPilihAlamatLainnya() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ADDRESS,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.SUBMIT_SEARCH_FROM_PILIH_ALAMAT_LAINNYA
        )
    }

    fun eventClickAddressCartChangeAddressClickButtonSimpanFromEditSuccess() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ADDRESS,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.CLICK_BUTTON_SIMPAN_FROM_EDIT,
            ConstantTransactionAnalytics.EventLabel.SUCCESS
        )
    }

    fun eventClickAddressCartChangeAddressClickButtonSimpanFromEditNotSuccess() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ADDRESS,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.CLICK_BUTTON_SIMPAN_FROM_EDIT,
            ConstantTransactionAnalytics.EventLabel.NOT_SUCCESS
        )
    }
}
