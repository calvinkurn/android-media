package com.tokopedia.purchase_platform.common.analytics

import javax.inject.Inject

/**
 * @author anggaprasetiyo on 05/06/18.
 */
class CheckoutAnalyticsChangeAddress @Inject constructor() : TransactionAnalytics() {
    fun eventClickAtcCartChangeAddressClickTambahAlamatBaruFromGantiAlamat() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_TAMBAH_ALAMAT_BARU_FROM_GANTI_ALAMAT
        )
    }

    fun eventClickAtcCartChangeAddressClickUbahFromPilihAlamatLainnya() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_UBAH_FROM_PILIH_ALAMAT_LAINNYA
        )
    }

    fun eventClickAtcCartChangeAddressClickChecklistAlamatFromPilihAlamatLainnya() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_CHECKLIST_ALAMAT_FROM_PILIH_ALAMAT_LAINNYA
        )
    }

    fun eventViewAtcCartChangeAddressImpressionChangeAddress() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.VIEW_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.IMPRESSION_CHANGE_ADDRESS
        )
    }

    fun eventClickAtcCartChangeAddressCartChangeAddressSubmitSearchFromPilihAlamatLainnya() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.SUBMIT_SEARCH_FROM_PILIH_ALAMAT_LAINNYA
        )
    }

    fun eventClickAtcCartChangeAddressClickArrowBackFromGantiAlamat() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_ARROW_BACK_FROM_GANTI_ALAMAT
        )
    }

    fun eventClickShippingCartChangeAddressClickTambahFromAlamatPengiriman() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_TAMBAH_FROM_ALAMAT_PENGIRIMAN
        )
    }

    fun eventClickShippingCartChangeAddressClickKotaAtauKecamatanPadaTambahAddress() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_KOTA_ATAU_KECAMATAN_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickShippingCartChangeAddressClickKodePosPadaTambahAddress() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_KODE_POS_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickShippingCartChangeAddressClickTandaiLokasiPadaTambahAddress() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_TANDAI_LOKASI_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickShippingCartChangeAddressClickChecklistKotaAtauKecamatanPadaTambahAddress(districtName: String?) {
        sendEventCategoryActionLabel(
                ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_CHECKLIST_KOTA_ATAU_KECAMATAN_PADA_TAMBAH_ADDRESS,
                districtName
        )
    }

    fun eventClickShippingCartChangeAddressClickXPojokKananKotaAtauKecamatanPadaTambahAddress() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_X_POJOK_KANAN_KOTA_ATAU_KECAMATAN_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickShippingCartChangeAddressClickXPojokKiriKotaAtauKecamatanPadaTambahAddress() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_X_POJOK_KIRI_KOTA_ATAU_KECAMATAN_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickShippingCartChangeAddressClickChecklistKodePosPAdaTambahAddress() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_CHECKLIST_KODE_POS_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickShippingCartChangeAddressClickFillKodePosPadaTambahAddress(eventLabel: String?) {
        sendEventCategoryActionLabel(
                ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_FILL_KODE_POS_PADA_TAMBAH_ADDRESS,
                eventLabel
        )
    }

    fun eventViewShippingCartChangeAddressViewValidationErrorNotFill(errorMessage: String?) {
        sendEventCategoryActionLabel(
                ConstantTransactionAnalytics.EventName.VIEW_SHIPPING,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.VIEW_VALIDATION_ERROR_NOT_FILL,
                errorMessage
        )
    }

    fun eventClickShippingCartChangeAddressClickDropdownSuggestionTandaiLokasiPadaTambahAddress() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_DROPDOWN_SUGGESTION_TANDAI_LOKASI_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickShippingCartChangeAddressClickVTandaiLokasiPadaTambahAddress() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_V_TANDAI_LOKASI_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickShippingCartChangeAddressClickBackArrowTandaiLokasiPadaTambahAddress() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_BACK_ARROW_TANDAI_LOKASI_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickShippingCartChangeAddressClickPinButtonFromTandaiLokasi() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_PIN_BUTTON_FROM_TANDAI_LOKASI
        )
    }

    fun eventViewShippingCartChangeAddressViewValidationErrorTandaiLokasi(errorMessage: String?) {
        sendEventCategoryActionLabel(
                ConstantTransactionAnalytics.EventName.VIEW_SHIPPING,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.VIEW_VALIDATION_ERROR_TANDAI_LOKASI,
                errorMessage
        )
    }

    fun eventClickShippingCartChangeAddressClickRadioButtonFromPilihAlamatLainnya() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_RADIO_BUTTON_FROM_PILIH_ALAMAT_LAINNYA
        )
    }

    fun eventClickCourierCartChangeAddressClickAlamatSebagaiPadaTambahAddress() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_ALAMAT_SEBAGAI_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickCourierCartChangeAddressClickChecklistAlamatSebagaiPadaTambahAddress() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_CHECKLIST_ALAMAT_SEBAGAI_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickCourierCartChangeAddressClickNamaPadaTambahAddress() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_NAMA_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickCourierCartChangeAddressClickTeleponPadaTambahAddress() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_TELEPON_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickCourierCartChangeAddressClickAlamatPadaTambahAddress() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_ALAMAT_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickCourierCartChangeAddressErrorValidationAlamatSebagaiPadaTambahAddress() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.VALIDATION_ERROR_ALAMAT_SEBAGAI_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickCourierCartChangeAddressErrorValidationNamaPadaTambahAddress() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.VALIDATION_ERROR_NAMA_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickCourierCartChangeAddressErrorValidationTeleponPadaTambahAddress() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.VALIDATION_ERROR_TELEPON_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickCourierCartChangeAddressErrorValidationKotaKecamatanPadaTambahAddress() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.VALIDATION_ERROR_KOTA_KECAMATAN_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickCourierCartChangeAddressErrorValidationKodePosPadaTambahAddress() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.VALIDATION_ERROR_KODE_POS_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickCourierCartChangeAddressErrorValidationAlamatPadaTambahAddress() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.VALIDATION_ERROR_ALAMAT_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickCourierCartChangeAddressErrorValidationAlamatSebagaiPadaTambahSuccess() {
        sendEventCategoryActionLabel(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_TAMBAH_ALAMAT_FROM_TAMBAH,
                ConstantTransactionAnalytics.EventLabel.SUCCESS
        )
    }

    fun eventClickCourierCartChangeAddressErrorValidationAlamatSebagaiPadaTambahNotSuccess() {
        sendEventCategoryActionLabel(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_TAMBAH_ALAMAT_FROM_TAMBAH,
                ConstantTransactionAnalytics.EventLabel.NOT_SUCCESS
        )
    }

    fun eventSearchResultNotFound(query: String) {
        sendEventCategoryActionLabel(
                ConstantTransactionAnalytics.EventName.CLICK_ADDRESS,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.SEARCH_NOT_FOUND,
                query
        )
    }

    //PHASE 2

    fun eventClickAddressCartChangeAddressCartChangeAddressSubmitSearchFromPilihAlamatLainnya() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_ADDRESS,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.SUBMIT_SEARCH_FROM_PILIH_ALAMAT_LAINNYA
        )
    }

    fun eventClickAddressCartChangeAddressClickButtonSimpanFromEditSuccess() {
        sendEventCategoryActionLabel(
                ConstantTransactionAnalytics.EventName.CLICK_ADDRESS,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_BUTTON_SIMPAN_FROM_EDIT,
                ConstantTransactionAnalytics.EventLabel.SUCCESS
        )
    }

    fun eventClickAddressCartChangeAddressClickButtonSimpanFromEditNotSuccess() {
        sendEventCategoryActionLabel(
                ConstantTransactionAnalytics.EventName.CLICK_ADDRESS,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_BUTTON_SIMPAN_FROM_EDIT,
                ConstantTransactionAnalytics.EventLabel.NOT_SUCCESS
        )
    }
}