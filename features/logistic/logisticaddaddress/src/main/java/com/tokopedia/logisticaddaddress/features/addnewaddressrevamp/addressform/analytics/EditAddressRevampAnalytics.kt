package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object EditAddressRevampAnalytics : BaseTrackerConst() {

    //    event name
    private const val EVENT_VIEW = "viewLogisticIris"
    private const val EVENT_CLICK = "clickLogistic"

    //    event label
    private const val LABEL_SUCCESS = "success"
    private const val LABEL_NOT_SUCCESS = "not success"

    //    event action
    private const val ACTION_VIEW_EDIT_ADDRESS_NEW = "view edit address page new"
    private const val ACTION_CLICK_BUTTON_SIMPAN = "click button simpan - edit address new"
    private const val ACTION_CLICK_ATUR_PINPOINT = "click atur pinpoint"
    private const val ACTION_CLICK_LABEL_ALAMAT = "click field label alamat"
    private const val ACTION_CLICK_CHIPS_LABEL_ALAMAT = "click chips label alamat"
    private const val ACTION_CLICK_FIELD_ALAMAT = "click field alamat"
    private const val ACTION_CLICK_FIELD_CATATAN_KURIR = "click field catatan untuk kurir"
    private const val ACTION_CLICK_FIELD_NAMA_PENERIMA = "click field nama penerima"
    private const val ACTION_CLICK_FIELD_NO_HP = "click field nomor hp"
    private const val ACTION_CLICK_PHONE_BOOK_ICON = "click phone book icon"
    private const val ACTION_CLICK_BACK_ARROW = "click back arrow on top left corner"
    private const val ACTION_CLICK_SIMPAN_ERROR = "click simpan button, error in 1 or more field"
    private const val ACTION_CLICK_FIELD_KOTA_KECAMATAN = "click field kota & kecamatan"

    //    event category
    const val CATEGORY_EDIT_ADDRESS_PAGE = "edit address page"

    //    business unit
    private const val BUSINESS_UNIT_LOGISTIC = "logistic"

    @JvmStatic
    fun sendScreenName(screenName: String?) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    fun onViewEditAddressPageNew(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_VIEW)
                .appendEventCategory(CATEGORY_EDIT_ADDRESS_PAGE)
                .appendEventAction(ACTION_VIEW_EDIT_ADDRESS_NEW)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickButtonSimpan(userId: String, success: Boolean) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK)
                .appendEventCategory(CATEGORY_EDIT_ADDRESS_PAGE)
                .appendEventAction(ACTION_CLICK_BUTTON_SIMPAN)
                .appendEventLabel(if (success) LABEL_SUCCESS else LABEL_NOT_SUCCESS)
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickAturPinPoint(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK)
                .appendEventCategory(CATEGORY_EDIT_ADDRESS_PAGE)
                .appendEventAction(ACTION_CLICK_ATUR_PINPOINT)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickFieldLabelAlamat(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK)
                .appendEventCategory(CATEGORY_EDIT_ADDRESS_PAGE)
                .appendEventAction(ACTION_CLICK_LABEL_ALAMAT)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickChipsLabelAlamat(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK)
                .appendEventCategory(CATEGORY_EDIT_ADDRESS_PAGE)
                .appendEventAction(ACTION_CLICK_CHIPS_LABEL_ALAMAT)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickFieldAlamat(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK)
                .appendEventCategory(CATEGORY_EDIT_ADDRESS_PAGE)
                .appendEventAction(ACTION_CLICK_FIELD_ALAMAT)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickFieldCatatanKurir(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK)
                .appendEventCategory(CATEGORY_EDIT_ADDRESS_PAGE)
                .appendEventAction(ACTION_CLICK_FIELD_CATATAN_KURIR)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickFieldNamaPenerima(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK)
                .appendEventCategory(CATEGORY_EDIT_ADDRESS_PAGE)
                .appendEventAction(ACTION_CLICK_FIELD_NAMA_PENERIMA)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickFieldNomorHp(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK)
                .appendEventCategory(CATEGORY_EDIT_ADDRESS_PAGE)
                .appendEventAction(ACTION_CLICK_FIELD_NO_HP)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickIconPhoneBook(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK)
                .appendEventCategory(CATEGORY_EDIT_ADDRESS_PAGE)
                .appendEventAction(ACTION_CLICK_PHONE_BOOK_ICON)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickBackArrowEditAddress(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK)
                .appendEventCategory(CATEGORY_EDIT_ADDRESS_PAGE)
                .appendEventAction(ACTION_CLICK_BACK_ARROW)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickSimpanError(userId: String, errorField: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK)
                .appendEventCategory(CATEGORY_EDIT_ADDRESS_PAGE)
                .appendEventAction(ACTION_CLICK_SIMPAN_ERROR)
                .appendEventLabel(errorField)
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }

    fun onClickFieldKotaKecamatan(userId: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK)
                .appendEventCategory(CATEGORY_EDIT_ADDRESS_PAGE)
                .appendEventAction(ACTION_CLICK_FIELD_KOTA_KECAMATAN)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
        )
    }
}
