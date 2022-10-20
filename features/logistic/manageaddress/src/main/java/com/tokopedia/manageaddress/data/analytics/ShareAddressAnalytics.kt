package com.tokopedia.manageaddress.data.analytics

import com.tokopedia.track.builder.Tracker
import com.tokopedia.track.builder.util.BaseTrackerConst

object ShareAddressAnalytics : BaseTrackerConst() {
    private const val BUSINESS_UNIT_LOGISTIC = "logistic"
    private const val EVENT_CLICK_LOGISTIC = "clickLogistic"

    private const val KEY_TRACKER_ID = "trackerId"

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

    private const val TRACKER_ID_CLICKING_TAB_UTAMA = "36673"
    private const val TRACKER_ID_CLICKING_TAB_FROM_FRIEND = "36674"
    private const val TRACKER_ID_CLICKING_PHONE_BOOK_TO_GET_CONTACT = "36675"
    private const val TRACKER_ID_CLICKING_KIRIM_BUTTON = "36676"
    private const val TRACKER_ID_CLICKING_SAVE_BUTTON = "36679"
    private const val TRACKER_ID_CHOOSE_ONE_OF_ADDRESS_LIST = "36680"
    private const val TRACKER_ID_AGREE_TO_SEND_ADDRESS = "36681"
    private const val TRACKER_ID_DISAGREE_TO_SEND_ADDRESS = "36682"
    private const val TRACKER_ID_CLICKING_DELETE_BUTTON = "36683"
    private const val TRACKER_ID_CLICKING_REQUEST_TO_FRIEND_BUTTON = "37108"
    private const val TRACKER_ID_CLICKING_SHARE_ADDRESS_BUTTON = "37111"
    private const val TRACKER_ID_DIRECT_SHARE_CLICKING_SHARE_BUTTON = "37170"
    private const val TRACKER_ID_DIRECT_SHARE_CLICKING_PHONE_BOOK_TO_GET_CONTACT = "37171"
    private const val TRACKER_ID_DIRECT_SHARE_USERS_CLICKING_KIRIM_BUTTON = "37172"
    private const val TRACKER_ID_DIRECT_SHARE_AGREE_TO_SEND_ADDRESS = "37173"
    private const val TRACKER_ID_DIRECT_SHARE_DISAGREE_TO_SEND_ADDRESS = "37174"
    private const val TRACKER_ID_CLICKING_SELECT_ALL_BUTTON = "37197"

    private fun sendTracker(
        action: String,
        category: String,
        label: String,
        trackerId: String,
    ) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_LOGISTIC)
            .setEventAction(action)
            .setEventCategory(category)
            .setEventLabel(label)
            .setCustomProperty(KEY_TRACKER_ID, trackerId)
            .setBusinessUnit(BUSINESS_UNIT_LOGISTIC)
            .setCurrentSite(CurrentSite.DEFAULT)
            .build()
            .send()
    }

    fun onClickMainTab() {
        sendTracker(
            action = ACTION_CLICKING_TAB_UTAMA,
            category = CATEGORY_ADDRESS_LIST_PAGE,
            label = "",
            trackerId = TRACKER_ID_CLICKING_TAB_UTAMA
        )
    }

    fun onClickFromFriendTab() {
        sendTracker(
            action = ACTION_CLICKING_TAB_FROM_FRIEND,
            category = CATEGORY_ADDRESS_LIST_PAGE,
            label = "",
            trackerId = TRACKER_ID_CLICKING_TAB_FROM_FRIEND
        )
    }

    fun onClickPhoneBookToGetContact(isRequestAddress: Boolean) {
        if (isRequestAddress) {
            sendTracker(
                action = ACTION_CLICKING_PHONE_BOOK_TO_GET_CONTACT,
                category = CATEGORY_BOTTOM_SHEET_INSERTING_IDENTITY,
                label = "",
                trackerId = TRACKER_ID_CLICKING_PHONE_BOOK_TO_GET_CONTACT
            )
        } else {
            sendTracker(
                action = ACTION_DIRECT_SHARE_CLICKING_PHONE_BOOK_TO_GET_CONTACT,
                category = CATEGORY_BOTTOM_SHEET_INSERTING_IDENTITY,
                label = "",
                trackerId = TRACKER_ID_DIRECT_SHARE_CLICKING_PHONE_BOOK_TO_GET_CONTACT
            )
        }
    }

    fun onClickBottomSheetSendButton(isRequestAddress: Boolean, isSuccess: Boolean) {
        if (isRequestAddress) {
            sendTracker(
                action = ACTION_CLICKING_KIRIM_BUTTON,
                category = CATEGORY_BOTTOM_SHEET_INSERTING_IDENTITY,
                label = if (isSuccess) LABEL_SUCCESS else LABEL_NOT_SUCCESS,
                trackerId = TRACKER_ID_CLICKING_KIRIM_BUTTON
            )
        } else {
            sendTracker(
                action = ACTION_DIRECT_SHARE_USERS_CLICKING_KIRIM_BUTTON,
                category = CATEGORY_BOTTOM_SHEET_INSERTING_IDENTITY,
                label = if (isSuccess) LABEL_SUCCESS else LABEL_NOT_SUCCESS,
                trackerId = TRACKER_ID_DIRECT_SHARE_USERS_CLICKING_KIRIM_BUTTON
            )
        }
    }

    fun onClickSaveButton(isSuccess: Boolean) {
        sendTracker(
            action = ACTION_CLICKING_SAVE_BUTTON,
            category = CATEGORY_ADDRESS_LIST_PAGE,
            label = if (isSuccess) LABEL_SUCCESS else LABEL_NOT_SUCCESS,
            trackerId = TRACKER_ID_CLICKING_SAVE_BUTTON
        )
    }

    fun onClickDeleteButton(isSuccess: Boolean) {
        sendTracker(
            action = ACTION_CLICKING_DELETE_BUTTON,
            category = CATEGORY_ADDRESS_LIST_PAGE,
            label = if (isSuccess) LABEL_SUCCESS else LABEL_NOT_SUCCESS,
            trackerId = TRACKER_ID_CLICKING_DELETE_BUTTON
        )
    }

    fun onChooseAddressList() {
        sendTracker(
            action = ACTION_CHOOSE_ONE_OF_ADDRESS_LIST,
            category = CATEGORY_ADDRESS_LIST_PAGE,
            label = "",
            trackerId = TRACKER_ID_CHOOSE_ONE_OF_ADDRESS_LIST
        )
    }

    fun onAgreeSendAddress(isDirectShare: Boolean, isSuccess: Boolean = false) {
        if (isDirectShare) {
            sendTracker(
                action = ACTION_DIRECT_SHARE_AGREE_TO_SEND_ADDRESS,
                category = CATEGORY_CONSENT_BOTTOM_SHEET,
                label = if (isSuccess) LABEL_SUCCESS else LABEL_NOT_SUCCESS,
                trackerId = TRACKER_ID_DIRECT_SHARE_AGREE_TO_SEND_ADDRESS
            )
        } else {
            sendTracker(
                action = ACTION_AGREE_TO_SEND_ADDRESS,
                category = CATEGORY_CONSENT_BOTTOM_SHEET,
                label = if (isSuccess) LABEL_SUCCESS else LABEL_NOT_SUCCESS,
                trackerId = TRACKER_ID_AGREE_TO_SEND_ADDRESS
            )
        }
    }

    fun onDisagreeSendAddress(isDirectShare: Boolean, isSuccess: Boolean = false) {
        if (isDirectShare) {
            sendTracker(
                action = ACTION_DIRECT_SHARE_DISAGREE_TO_SEND_ADDRESS,
                category = CATEGORY_CONSENT_BOTTOM_SHEET,
                label = "",
                trackerId = TRACKER_ID_DIRECT_SHARE_DISAGREE_TO_SEND_ADDRESS
            )
        } else {
            sendTracker(
                action = ACTION_DISAGREE_TO_SEND_ADDRESS,
                category = CATEGORY_CONSENT_BOTTOM_SHEET,
                label = if (isSuccess) LABEL_SUCCESS else LABEL_NOT_SUCCESS,
                trackerId = TRACKER_ID_DISAGREE_TO_SEND_ADDRESS
            )
        }
    }

    fun onClickRequestAddress() {
        sendTracker(
            action = ACTION_CLICKING_REQUEST_TO_FRIEND_BUTTON,
            category = CATEGORY_ADDRESS_LIST_PAGE,
            label = "",
            trackerId = TRACKER_ID_CLICKING_REQUEST_TO_FRIEND_BUTTON
        )
    }

    fun onClickShareAddress() {
        sendTracker(
            action = ACTION_CLICKING_SHARE_ADDRESS_BUTTON,
            category = CATEGORY_ADDRESS_LIST_PAGE,
            label = "",
            trackerId = TRACKER_ID_CLICKING_SHARE_ADDRESS_BUTTON
        )
    }

    fun onClickDirectShareButton() {
        sendTracker(
            action = ACTION_DIRECT_SHARE_CLICKING_SHARE_BUTTON,
            category = CATEGORY_ADDRESS_LIST_PAGE,
            label = "",
            trackerId = TRACKER_ID_DIRECT_SHARE_CLICKING_SHARE_BUTTON
        )
    }

    fun onCheckAllAddress(isChecked: Boolean) {
        sendTracker(
            action = ACTION_CLICKING_SELECT_ALL_BUTTON,
            category = CATEGORY_ADDRESS_LIST_PAGE,
            label = if (isChecked) LABEL_CHECK else LABEL_UNCHECK,
            trackerId = TRACKER_ID_CLICKING_SELECT_ALL_BUTTON
        )
    }
}
