package com.tokopedia.tokopoints.view.sendgift

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView

interface SendGiftContract {
    interface View : CustomerView {
        fun showLoading()
        fun hideLoading()
        fun showLoadingSendNow()
        fun hideLoadingSendNow()
        fun openPreConfirmationWindow()
        fun onErrorPreValidate(error: String)
        fun onSuccess()
        fun onError(error: String)
        val appContext: Context
        val activityContext: Context

        fun showPopup(title: String, message: String, success: Int)
    }

    interface Presenter {
        fun sendGift(id: Int?, email: String, notes: String)
        fun preValidateGift(id: Int?, email: String)
    }
}
