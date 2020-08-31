package com.tokopedia.managepassword.addpassword.analytics

import com.tokopedia.track.TrackApp

class AddPasswordAnalytics {
    private val tracker = TrackApp.getInstance().gtm

    fun onClickSubmit() {
        tracker.sendGeneralEvent(
                EVENT.CLICK_LOGIN,
                CATEGORY.ACCOUNT_SETTING,
                ACTION.CLICK_ON_BUTTON_SUBMIT,
                LABEL.CLICK
        )
    }

    fun onSuccessAddPassword() {
        tracker.sendGeneralEvent(
                EVENT.CLICK_LOGIN,
                CATEGORY.ACCOUNT_SETTING,
                ACTION.CLICK_ON_BUTTON_SUBMIT,
                LABEL.CLICK_SUCCESS
        )
    }

    fun onFailedAddPassword(message: String) {
        tracker.sendGeneralEvent(
                EVENT.CLICK_LOGIN,
                CATEGORY.ACCOUNT_SETTING,
                ACTION.CLICK_ON_BUTTON_SUBMIT,
                "${LABEL.CLICK_FAILED} - $message"
        )
    }

    companion object {
        private object EVENT {
            const val CLICK_LOGIN = "clickAccount"
        }

        private object CATEGORY {
            const val ACCOUNT_SETTING = "account setting - password"
        }

        private object ACTION {
            const val CLICK_ON_BUTTON_SUBMIT = "click on buat kata sandi"
        }

        private object LABEL {
            const val CLICK = "click"
            const val CLICK_SUCCESS = "success"
            const val CLICK_FAILED = "failed"
        }
    }
}