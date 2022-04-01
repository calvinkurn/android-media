package com.tokopedia.updateinactivephone.features

import com.tokopedia.track.TrackApp
import javax.inject.Inject

class InactivePhoneWithPinTracker @Inject constructor() {

    private val tracker = TrackApp.getInstance().gtm

    fun clickOnButtonNextOnboarding() {
        tracker.sendGeneralEvent(
            Event.CLICK_INACTIVE_PHONE_NUMBER,
            Category.INACTIVE_PHONE_PAGE,
            Action.CLICK_ON_BUTTON_NEXT,
            Label.EMPTY
        )
    }

    fun clickOnButtonBackOnboarding() {
        tracker.sendGeneralEvent(
            Event.CLICK_INACTIVE_PHONE_NUMBER,
            Category.INACTIVE_PHONE_PAGE,
            Action.CLICK_ON_BUTTON_BACK,
            Label.LANJUT_UBAH
        )
    }

    fun clickOnButtonSubmitNewPhone() {
        tracker.sendGeneralEvent(
            Event.CLICK_INACTIVE_PHONE_NUMBER,
            Category.INACTIVE_PHONE_PAGE,
            Action.CLICK_ON_BUTTON_SUBMIT,
            Label.CLICK
        )
    }

    fun onSuccessSubmitNewPhone() {
        tracker.sendGeneralEvent(
            Event.CLICK_INACTIVE_PHONE_NUMBER,
            Category.INACTIVE_PHONE_PAGE,
            Action.CLICK_ON_BUTTON_SUBMIT,
            Label.SUCCESS
        )
    }

    fun onFailedSubmitNewPhone(message: String) {
        tracker.sendGeneralEvent(
            Event.CLICK_INACTIVE_PHONE_NUMBER,
            Category.INACTIVE_PHONE_PAGE,
            Action.CLICK_ON_BUTTON_SUBMIT,
            "${Label.FAILED} - $message"
        )
    }

    fun clickOnButtonBackAddNewPhone() {
        tracker.sendGeneralEvent(
            Event.CLICK_INACTIVE_PHONE_NUMBER,
            Category.INACTIVE_PHONE_PAGE,
            Action.CLICK_ON_BUTTON_BACK,
            Label.CHANGE_PHONE_NUMBER
        )
    }

    fun clickOnPopupLanjutVerifikasi() {
        tracker.sendGeneralEvent(
            Event.CLICK_INACTIVE_PHONE_NUMBER,
            Category.INACTIVE_PHONE_PAGE,
            Action.CLICK_ON_POPUP_INACTIVE_PHONE,
            Label.LANJUT_VERIFIKASI
        )
    }

    fun clickOnPopupKeluar() {
        tracker.sendGeneralEvent(
            Event.CLICK_INACTIVE_PHONE_NUMBER,
            Category.INACTIVE_PHONE_PAGE,
            Action.CLICK_ON_POPUP_INACTIVE_PHONE,
            Label.EXIT
        )
    }

    fun clickOnButtonHomeSuccessPage() {
        tracker.sendGeneralEvent(
            Event.CLICK_INACTIVE_PHONE_NUMBER,
            Category.INACTIVE_PHONE_PAGE,
            Action.CLICK_ON_BUTTON_HOME,
            Label.EMPTY
        )
    }

    companion object {

        object Category {
            const val INACTIVE_PHONE_PAGE = "ubah nomor hp page"
        }

        object Action {
            const val CLICK_ON_BUTTON_NEXT = "click on button lanjut ubah"
            const val CLICK_ON_BUTTON_BACK = "click on button back"
            const val CLICK_ON_BUTTON_SUBMIT = "click simpan"
            const val CLICK_ON_BUTTON_HOME = "click button ke home"
            const val CLICK_ON_POPUP_INACTIVE_PHONE = "click on pop up ubah nomor hp"
        }

        object Label {
            const val EMPTY = ""
            const val LANJUT_UBAH = "lanjut ubah"
            const val SUCCESS = "success"
            const val CLICK = "click"
            const val FAILED = "failed"
            const val CHANGE_PHONE_NUMBER = "ubah nomor hp page"
            const val LANJUT_VERIFIKASI = "lanjut verifikasi"
            const val EXIT = "keluar"
        }

        object Event {
            const val CLICK_INACTIVE_PHONE_NUMBER = "clickInactivePhoneNumber"
        }
    }
}