package com.tokopedia.logisticCommon.data.analytics

import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object ShareAddressAnalytics : BaseTrackerConst() {
    private const val BUSINESS_UNIT_LOGISTIC = "logistic"
    private const val EVENT_CLICK_LOGISTIC = "clickLogistic"

    private const val CATEGORY_ADDRESS_LIST_PAGE = "address list page"
    private const val CATEGORY_BOTTOM_SHEET_INSERTING_IDENTITY = "bottom sheet inserting identity"
    private const val CATEGORY_CONSENT_BOTTOM_SHEET = "consent bottom sheet"

    private const val ACTION_CLICKING_TAB_UTAMA = "clicking tab utama"
    private const val ACTION_CLICKING_TAB_FROM_FRIEND = "clicking tab dari teman"
    private const val ACTION_CLICKING_SAVE_BUTTON = "clicking simpan button"
    private const val ACTION_CLICKING_DELETE_BUTTON = "clicking hapus button"
    private const val ACTION_CHOOSE_ONE_OF_ADDRESS_LIST = "choosing one of the address in address list"
    private const val ACTION_CLICKING_KIRIM_BUTTON = "clicking Kirim button"
    private const val ACTION_DIRECT_SHARE_USERS_CLICKING_KIRIM_BUTTON = "direct share - clicking Kirim button"
    private const val ACTION_CLICKING_PHONE_BOOK_TO_GET_CONTACT = "clicking phone book to get contact"
    private const val ACTION_DIRECT_SHARE_CLICKING_PHONE_BOOK_TO_GET_CONTACT = "direct share - clicking phone book to get contact"
    private const val ACTION_AGREE_TO_SEND_ADDRESS = "agreeing to send the address by click yes"
    private const val ACTION_DISAGREE_TO_SEND_ADDRESS = "disagreeing to send the address by click no"
    private const val ACTION_DIRECT_SHARE_AGREE_TO_SEND_ADDRESS = "direct share - agreeing to send the address by click yes"
    private const val ACTION_DIRECT_SHARE_DISAGREE_TO_SEND_ADDRESS = "direct share - disagreeing to send the address by click no"
    private const val ACTION_CLICKING_REQUEST_TO_FRIEND_BUTTON = "clicking request ke teman kamu button"
    private const val ACTION_CLICKING_SHARE_ADDRESS_BUTTON = "clicking bagikan alamat button"
    private const val ACTION_DIRECT_SHARE_CLICKING_SHARE_BUTTON = "direct share - clicking share button"
    private const val ACTION_CLICKING_SELECT_ALL_BUTTON = "clicking select all button"

    private const val LABEL_SUCCESS = "success"
    private const val LABEL_NOT_SUCCESS = "not success"
    private const val LABEL_CHECK = "check"
    private const val LABEL_UNCHECK = "uncheck"

    fun onClickMainTab() {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK_LOGISTIC)
                .appendEventCategory(CATEGORY_ADDRESS_LIST_PAGE)
                .appendEventAction(ACTION_CLICKING_TAB_UTAMA)
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .build()
        )
    }

    fun onClickFromFriendTab() {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK_LOGISTIC)
                .appendEventCategory(CATEGORY_ADDRESS_LIST_PAGE)
                .appendEventAction(ACTION_CLICKING_TAB_FROM_FRIEND)
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .build()
        )
    }

    fun onClickPhoneBookToGetContact(isRequestAddress: Boolean) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK_LOGISTIC)
                .appendEventCategory(CATEGORY_BOTTOM_SHEET_INSERTING_IDENTITY)
                .appendEventAction(
                    if (isRequestAddress) {
                        ACTION_CLICKING_PHONE_BOOK_TO_GET_CONTACT
                    } else {
                        ACTION_DIRECT_SHARE_CLICKING_PHONE_BOOK_TO_GET_CONTACT
                    }
                )
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .build()
        )
    }

    fun onClickBottomSheetSendButton(isRequestAddress: Boolean, isSuccess: Boolean) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK_LOGISTIC)
                .appendEventCategory(CATEGORY_BOTTOM_SHEET_INSERTING_IDENTITY)
                .appendEventAction(
                    if (isRequestAddress) {
                        ACTION_CLICKING_KIRIM_BUTTON
                    } else {
                        ACTION_DIRECT_SHARE_USERS_CLICKING_KIRIM_BUTTON
                    }
                )
                .appendEventLabel(if (isSuccess) LABEL_SUCCESS else LABEL_NOT_SUCCESS)
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .build()
        )
    }

    fun onClickSaveButton(isSuccess: Boolean) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK_LOGISTIC)
                .appendEventCategory(CATEGORY_ADDRESS_LIST_PAGE)
                .appendEventAction(ACTION_CLICKING_SAVE_BUTTON)
                .appendEventLabel(
                    if (isSuccess) {
                        LABEL_SUCCESS
                    } else {
                        LABEL_NOT_SUCCESS
                    }
                )
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .build()
        )
    }

    fun onClickDeleteButton(isSuccess: Boolean) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK_LOGISTIC)
                .appendEventCategory(CATEGORY_ADDRESS_LIST_PAGE)
                .appendEventAction(ACTION_CLICKING_DELETE_BUTTON)
                .appendEventLabel(
                    if (isSuccess) {
                        LABEL_SUCCESS
                    } else {
                        LABEL_NOT_SUCCESS
                    }
                )
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .build()
        )
    }

    fun onChooseAddressList() {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK_LOGISTIC)
                .appendEventCategory(CATEGORY_ADDRESS_LIST_PAGE)
                .appendEventAction(ACTION_CHOOSE_ONE_OF_ADDRESS_LIST)
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .build()
        )
    }

    fun onAgreeSendAddress(isDirectShare: Boolean, isSuccess: Boolean = false) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK_LOGISTIC)
                .appendEventCategory(CATEGORY_CONSENT_BOTTOM_SHEET)
                .appendEventAction(
                    if (isDirectShare) {
                        ACTION_DIRECT_SHARE_AGREE_TO_SEND_ADDRESS
                    } else {
                        ACTION_AGREE_TO_SEND_ADDRESS
                    }
                )
                .appendEventLabel(
                    if (isSuccess) {
                        LABEL_SUCCESS
                    } else {
                        LABEL_NOT_SUCCESS
                    }
                )
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .build()
        )
    }

    fun onDisagreeSendAddress(isDirectShare: Boolean, isSuccess: Boolean = false) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK_LOGISTIC)
                .appendEventCategory(CATEGORY_CONSENT_BOTTOM_SHEET)
                .appendEventAction(
                    if (isDirectShare) {
                        ACTION_DIRECT_SHARE_DISAGREE_TO_SEND_ADDRESS
                    } else {
                        ACTION_DISAGREE_TO_SEND_ADDRESS
                    }
                )
                .appendEventLabel(
                    if (isDirectShare) {
                        ""
                    } else {
                        if (isSuccess) {
                            LABEL_SUCCESS
                        } else {
                            LABEL_NOT_SUCCESS
                        }
                    }
                )
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .build()
        )
    }

    fun onClickRequestAddress() {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK_LOGISTIC)
                .appendEventCategory(CATEGORY_ADDRESS_LIST_PAGE)
                .appendEventAction(ACTION_CLICKING_REQUEST_TO_FRIEND_BUTTON)
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .build()
        )
    }

    fun onClickShareAddress() {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK_LOGISTIC)
                .appendEventCategory(CATEGORY_ADDRESS_LIST_PAGE)
                .appendEventAction(ACTION_CLICKING_SHARE_ADDRESS_BUTTON)
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .build()
        )
    }

    fun onClickDirectShareButton() {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK_LOGISTIC)
                .appendEventCategory(CATEGORY_ADDRESS_LIST_PAGE)
                .appendEventAction(ACTION_DIRECT_SHARE_CLICKING_SHARE_BUTTON)
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .build()
        )
    }

    fun onCheckAllAddress(isChecked: Boolean) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK_LOGISTIC)
                .appendEventCategory(CATEGORY_ADDRESS_LIST_PAGE)
                .appendEventAction(ACTION_CLICKING_SELECT_ALL_BUTTON)
                .appendEventLabel(
                    if(isChecked) {
                        LABEL_CHECK
                    } else {
                        LABEL_UNCHECK
                    }
                )
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .build()
        )
    }
}
