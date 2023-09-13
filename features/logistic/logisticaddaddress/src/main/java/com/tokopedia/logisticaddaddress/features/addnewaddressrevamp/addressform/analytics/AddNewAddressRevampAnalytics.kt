package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform.analytics

import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object AddNewAddressRevampAnalytics : BaseTrackerConst() {

    private const val CLICK_ADDRESS = "clickAddress"
    private const val CLICK_BACK_ARROW = "click back arrow on top left corner"
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
    private const val CLICK_FIELD_KOTA_KECAMATAN = "click field kota dan kecamatan"
    private const val CLICK_ATUR_PINPOINT = "click atur pinpoint"

    private const val ANA_POSITIVE = "add new address positive"
    private const val ANA_NEGATIVE = "add new address negative"

    private const val BUSINESS_UNIT_LOGISTIC = "logistics & fulfillment"

    /*ANA Positive*/
    fun onClickFieldLabelAlamatPositive(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_POSITIVE)
                .appendEventAction(CLICK_FIELD_LABEL_ALAMAT)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickChipsLabelAlamatPositive(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_POSITIVE)
                .appendEventAction(CLICK_CHIPS_LABEL_ALAMAT)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickFieldAlamatPositive(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_POSITIVE)
                .appendEventAction(CLICK_FIELD_ALAMAT)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickFieldCatatanKurirPositive(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_POSITIVE)
                .appendEventAction(CLICK_FIELD_CATATAN_KURIR)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickFieldNamaPenerimaPositive(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_POSITIVE)
                .appendEventAction(CLICK_FIELD_NAMA_PENERIMA)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickIconNamaPenerimaPositive(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_POSITIVE)
                .appendEventAction(CLICK_ICON_I_NAMA_PENERIMA)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickFieldNomorHpPositive(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_POSITIVE)
                .appendEventAction(CLICK_FIELD_NO_HP)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickIconPhoneBookPositive(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_POSITIVE)
                .appendEventAction(CLICK_PHONE_BOOK_ICON)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickBoxJadikanAlamatUtamaPositive(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_POSITIVE)
                .appendEventAction(CLICK_BOX_JADIKAN_ALAMAT_UTAMA)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickSimpanPositive(userId: String, eventLabel: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_POSITIVE)
                .appendEventAction(CLICK_SIMPAN)
                .appendEventLabel(eventLabel)
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickSimpanErrorPositive(userId: String, errorField: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_POSITIVE)
                .appendEventAction(CLICK_SIMPAN_ERROR)
                .appendEventLabel(errorField)
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickBackPositive(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_POSITIVE)
                .appendEventAction(CLICK_BACK_ARROW)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    /*ANA Negative*/
    fun onClickFieldLabelAlamatNegative(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_NEGATIVE)
                .appendEventAction(CLICK_FIELD_LABEL_ALAMAT)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickFieldAlamatNegative(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_NEGATIVE)
                .appendEventAction(CLICK_FIELD_ALAMAT)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickFieldCatatanKurirNegative(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_NEGATIVE)
                .appendEventAction(CLICK_FIELD_CATATAN_KURIR)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickFieldNamaPenerimaNegative(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_NEGATIVE)
                .appendEventAction(CLICK_FIELD_NAMA_PENERIMA)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickIconNamaPenerimaNegative(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_NEGATIVE)
                .appendEventAction(CLICK_ICON_I_NAMA_PENERIMA)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickFieldNomorHpNegative(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_NEGATIVE)
                .appendEventAction(CLICK_FIELD_NO_HP)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickIconPhoneBookNegative(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_NEGATIVE)
                .appendEventAction(CLICK_PHONE_BOOK_ICON)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickBoxJadikanAlamatUtamaNegative(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_NEGATIVE)
                .appendEventAction(CLICK_BOX_JADIKAN_ALAMAT_UTAMA)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickSimpanNegative(userId: String, eventLabel: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_NEGATIVE)
                .appendEventAction(CLICK_SIMPAN)
                .appendEventLabel(eventLabel)
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickSimpanErrorNegative(userId: String, errorField: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_NEGATIVE)
                .appendEventAction(CLICK_SIMPAN_ERROR)
                .appendEventLabel(errorField)
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickBackNegative(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_NEGATIVE)
                .appendEventAction(CLICK_BACK_ARROW)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickFieldKotaKecamatanNegative(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_NEGATIVE)
                .appendEventAction(CLICK_FIELD_KOTA_KECAMATAN)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickAturPinpointNegative(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_ADDRESS)
                .appendEventCategory(ANA_NEGATIVE)
                .appendEventAction(CLICK_ATUR_PINPOINT)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }
}
