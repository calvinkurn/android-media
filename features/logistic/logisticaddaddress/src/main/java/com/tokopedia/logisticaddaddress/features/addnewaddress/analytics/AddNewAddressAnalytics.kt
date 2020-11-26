package com.tokopedia.logisticaddaddress.features.addnewaddress.analytics

import android.app.Activity
import com.tokopedia.logisticaddaddress.common.AddressConstants.LOGISTIC_LABEL
import com.tokopedia.logisticaddaddress.common.AddressConstants.NON_LOGISTIC_LABEL
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
    private const val CLICK_PILIH_LOKASI_UNNAMED_ROAD = "click pilih lokasi unnamed road"
    private const val CLICK_FIELD_KODE_POS = "click field kode pos"
    private const val CLICK_CHIPS_KODE_POS = "click chips kode pos"
    private const val CLICK_FIELD_ALAMAT = "click field alamat"
    private const val CLICK_BACK_ARROW_ON_NEGATIVE_PAGE = "click back arrow on negative page"
    private const val VIEW_TOASTER_PILIH_KOTA_DAN_KODE_POS_TERLEBIH_DAHULU = "view toaster pilih kota dan kode pos terlebih dahulu"
    private const val VIEW_TOASTER_ALAMAT_TIDAK_SESUAI_DENGAN_PETA = "view toaster alamat tidak sesuai dengan peta"

    @JvmStatic
    fun sendScreenName(screenName: String) {
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

    fun eventClickButtonOkOnAllowLocation(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_BUTTON_OK_ON_ALLOW_LOCATION, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickButtonDoNotAllowOnAllowLocation(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_BUTTON_DO_NOT_ALLOW_ON_ALLOW_LOCATION, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickButtonAktifkanLayananLokasiOnBlockGps(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_BUTTON_AKTIFKAN_LAYANAN_LOKASI_ON_BLOCK_GPS, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickButtonXOnBlockGps(isFullFlow: Boolean, isLogisticLabel : Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_BUTTON_X_ON_BLOCK_GPS, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventViewErrorAlamatTidakValid(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(VIEW_ADDRESS, CART_CHANGE_ADDRESS, VIEW_ERROR_ALAMAT_TIDAK_VALID, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickBackArrowOnInputAddress(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_BACK_ARROW_ON_INPUT_ADDRESS, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickFieldCariLokasi(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_FIELD_CARI_LOKASI, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickMagnifier(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_MAGNIFIER, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickAddressSuggestionFromSuggestionList(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_ADDRESS_SUGGESTION_FROM_SUGGESTION_LIST, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickButtonPilihLokasi(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_BUTTON_PILIH_LOKASI, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickFieldDetailAlamat(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_FIELD_DETAIL_ALAMAT, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickBackArrowOnPinPoint(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_BACK_ARROW_ON_PIN_POINT, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickButtonPilihLokasiIniSuccess(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_BUTTON_PILIH_LOKASI_INI, "$SUCCESS - ${getAnalyticsLabel(isFullFlow, isLogisticLabel)}")
    }

    fun eventClickButtonPilihLokasiIniNotSuccess(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_BUTTON_PILIH_LOKASI_INI, "$NOT_SUCCESS - ${getAnalyticsLabel(isFullFlow, isLogisticLabel)}")
    }

    fun eventClickButtonPilihLokasiIni(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_BUTTON_PILIH_LOKASI_INI, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickFieldDetailAlamatChangeAddressPositive(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS_POSITIVE, CLICK_FIELD_DETAIL_ALAMAT, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickFieldLabelAlamatChangeAddressPositive(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS_POSITIVE, CLICK_FIELD_LABEL_ALAMAT, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickChipsLabelAlamatChangeAddressPositive(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS_POSITIVE, CLICK_CHIPS_LABEL_ALAMAT, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickFieldNamaPenerimaChangeAddressPositive(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS_POSITIVE, CLICK_FIELD_NAMA_PENERIMA, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickFieldNoPonselChangeAddressPositive(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS_POSITIVE, CLICK_FIELD_NO_PONSEL, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickButtonUbahPinPointChangeAddressPositive(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS_POSITIVE, CLICK_BUTTON_UBAH_PIN_POINT, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickButtonUnnamedRoad(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(VIEW_ADDRESS, CART_CHANGE_ADDRESS, CLICK_PILIH_LOKASI_UNNAMED_ROAD, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickBackArrowOnPositivePageChangeAddressPositive(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS_POSITIVE, CLICK_BACK_ARROW_ON_POSITIVE_PAGE, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickButtonSimpanSuccess(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_BUTTON_SIMPAN, "$POSITIVE_SUCCESS - ${getAnalyticsLabel(isFullFlow, isLogisticLabel)}")
    }

    fun eventClickButtonSimpanNotSuccess(errorField: String, isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_BUTTON_SIMPAN, "$POSITIVE_NOT_SUCCESS - $errorField - ${getAnalyticsLabel(isFullFlow, isLogisticLabel)}")
    }

    fun eventViewFailedPinPointNotification(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, VIEW_FAILED_PINPOINT_NOTIFICATION, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickButtonTandaiLokasiChangeAddressNegativeSuccess(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, CLICK_BUTTON_TANDAI_LOKASI, "$SUCCESS - ${getAnalyticsLabel(isFullFlow, isLogisticLabel)}")
    }

    fun eventClickButtonTandaiLokasiChangeAddressNegativeFailed(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, CLICK_BUTTON_TANDAI_LOKASI, "$FAILED - ${getAnalyticsLabel(isFullFlow, isLogisticLabel)}")
    }

    fun eventClickFieldKotaKecamatanChangeAddressNegative(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, CLICK_FIELD_KOTA_KECAMATAN, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickChipsKotaKecamatanChangeAddressNegative(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, CLICK_CHIPS_KOTA_KECAMATAN, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickSuggestionKotaKecamatanChangeAddressNegative(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, CLICK_SUGGESTION_KOTA_KECAMATAN, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickFieldKodePosChangeAddressNegative(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, CLICK_FIELD_KODE_POS, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickChipsKodePosChangeAddressNegative(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, CLICK_CHIPS_KODE_POS, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickFieldAlamatChangeAddressNegative(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, CLICK_FIELD_ALAMAT, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickFieldLabelAlamatChangeAddressNegative(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, CLICK_FIELD_LABEL_ALAMAT, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickChipsLabelAlamatChangeAddressNegative(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, CLICK_CHIPS_LABEL_ALAMAT, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickFieldNamaPenerimaChangeAddressNegative(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, CLICK_FIELD_NAMA_PENERIMA, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickFieldNoPonselChangeAddressNegative(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, CLICK_FIELD_NO_PONSEL, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventViewToasterPilihKotaDanKodePosTerlebihDahulu(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(VIEW_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, VIEW_TOASTER_PILIH_KOTA_DAN_KODE_POS_TERLEBIH_DAHULU, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventViewToasterAlamatTidakSesuaiDenganPeta(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(VIEW_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, VIEW_TOASTER_ALAMAT_TIDAK_SESUAI_DENGAN_PETA, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickBackArrowOnNegativePage(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, CLICK_BACK_ARROW_ON_NEGATIVE_PAGE, getAnalyticsLabel(isFullFlow, isLogisticLabel))
    }

    fun eventClickButtonSimpanNegativeSuccess(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS, CLICK_BUTTON_SIMPAN, "$NEGATIVE_SUCCESS - ${getAnalyticsLabel(isFullFlow, isLogisticLabel)}")
    }

    fun eventClickButtonSimpanNegativeNotSuccess(errorField: String, isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(CLICK_ADDRESS, CART_CHANGE_ADDRESS_NEGATIVE, CLICK_BUTTON_SIMPAN, "$NEGATIVE_NOT_SUCCESS - $errorField - ${getAnalyticsLabel(isFullFlow, isLogisticLabel)}")
    }

    private fun getAnalyticsLabel(flow: Boolean, logistic: Boolean) = if (flow && logistic) LOGISTIC_LABEL else NON_LOGISTIC_LABEL

}