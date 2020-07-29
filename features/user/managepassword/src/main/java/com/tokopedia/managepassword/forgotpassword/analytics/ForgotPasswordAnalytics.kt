package com.tokopedia.managepassword.forgotpassword.analytics

import com.tokopedia.track.TrackApp

class ForgotPasswordAnalytics {

    private val tracker = TrackApp.getInstance().gtm

    fun onCLickRegister() {
        tracker.sendGeneralEvent(
                EVENT.CLICK_LOGIN,
                CATEGORY.ACCOUNT_SETTING,
                ACTION.CLICK_ON_BUTTON_REGISTER,
                "${LABEL.CLICK} - $RESET_PASSWORD"
        )
    }

    fun onSuccessReset() {
        tracker.sendGeneralEvent(
                EVENT.CLICK_LOGIN,
                CATEGORY.ACCOUNT_SETTING,
                ACTION.CLICK_ON_BUTTON_SUBMIT,
                LABEL.CLICK_SUCCESS
        )
    }

    fun onError(message: String) {
        tracker.sendGeneralEvent(
                EVENT.CLICK_LOGIN,
                CATEGORY.ACCOUNT_SETTING,
                ACTION.CLICK_ON_BUTTON_SUBMIT,
                "${LABEL.CLICK_FAILED} - $message - $RESET_PASSWORD"
        )
    }

    companion object {
        private const val RESET_PASSWORD = "reset password"

        private object EVENT {
            const val CLICK_LOGIN = "clickAccount"
        }

        private object CATEGORY {
            const val ACCOUNT_SETTING = "account setting - password"
        }

        private object ACTION {
            const val CLICK_ON_BUTTON_SUBMIT = "click on button ganti kata sandi"
            const val CLICK_ON_BUTTON_REGISTER = "click on button daftar sekarang"
        }

        private object LABEL {
            const val CLICK = "click"
            const val CLICK_SUCCESS = "click - email - success - $RESET_PASSWORD"
            const val CLICK_FAILED = "click - email - failed"
        }
    }
}