package com.tokopedia.changepassword.common.analytics

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
        private const val SCREEN_NAME: String = "Change Password Page"

        object EVENT {
            private const val CLICK_ACCOUNT = "clickAccount"
        }

        object CATEGORY {
            private const val ACCOUNT_SETTING = "account setting - password"
        }

        object ACTION {
            private const val CLICK_ON_BUTTON_SUBMIT = "click on button simpan change password"
            private const val CLICK_ON_BUTTON_FORGOT_PASSWORD = "click lupa kata sandi - change password"
        }

        object LABEL {
            private const val EMPTY = ""
            private const val CLICK = "click"
            private const val CLICK_SUCCESS = "success"
            private const val CLICK_FAILED = "failed"
        }
    }
}