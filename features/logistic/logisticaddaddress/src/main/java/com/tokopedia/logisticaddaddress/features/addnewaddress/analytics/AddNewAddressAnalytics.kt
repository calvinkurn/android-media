package com.tokopedia.logisticaddaddress.features.addnewaddress.analytics

import android.app.Activity
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * Created by fwidjaja on 2019-06-19.
 */
object AddNewAddressAnalytics {

    private const val SUCCESS = "success"
    private const val FAILED = "failed"
    private const val NOT_SUCCESS = "not success"
    private const val POSITIVE_SUCCESS = "positive success"
    private const val POSITIVE_NOT_SUCCESS = "positive not success"
    private const val NEGATIVE_SUCCESS = "negative success"
    private const val NEGATIVE_NOT_SUCCESS = "negative not success"
    private const val VIEW_ADDRESS = "viewAddress"
    private const val CLICK_ADDRESS = "clickAddress"
    private const val CART_CHANGE_ADDRESS = "cart change address"
    private const val CLICK_BUTTON_OK_ON_ALLOW_LOCATION = "click button ok on allow location"
    private const val CLICK_BUTTON_DO_NOT_ALLOW_ON_ALLOW_LOCATION = "click button do not allow on allow location"
    private const val CLICK_BUTTON_AKTIFKAN_LAYANAN_LOKASI_ON_BLOCK_GPS = "click button aktifkan layanan lokasi on block gps"
    private const val CLICK_BUTTON_X_ON_BLOCK_GPS = "click button x on block gps"
    private const val VIEW_ERROR_ALAMAT_TIDAK_VALID = "view error alamat tidak valid"
    private const val CLICK_BACK_ARROW_ON_INPUT_ADDRESS = "click back arrow on input address"
    private const val CLICK_FIELD_CARI_LOKASI = "click field cari lokasi"
    private const val CLICK_MAGNIFIER = "click magnifier"
    private const val CLICK_ADDRESS_SUGGESTION_FROM_SUGGESTION_LIST = "click address suggestion from suggestion list"
    private const val CLICK_BUTTON_PILIH_LOKASI = "click button pilih lokasi"
    private const val CLICK_FIELD_DETAIL_ALAMAT = "click field detail alamat"
    private const val CLICK_BACK_ARROW_ON_PIN_POINT = "click back arrow on pin point"
    private const val CLICK_BUTTON_PILIH_LOKASI_INI = "click button pilih lokasi ini"
    private const val CART_CHANGE_ADDRESS_POSITIVE = "cart change address positive"
    private const val CART_CHANGE_ADDRESS_NEGATIVE = "cart change address negative"
    private const val CLICK_FIELD_LABEL_ALAMAT = "click field label alamat"
    private const val CLICK_CHIPS_LABEL_ALAMAT = "click chips label alamat"
    private const val CLICK_FIELD_NAMA_PENERIMA = "click field nama penerima"
    private const val CLICK_FIELD_NO_PONSEL = "click field no ponsel"
    private const val CLICK_BUTTON_UBAH_PIN_POINT = "click button ubah pin point"
    private const val CLICK_BACK_ARROW_ON_POSITIVE_PAGE = "click back arrow on positive page"
    private const val CLICK_BUTTON_SIMPAN = "click button simpan"
    private const val VIEW_FAILED_PINPOINT_NOTIFICATION = "view failed pin point notification"
    private const val CLICK_BUTTON_TANDAI_LOKASI = "click button tandai lokasi"
    private const val CLICK_FIELD_KOTA_KECAMATAN = "click field kota/kecamatan"
    private const val CLICK_CHIPS_KOTA_KECAMATAN = "click chips kota/kecamatan"
    private const val CLICK_SUGGESTION_KOTA_KECAMATAN = "click suggestion kota/kecamatan"
    private const val CLICK_FIELD_KODE_POS = "click field kode pos"
    private const val CLICK_CHIPS_KODE_POS = "click chips kode pos"
    private const val CLICK_FIELD_ALAMAT = "click field alamat"
    private const val CLICK_BACK_ARROW_ON_NEGATIVE_PAGE = "click back arrow on negative page"
    private const val VIEW_TOASTER_PILIH_KOTA_DAN_KODE_POS_TERLEBIH_DAHULU = "view toaster pilih kota dan kode pos terlebih dahulu"
    private const val VIEW_TOASTER_ALAMAT_TIDAK_SESUAI_DENGAN_PETA = "view toaster alamat tidak sesuai dengan peta"

    @JvmStatic
    fun sendScreenName(activity: Activity, screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    private fun sendEventCategoryAction(event: String, eventCategory: String,
                                        eventAction: String) {
        sendEventCategoryActionLabel(event, eventCategory, eventAction, "")
    }

    private fun sendEventCategoryActionLabel(event: String, eventCategory: String,
                                              eventAction: String, eventLabel: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                event, eventCategory, eventAction, eventLabel))
    }

    fun eventClickButtonOkOnAllowLocation() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_BUTTON_OK_ON_ALLOW_LOCATION)
    }

    fun eventClickButtonDoNotAllowOnAllowLocation() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_BUTTON_DO_NOT_ALLOW_ON_ALLOW_LOCATION)
    }

    fun eventClickButtonAktifkanLayananLokasiOnBlockGps() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_BUTTON_AKTIFKAN_LAYANAN_LOKASI_ON_BLOCK_GPS)
    }

    fun eventClickButtonXOnBlockGps() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_BUTTON_X_ON_BLOCK_GPS)
    }

    fun eventViewErrorAlamatTidakValid() {
        sendEventCategoryAction(VIEW_ADDRESS, CART_CHANGE_ADDRESS, VIEW_ERROR_ALAMAT_TIDAK_VALID)
    }

    fun eventClickBackArrowOnInputAddress() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_BACK_ARROW_ON_INPUT_ADDRESS)
    }

    fun eventClickFieldCariLokasi() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_FIELD_CARI_LOKASI)
    }

    fun eventClickMagnifier() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_MAGNIFIER)
    }

    fun eventClickAddressSuggestionFromSuggestionList() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_ADDRESS_SUGGESTION_FROM_SUGGESTION_LIST)
    }

    fun eventClickButtonPilihLokasi() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_BUTTON_PILIH_LOKASI)
    }

    fun eventClickFieldDetailAlamat() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_FIELD_DETAIL_ALAMAT)
    }

    fun eventClickBackArrowOnPinPoint() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_BACK_ARROW_ON_PIN_POINT)
    }

    fun eventClickButtonPilihLokasiIniSuccess() {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_BUTTON_PILIH_LOKASI_INI, SUCCESS)
    }

    fun eventClickButtonPilihLokasiIniNotSuccess() {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_BUTTON_PILIH_LOKASI_INI, NOT_SUCCESS)
    }

    fun eventClickButtonPilihLokasiIni() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_BUTTON_PILIH_LOKASI_INI)
    }

    fun eventClickFieldDetailAlamatChangeAddressPositive() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS_POSITIVE, CLICK_FIELD_DETAIL_ALAMAT)
    }

    fun eventClickFieldLabelAlamatChangeAddressPositive() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS_POSITIVE, CLICK_FIELD_LABEL_ALAMAT)
    }

    fun eventClickChipsLabelAlamatChangeAddressPositive() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS_POSITIVE, CLICK_CHIPS_LABEL_ALAMAT)
    }

    fun eventClickFieldNamaPenerimaChangeAddressPositive() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS_POSITIVE, CLICK_FIELD_NAMA_PENERIMA)
    }

    fun eventClickFieldNoPonselChangeAddressPositive() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS_POSITIVE, CLICK_FIELD_NO_PONSEL)
    }

    fun eventClickButtonUbahPinPointChangeAddressPositive() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS_POSITIVE, CLICK_BUTTON_UBAH_PIN_POINT)
    }

    fun eventClickBackArrowOnPositivePageChangeAddressPositive() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS_POSITIVE, CLICK_BACK_ARROW_ON_POSITIVE_PAGE)
    }

    fun eventClickButtonSimpanSuccess() {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_BUTTON_SIMPAN, POSITIVE_SUCCESS)
    }

    fun eventClickButtonSimpanNotSuccess(errorField: String) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_BUTTON_SIMPAN, "$POSITIVE_NOT_SUCCESS - $errorField")
    }

    fun eventViewFailedPinPointNotification() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, VIEW_FAILED_PINPOINT_NOTIFICATION)
    }

    fun eventClickButtonTandaiLokasiChangeAddressNegativeSuccess() {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, CLICK_BUTTON_TANDAI_LOKASI, SUCCESS)
    }

    fun eventClickButtonTandaiLokasiChangeAddressNegativeFailed() {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, CLICK_BUTTON_TANDAI_LOKASI, FAILED)
    }

    fun eventClickFieldKotaKecamatanChangeAddressNegative() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, CLICK_FIELD_KOTA_KECAMATAN)
    }

    fun eventClickChipsKotaKecamatanChangeAddressNegative() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, CLICK_CHIPS_KOTA_KECAMATAN)
    }

    fun eventClickSuggestionKotaKecamatanChangeAddressNegative() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, CLICK_SUGGESTION_KOTA_KECAMATAN)
    }

    fun eventClickFieldKodePosChangeAddressNegative() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, CLICK_FIELD_KODE_POS)
    }

    fun eventClickChipsKodePosChangeAddressNegative() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, CLICK_CHIPS_KODE_POS)
    }

    fun eventClickFieldAlamatChangeAddressNegative() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, CLICK_FIELD_ALAMAT)
    }

    fun eventClickFieldLabelAlamatChangeAddressNegative() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, CLICK_FIELD_LABEL_ALAMAT)
    }

    fun eventClickChipsLabelAlamatChangeAddressNegative() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, CLICK_CHIPS_LABEL_ALAMAT)
    }

    fun eventClickFieldNamaPenerimaChangeAddressNegative() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, CLICK_FIELD_NAMA_PENERIMA)
    }

    fun eventClickFieldNoPonselChangeAddressNegative() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, CLICK_FIELD_NO_PONSEL)
    }

    fun eventViewToasterPilihKotaDanKodePosTerlebihDahulu() {
        sendEventCategoryAction(VIEW_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, VIEW_TOASTER_PILIH_KOTA_DAN_KODE_POS_TERLEBIH_DAHULU)
    }

    fun eventViewToasterAlamatTidakSesuaiDenganPeta() {
        sendEventCategoryAction(VIEW_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, VIEW_TOASTER_ALAMAT_TIDAK_SESUAI_DENGAN_PETA)
    }

    fun eventClickBackArrowOnNegativePage() {
        sendEventCategoryAction(CLICK_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, CLICK_BACK_ARROW_ON_NEGATIVE_PAGE)
    }

    fun eventClickButtonSimpanNegativeSuccess() {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_BUTTON_SIMPAN, NEGATIVE_SUCCESS)
    }

    fun eventClickButtonSimpanNegativeNotSuccess(errorField: String) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, CLICK_BUTTON_SIMPAN, "$NEGATIVE_NOT_SUCCESS - $errorField")
    }
}