package com.tokopedia.profilecompletion.changename.data.analytics

import com.tokopedia.track.TrackApp

class ChangeNameTracker {

    private val tracker = TrackApp.getInstance().gtm

    fun clickOnChangeName() {
	tracker.sendGeneralEvent(
	    EVENT_CLICK_ACCOUNT,
	    CATEGORY,
	    CLICK_ON_BUTTON_CHANGE_NAME,
	    LABEL_EMPTY
	)
    }

    fun back() {
	tracker.sendGeneralEvent(
	    EVENT_CLICK_ACCOUNT,
	    CATEGORY,
	    CLICK_ON_BUTTON_BACK,
	    LABEL_EMPTY
	)
    }

    fun onSuccessChangeName() {
	tracker.sendGeneralEvent(
	    EVENT_CLICK_ACCOUNT,
	    CATEGORY,
	    CLICK_ON_BUTTON_SAVE,
	    LABEL_SUCCESS
	)
    }

    fun onSuccessResponse(messgae: String) {
	tracker.sendGeneralEvent(
	    "",
	    "",
	    "",
	    ""
	)
    }

    fun onFailedChangeName(errorMessage: String) {
	tracker.sendGeneralEvent(
	    EVENT_CLICK_ACCOUNT,
	    CATEGORY,
	    CLICK_ON_BUTTON_SAVE,
	    "$LABEL_FAILED - $errorMessage"
	)
    }

    companion object {
	private const val EVENT_CLICK_ACCOUNT = "clickAccount"

	private const val CATEGORY = "account setting - change name"

	private const val CLICK_ON_BUTTON_CHANGE_NAME = "click on button change name"
	private const val CLICK_ON_BUTTON_SAVE = "click on button simpan"
	private const val CLICK_ON_BUTTON_BACK = "click on button back"

	private const val LABEL_SUCCESS = "success"
	private const val LABEL_FAILED = "failed"
	private const val LABEL_EMPTY = ""
    }
}