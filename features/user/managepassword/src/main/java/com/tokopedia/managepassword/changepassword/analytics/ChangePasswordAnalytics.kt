package com.tokopedia.managepassword.changepassword.analytics

import com.tokopedia.track.TrackApp

/**
 * @author by nisie on 7/25/18.
 */
class ChangePasswordAnalytics{

    private val tracker = TrackApp.getInstance().gtm

    fun onClickSubmit() {
        tracker.sendGeneralEvent(
                EVENT.CLICK_ACCOUNT,
                CATEGORY.ACCOUNT_SETTING,
                ACTION.CLICK_ON_BUTTON_SUBMIT,
                LABEL.CLICK
        )
    }

    fun onClickForgotPassword() {
        tracker.sendGeneralEvent(
                EVENT.CLICK_ACCOUNT,
                CATEGORY.ACCOUNT_SETTING,
                ACTION.CLICK_ON_BUTTON_FORGOT_PASSWORD,
                LABEL.EMPTY
        )
    }

    fun onSuccessChangePassword() {
        tracker.sendGeneralEvent(
                EVENT.CLICK_ACCOUNT,
                CATEGORY.ACCOUNT_SETTING,
                ACTION.CLICK_ON_BUTTON_SUBMIT,
                LABEL.CLICK_SUCCESS
        )
    }

    fun onErrorValidate(message: String) {
        tracker.sendGeneralEvent(
                EVENT.CLICK_ACCOUNT,
                CATEGORY.ACCOUNT_SETTING,
                ACTION.CLICK_ON_BUTTON_SUBMIT,
                "${LABEL.CLICK_FAILED} - $message"
        )
    }

    companion object {
        const val SCREEN_NAME: String = "Change Password Page"

        private object EVENT {
            const val CLICK_ACCOUNT = "clickAccount"
        }

        private object CATEGORY {
            const val ACCOUNT_SETTING = "account setting - password"
        }

        private object ACTION {
            const val CLICK_ON_BUTTON_SUBMIT = "click on button simpan change password"
            const val CLICK_ON_BUTTON_FORGOT_PASSWORD = "click lupa kata sandi - change password"
        }

        private object LABEL {
            const val EMPTY = ""
            const val CLICK = "click"
            const val CLICK_SUCCESS = "success"
            const val CLICK_FAILED = "failed"
        }
    }
}