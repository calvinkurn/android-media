package com.tokopedia.profilecompletion.addphone.data.analitycs

import com.tokopedia.track.TrackApp

class AddPhoneNumberTracker {

    private val tracker = TrackApp.getInstance().gtm

    fun clickOnInputPhoneNumber() {
	tracker.sendGeneralEvent(
	    Event.CLICK_PROFILE,
	    Category.ADD_PHONE_NUMBER_PAGE,
	    Action.CLICK_ON_INPUT_PHONE_NUMBER,
	    Label.EMPTY
	)
    }

    fun clickOnButtonNext(isSuccess: Boolean, message: String) {
	tracker.sendGeneralEvent(
	    Event.CLICK_PROFILE,
	    Category.ADD_PHONE_NUMBER_PAGE,
	    Action.CLICK_ON_BUTTON_NEXT,
	    if (isSuccess) {
		"${Label.SUCCESS} - $message"
	    } else {
		"${Label.FAILED} - $message"
	    }
	)
    }

    fun viewPersonalDataPage(isSuccess: Boolean) {
	tracker.sendGeneralEvent(
	    Event.VIEW_PROFILE_IRIS,
	    Category.PERSONAL_DATA_PAGE,
	    Action.VIEW_PHONE_NUMBER_ADDED,
	    if (isSuccess) Label.SUCCESS else Label.FAILED
	)
    }

    companion object {
	object Event {
	    const val CLICK_PROFILE = "clickProfile"
	    const val VIEW_PROFILE_IRIS = "viewProfileIris"
	}

	object Category {
	    const val ADD_PHONE_NUMBER_PAGE = "add phone number page"
	    const val PERSONAL_DATA_PAGE = "personal data page"
	}

	object Action {
	    const val CLICK_ON_INPUT_PHONE_NUMBER = "input phone number"
	    const val CLICK_ON_BUTTON_NEXT = "click on button selanjutnya"
	    const val VIEW_PHONE_NUMBER_ADDED = "view nomor telepon berhasil ditambah"
	}

	object Label {
	    const val SUCCESS = "success"
	    const val FAILED = "failed"
	    const val EMPTY = ""
	}
    }
}