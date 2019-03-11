package com.tokopedia.cod.view

/**
 * Created by fajarnuha on 17/12/18.
 */
interface CodContract {

    interface View {
        fun initView()
        fun showError(message: String?)
        fun showLoading()
        fun hideLoading()
        fun onPayClicked(view: android.view.View)
        fun navigateToThankYouPage(applink: String)
        fun sendEventEECod()
    }

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun confirmPayment()
    }

}