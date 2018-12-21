package com.tokopedia.cod

import android.view.View

/**
 * Created by fajarnuha on 17/12/18.
 */
interface CodContract {

    interface View {
        fun initView()
        fun showError(message: String)
        fun showLoading()
        fun hideLoading()
        fun onPayClicked(view: android.view.View)
        fun navigateToThankYouPage()
    }

    interface Presenter {
        fun submitConfirmation()
        fun confirmPayment()
    }

}