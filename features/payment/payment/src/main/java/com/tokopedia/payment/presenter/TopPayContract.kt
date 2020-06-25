package com.tokopedia.payment.presenter

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.common.payment.model.PaymentPassData
import rx.Subscription

/**
 * Created by zulfikarrahman on 3/27/18.
 */
interface TopPayContract {

    interface Presenter : CustomerPresenter<View?> {
        val userId: String
        fun processUriPayment()
        fun registerFingerPrint(transactionId: String?, publicKey: String?, date: String?, accountSignature: String?, userId: String?)
        fun paymentFingerPrint(transactionId: String?, publicKey: String?, date: String?, accountSignature: String?, userId: String?)
        fun getPostDataOtp(transactionId: String, urlOtp: String)
        fun clearTimeoutSubscription()
        fun addTimeoutSubscription(subscribe: Subscription)
    }

    interface View : CustomerView {
        val paymentPassData: PaymentPassData?
        fun renderWebViewPostUrl(url: String, postData: ByteArray, isGet: Boolean)
        fun showToastMessageWithForceCloseView(message: String)
        fun onSuccessRegisterFingerPrint()
        fun hideProgressDialog()
        fun showProgressDialog()
        fun onErrorRegisterFingerPrint()
        fun onErrorPaymentFingerPrint()
        fun onSuccessPaymentFingerprint(url: String?, paramEncode: String?)
        fun onErrorGetPostDataOtp(e: Throwable?)
        fun onSuccessGetPostDataOTP(postData: String, urlOtp: String)
    }
}