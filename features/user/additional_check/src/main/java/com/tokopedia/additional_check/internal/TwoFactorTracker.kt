package com.tokopedia.additional_check.internal

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

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

    fun viewAccountLinkingReminder() {
        track(
            TrackAppUtils.gtmData(
                Event.VIEW_ACCOUNT_IRIS,
                Category.ACCOUNT_LINKING_REMINDER,
                Action.VIEW_ACCOUNT_LINKING_REMINDER,
                Label.EMPTY)
        )
    }

    fun clickAccountLinkingReminder(label: String) {
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.ACCOUNT_LINKING_REMINDER,
                Action.CLICK_ACCOUNT_LINKING_REMINDER,
                label
            )
        )
    }

    private fun track(map: MutableMap<String, Any>) {
        map[KEY_BUSINESS_UNIT] = BUSSINESS_UNIT
        map[KEY_CURRENT_SITE] = CURRENT_SITE
        tracker.sendGeneralEvent(map)
    }

    companion object {
        const val KEY_BUSINESS_UNIT = "businessUnit"
        const val KEY_CURRENT_SITE = "currentSite"

        const val BUSSINESS_UNIT = "user platform"
        const val CURRENT_SITE = "tokopediamarketplace"

        object Event {
            const val CLICK_AUTH = "clickAuth"
            const val CLICK_ACCOUNT = "clickAccount"
            const val VIEW_ACCOUNT_IRIS = "viewAccountIris"
        }

        object Category {
            const val ADD_PHONE_NUMBER_PAGE = "2fa mlu add phone number page"
            const val ADD_PIN_PAGE = "2fa mlu add pin page"

            const val ACCOUNT_LINKING_REMINDER = "account linking reminder"
        }

        object Action {
            const val CLICK_BUTTON_ADD_PHONE_NUMBER = "click on button tambah nomor hp"
            const val VIEW_ADD_PIN_PAGE = "view 2fa mlu add pin page"
            const val CLICK_ADD_PIN_PAGE = "click on button buat pin tokopedia"
            const val CLICK_CLOSE_BUTTON_PAGE_ADD_PIN = "click on button close"

            const val VIEW_ACCOUNT_LINKING_REMINDER = "view account linking reminder"
            const val CLICK_ACCOUNT_LINKING_REMINDER = "click on button account linking reminder"
        }


        object Label {
            const val EMPTY = ""
            const val LINK_ACCOUNT = "sambungin akun"
            const val CLOSE = "close"
        }
    }
}