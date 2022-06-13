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
                Action.CLICK_ON_BTN_CLOSE,
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

    fun viewBiometricPopup() {
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.BIOMETRIC_MLU,
                Action.VIEW_BIOMETRIC_PAGE,
                Label.EMPTY)
        )
    }

    fun clickRegisterBiometric() {
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.BIOMETRIC_MLU,
                Action.CLICK_ON_REGISTER_BIOMETRIC,
                Label.EMPTY)
        )
    }

    fun clickCloseBiometric() {
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.BIOMETRIC_MLU,
                Action.CLICK_ON_BTN_CLOSE,
                Label.EMPTY)
        )
    }

    fun successAddBiometric() {
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.BIOMETRIC_SUCCESS_MLU,
                Action.SUCCESS_ADD_BIOMETRIC,
                Label.EMPTY)
        )
    }

    fun clickContinueShoppingWhenSuccess() {
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.BIOMETRIC_SUCCESS_MLU,
                Action.CLICK_ON_BTN_LANJUT_BELANJA,
                Label.EMPTY)
        )
    }

    fun clickCloseWhenSuccess() {
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.BIOMETRIC_SUCCESS_MLU,
                Action.CLICK_ON_BTN_CLOSE,
                Label.EMPTY)
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

            const val BIOMETRIC_MLU = "2fa mlu add biometrics"
            const val BIOMETRIC_SUCCESS_MLU = "2fa mlu success add biometrics"

        }

        object Action {
            const val CLICK_BUTTON_ADD_PHONE_NUMBER = "click on button tambah nomor hp"
            const val VIEW_ADD_PIN_PAGE = "view 2fa mlu add pin page"
            const val CLICK_ADD_PIN_PAGE = "click on button buat pin tokopedia"

            const val VIEW_ACCOUNT_LINKING_REMINDER = "view account linking reminder"
            const val CLICK_ACCOUNT_LINKING_REMINDER = "click on button account linking reminder"

            const val VIEW_BIOMETRIC_PAGE = "auto pop up 2fa mlu add biometrics page"
            const val CLICK_ON_REGISTER_BIOMETRIC = "click on daftarkan sidik jari"
            const val CLICK_ON_BTN_CLOSE = "click on button close"

            const val SUCCESS_ADD_BIOMETRIC = "auto pop up 2fa mlu success add biometrics page"
            const val CLICK_ON_BTN_LANJUT_BELANJA = "click on button lanjut belanja"

        }


        object Label {
            const val EMPTY = ""
            const val LINK_ACCOUNT = "sambungin akun"
            const val CLOSE = "close"
        }
    }
}