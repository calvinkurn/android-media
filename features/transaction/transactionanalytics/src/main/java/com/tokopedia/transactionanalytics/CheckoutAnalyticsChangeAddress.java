package com.tokopedia.transactionanalytics;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import javax.inject.Inject;

import static com.tokopedia.transactionanalytics.ConstantTransactionAnalytics.EventAction;
import static com.tokopedia.transactionanalytics.ConstantTransactionAnalytics.EventCategory;
import static com.tokopedia.transactionanalytics.ConstantTransactionAnalytics.EventLabel;
import static com.tokopedia.transactionanalytics.ConstantTransactionAnalytics.EventName;


/**
 * @author anggaprasetiyo on 05/06/18.
 */
public class CheckoutAnalyticsChangeAddress extends TransactionAnalytics {
    @Inject
    public CheckoutAnalyticsChangeAddress(AnalyticTracker analyticTracker) {
        super(analyticTracker);
    }

    public void eventClickAtcCartChangeAddressClickPilihAlamatLainnyaFromGantiAlamat() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_PILIH_ALAMAT_LAINNYA_FROM_GANTI_ALAMAT
        );
    }

    public void eventClickAtcCartChangeAddressClickTambahAlamatBaruFromGantiAlamat() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_TAMBAH_ALAMAT_BARU_FROM_GANTI_ALAMAT
        );
    }

    public void eventClickAtcCartChangeAddressClickKirimKeBeberapaAlamatFromGantiAlamat() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_KIRIM_KE_BEBERAPA_ALAMAT_FROM_GANTI_ALAMAT
        );
    }

    public void eventClickAtcCartChangeAddressClickKirimKeAlamatIniFromGantiAlamat() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_KIRIM_KE_ALAMAT_INI_FROM_GANTI_ALAMAT
        );
    }

    public void eventClickAtcCartChangeAddressClickXFromPilihAlamatLainnya() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_X_FROM_PILIH_ALAMAT_LAINNYA
        );
    }

    public void eventClickAtcCartChangeAddressClickPlusFromPilihAlamatLainnya() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_PLUS_FROM_PILIH_ALAMAT_LAINNYA
        );
    }

    public void eventClickAtcCartChangeAddressClickUbahFromPilihAlamatLainnya() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_UBAH_FROM_PILIH_ALAMAT_LAINNYA
        );
    }

    public void eventClickAtcCartChangeAddressClickChecklistAlamatFromPilihAlamatLainnya() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_CHECKLIST_ALAMAT_FROM_PILIH_ALAMAT_LAINNYA
        );
    }

    public void eventViewAtcCartChangeAddressImpressionChangeAddress() {
        sendEventCategoryAction(
                EventName.VIEW_ATC,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.IMPRESSION_CHANGE_ADDRESS
        );
    }

    public void eventClickAtcCartChangeAddressCartChangeAddressSubmitSearchFromPilihAlamatLainnya() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.SUBMIT_SEARCH_FROM_PILIH_ALAMAT_LAINNYA
        );
    }

    public void eventClickAtcCartChangeAddressClickTambahAlamatFromPlus() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_TAMBAH_ALAMAT_FROM_PLUS
        );
    }

    public void eventClickAtcCartChangeAddressClickArrowBackFromGantiAlamat() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_ARROW_BACK_FROM_GANTI_ALAMAT
        );
    }

    public void eventClickAtcCartChangeAddressClickArrowBackFromPlus() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_ARROW_BACK_FROM_PLUS
        );
    }

    public void eventClickShippingCartChangeAddressClickPlusIconFromTujuanPengiriman() {
        sendEventCategoryAction(
                EventName.CLICK_SHIPPING,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_PLUS_ICON_FROM_TUJUAN_PENGIRIMAN
        );
    }

    public void eventClickShippingCartChangeAddressClickTambahFromAlamatPengiriman() {
        sendEventCategoryAction(
                EventName.CLICK_SHIPPING,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_TAMBAH_FROM_ALAMAT_PENGIRIMAN
        );
    }

    public void eventClickShippingCartChangeAddressClickSimpanFromTambahAlamat() {
        sendEventCategoryAction(
                EventName.CLICK_SHIPPING,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_SIMPAN_FROM_TAMBAH_ALAMAT
        );
    }

    public void eventClickShippingCartChangeAddressClickRadioButtonFromTujuanPengiriman() {
        sendEventCategoryAction(
                EventName.CLICK_SHIPPING,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_RADIO_BUTTON_FROM_TUJUAN_PENGIRIMAN
        );
    }

    public void eventClickShippingCartChangeAddressClickKotaAtauKecamatanPadaTambahAddress() {
        sendEventCategoryAction(
                EventName.CLICK_SHIPPING,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_KOTA_ATAU_KECAMATAN_PADA_TAMBAH_ADDRESS
        );
    }

    public void eventClickShippingCartChangeAddressClickKodePosPadaTambahAddress() {
        sendEventCategoryAction(
                EventName.CLICK_SHIPPING,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_KODE_POS_PADA_TAMBAH_ADDRESS
        );
    }

    public void eventClickShippingCartChangeAddressClickTandaiLokasiPadaTambahAddress() {
        sendEventCategoryAction(
                EventName.CLICK_SHIPPING,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_TANDAI_LOKASI_PADA_TAMBAH_ADDRESS
        );
    }

    public void eventClickShippingCartChangeAddressClickTambahAlamatFromTambah() {
        sendEventCategoryAction(
                EventName.CLICK_SHIPPING,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_TAMBAH_ALAMAT_FROM_TAMBAH
        );
    }

    public void eventClickShippingCartChangeAddressClickChecklistKotaAtauKecamatanPadaTambahAddress(String districtName) {
        sendEventCategoryActionLabel(
                EventName.CLICK_SHIPPING,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_CHECKLIST_KOTA_ATAU_KECAMATAN_PADA_TAMBAH_ADDRESS,
                districtName
        );
    }

    public void eventClickShippingCartChangeAddressClickXPojokKananKotaAtauKecamatanPadaTambahAddress() {
        sendEventCategoryAction(
                EventName.CLICK_SHIPPING,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_X_POJOK_KANAN_KOTA_ATAU_KECAMATAN_PADA_TAMBAH_ADDRESS
        );
    }

    public void eventClickShippingCartChangeAddressClickXPojokKiriKotaAtauKecamatanPadaTambahAddress() {
        sendEventCategoryAction(
                EventName.CLICK_SHIPPING,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_X_POJOK_KIRI_KOTA_ATAU_KECAMATAN_PADA_TAMBAH_ADDRESS
        );
    }

    public void eventClickShippingCartChangeAddressClickChecklistKodePosPAdaTambahAddress() {
        sendEventCategoryAction(
                EventName.CLICK_SHIPPING,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_CHECKLIST_KODE_POS_PADA_TAMBAH_ADDRESS
        );
    }

    public void eventClickShippingCartChangeAddressClickFillKodePosPadaTambahAddress(String eventLabel) {
        sendEventCategoryActionLabel(
                EventName.CLICK_SHIPPING,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_FILL_KODE_POS_PADA_TAMBAH_ADDRESS,
                eventLabel
        );
    }

    public void eventViewShippingCartChangeAddressViewValidationErrorNotFill(String errorMessage) {
        sendEventCategoryActionLabel(
                EventName.VIEW_SHIPPING,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.VIEW_VALIDATION_ERROR_NOT_FILL,
                errorMessage
        );
    }

    public void eventClickShippingCartChangeAddressClickDropdownSuggestionTandaiLokasiPadaTambahAddress() {
        sendEventCategoryAction(
                EventName.CLICK_SHIPPING,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_DROPDOWN_SUGGESTION_TANDAI_LOKASI_PADA_TAMBAH_ADDRESS
        );
    }

    public void eventClickShippingCartChangeAddressClickVTandaiLokasiPadaTambahAddress() {
        sendEventCategoryAction(
                EventName.CLICK_SHIPPING,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_V_TANDAI_LOKASI_PADA_TAMBAH_ADDRESS
        );
    }

    public void eventClickShippingCartChangeAddressClickBackArrowTandaiLokasiPadaTambahAddress() {
        sendEventCategoryAction(
                EventName.CLICK_SHIPPING,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_BACK_ARROW_TANDAI_LOKASI_PADA_TAMBAH_ADDRESS
        );
    }

    public void eventClickShippingCartChangeAddressClickPinButtonFromTandaiLokasi() {
        sendEventCategoryAction(
                EventName.CLICK_SHIPPING,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_PIN_BUTTON_FROM_TANDAI_LOKASI
        );
    }

    public void eventViewShippingCartChangeAddressViewValidationErrorTandaiLokasi(String errorMessage) {
        sendEventCategoryActionLabel(
                EventName.VIEW_SHIPPING,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.VIEW_VALIDATION_ERROR_TANDAI_LOKASI,
                errorMessage
        );
    }

    public void eventClickShippingCartChangeAddressClickRadioButtonFromPilihAlamatLainnya() {
        sendEventCategoryAction(
                EventName.CLICK_SHIPPING,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_RADIO_BUTTON_FROM_PILIH_ALAMAT_LAINNYA
        );
    }

    public void eventClickCourierCartChangeAddressClickAlamatSebagaiPadaTambahAddress() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_ALAMAT_SEBAGAI_PADA_TAMBAH_ADDRESS
        );
    }

    public void eventClickCourierCartChangeAddressClickChecklistAlamatSebagaiPadaTambahAddress() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_CHECKLIST_ALAMAT_SEBAGAI_PADA_TAMBAH_ADDRESS
        );
    }

    public void eventClickCourierCartChangeAddressClickNamaPadaTambahAddress() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_NAMA_PADA_TAMBAH_ADDRESS
        );
    }

    public void eventClickCourierCartChangeAddressClickTeleponPadaTambahAddress() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_TELEPON_PADA_TAMBAH_ADDRESS
        );
    }

    public void eventClickCourierCartChangeAddressClickAlamatPadaTambahAddress() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_ALAMAT_PADA_TAMBAH_ADDRESS
        );
    }


    public void eventClickCourierCartChangeAddressErrorValidationAlamatSebagaiPadaTambahAddress() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.VALIDATION_ERROR_ALAMAT_SEBAGAI_PADA_TAMBAH_ADDRESS
        );
    }

    public void eventClickCourierCartChangeAddressErrorValidationNamaPadaTambahAddress() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.VALIDATION_ERROR_NAMA_PADA_TAMBAH_ADDRESS
        );
    }

    public void eventClickCourierCartChangeAddressErrorValidationTeleponPadaTambahAddress() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.VALIDATION_ERROR_TELEPON_PADA_TAMBAH_ADDRESS
        );
    }

    public void eventClickCourierCartChangeAddressErrorValidationKotaKecamatanPadaTambahAddress() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.VALIDATION_ERROR_KOTA_KECAMATAN_PADA_TAMBAH_ADDRESS
        );
    }

    public void eventClickCourierCartChangeAddressErrorValidationKodePosPadaTambahAddress() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.VALIDATION_ERROR_KODE_POS_PADA_TAMBAH_ADDRESS
        );
    }

    public void eventClickCourierCartChangeAddressErrorValidationAlamatPadaTambahAddress() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.VALIDATION_ERROR_ALAMAT_PADA_TAMBAH_ADDRESS
        );
    }

    public void eventClickCourierCartChangeAddressErrorValidationAlamatSebagaiPadaTambahSuccess() {
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_TAMBAH_ALAMAT_FROM_TAMBAH,
                EventLabel.SUCCESS
        );
    }

    public void eventClickCourierCartChangeAddressErrorValidationAlamatSebagaiPadaTambahNotSuccess() {
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_TAMBAH_ALAMAT_FROM_TAMBAH,
                EventLabel.NOT_SUCCESS
        );
    }


    //PHASE 2

    public void eventClickAddressCartChangeAddressClickTambahFromTambahAlamatBaruSuccess() {
        sendEventCategoryActionLabel(
                EventName.CLICK_ADDRESS,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_TAMBAH_FROM_TAMBAH_ALAMAT_BARU,
                EventLabel.SUCCESS
        );
    }

    public void eventClickAddressCartChangeAddressClickTambahFromTambahAlamatBaruFailed() {
        sendEventCategoryActionLabel(
                EventName.CLICK_ADDRESS,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CLICK_TAMBAH_FROM_TAMBAH_ALAMAT_BARU,
                EventLabel.FAILED
        );
    }


    public void eventClickAddressCartChangeAddressCartChangeAddressSubmitSearchFromPilihAlamatLainnya() {
        sendEventCategoryAction(
                EventName.CLICK_ADDRESS,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.SUBMIT_SEARCH_FROM_PILIH_ALAMAT_LAINNYA
        );
    }

}
