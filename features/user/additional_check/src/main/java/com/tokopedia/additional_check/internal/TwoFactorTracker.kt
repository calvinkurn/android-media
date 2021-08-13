package com.tokopedia.additional_check.internal

import com.tokopedia.track.TrackApp

class TwoFactorTracker {

    private val tracker = TrackApp.getInstance().gtm

    /**
     * Add Phone Number
     */
    fun clickButtonPageAddPhoneNumber() {
        tracker.sendGeneralEvent(
                Event.CLICK_AUTH,
                Category.ADD_PHONE_NUMBER_PAGE,
                Action.CLICK_BUTTON_ADD_PHONE_NUMBER,
                Label.EMPTY
        )
    }

    /**
     * Add PIN
     */
    fun viewPageOnboardingAddPin() {
        tracker.sendGeneralEvent(
                Event.CLICK_AUTH,
                Category.ADD_PIN_PAGE,
                Action.VIEW_ADD_PIN_PAGE,
                Label.EMPTY
        )
    }

    fun clickButtonPageAddPin() {
        tracker.sendGeneralEvent(
                Event.CLICK_AUTH,
                Category.ADD_PIN_PAGE,
                Action.CLICK_ADD_PIN_PAGE,
                Label.EMPTY
        )
    }

    fun clickCloseButtonPageAddPin() {
        tracker.sendGeneralEvent(
                Event.CLICK_AUTH,
                Category.ADD_PIN_PAGE,
                Action.CLICK_CLOSE_BUTTON_PAGE_ADD_PIN,
                Label.EMPTY
        )
    }

    companion object {
        object Event {
            const val CLICK_AUTH = "clickAuth"
        }

        object Category {
            const val ADD_PHONE_NUMBER_PAGE = "2fa mlu add phone number page"
            const val ADD_PIN_PAGE = "2fa mlu add pin page"
        }

        object Action {
            const val CLICK_BUTTON_ADD_PHONE_NUMBER = "click on button tambah nomor hp"
            const val VIEW_ADD_PIN_PAGE = "view 2fa mlu add pin page"
            const val CLICK_ADD_PIN_PAGE = "click on button buat pin tokopedia"
            const val CLICK_CLOSE_BUTTON_PAGE_ADD_PIN = "click on button close"
        }


        object Label {
            const val EMPTY = ""
        }
    }
}