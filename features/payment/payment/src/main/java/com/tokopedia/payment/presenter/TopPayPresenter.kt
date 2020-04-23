package com.tokopedia.payment.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.network.constant.ErrorNetMessage
import com.tokopedia.payment.fingerprint.data.model.ResponsePaymentFingerprint
import com.tokopedia.payment.fingerprint.domain.GetPostDataOtpUseCase
import com.tokopedia.payment.fingerprint.domain.PaymentFingerprintUseCase
import com.tokopedia.payment.fingerprint.domain.SaveFingerPrintUseCase
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import rx.Subscription
import rx.subscriptions.CompositeSubscription
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*

/**
 * Created by kris on 3/14/17. Tokopedia
 */
class TopPayPresenter(private val saveFingerPrintUseCase: SaveFingerPrintUseCase,
                      private val paymentFingerprintUseCase: PaymentFingerprintUseCase,
                      private val getPostDataOtpUseCase: GetPostDataOtpUseCase,
                      private val userSession: UserSessionInterface) : BaseDaggerPresenter<TopPayContract.View?>(), TopPayContract.Presenter {

    private val compositeSubscription = CompositeSubscription()

    override val userId: String
        get() = userSession.userId

    override fun processUriPayment() {
        val paymentPassData = view?.paymentPassData
        if (paymentPassData != null) {
            try {
                val postData = paymentPassData.queryString.toByteArray()
                val method = paymentPassData.method ?: PaymentPassData.METHOD_POST
                view?.renderWebViewPostUrl(paymentPassData.redirectUrl, postData, method.equals(PaymentPassData.METHOD_GET, true))
            } catch (e: Exception) {
                view?.showToastMessageWithForceCloseView(ErrorNetMessage.MESSAGE_ERROR_DEFAULT)
            }
        } else {
            view?.showToastMessageWithForceCloseView(ErrorNetMessage.MESSAGE_ERROR_DEFAULT)
        }
    }

    override fun registerFingerPrint(transactionId: String?, publicKey: String?, date: String?, accountSignature: String?, userId: String?) {
        view?.let {
            it.showProgressDialog()
            saveFingerPrintUseCase.execute(saveFingerPrintUseCase.createRequestParams(transactionId, publicKey, date, accountSignature, userId),
                    subscriberRegisterFingerPrint)
        }
    }

    override fun paymentFingerPrint(transactionId: String?, publicKey: String?, date: String?, accountSignature: String?, userId: String?) {
        view?.let {
            it.showProgressDialog()
            paymentFingerprintUseCase.execute(paymentFingerprintUseCase.createRequestParams(transactionId, publicKey, date, accountSignature, userId),
                    subscriberPaymentFingerPrint)
        }
    }

    override fun getPostDataOtp(transactionId: String, urlOtp: String) {
        view?.let {
            it.showProgressDialog()
            getPostDataOtpUseCase.execute(getPostDataOtpUseCase.createRequestParams(transactionId),
                    getSubscriberPostDataOTP(urlOtp))
        }
    }

    override fun clearTimeoutSubscription() {
        compositeSubscription.clear()
    }

    override fun addTimeoutSubscription(subscribe: Subscription) {
        compositeSubscription.add(subscribe)
    }

    private fun getSubscriberPostDataOTP(urlOtp: String): Subscriber<HashMap<String, String>?> {

        return object : Subscriber<HashMap<String, String>?>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable?) {
                view?.hideProgressDialog()
                view?.onErrorGetPostDataOtp(e)
            }

            override fun onNext(stringStringHashMap: HashMap<String, String>?) {
                view?.hideProgressDialog()
                if (stringStringHashMap != null) {
                    view?.onSuccessGetPostDataOTP(urlEncodeUTF8(stringStringHashMap), urlOtp)
                } else {
                    view?.onErrorGetPostDataOtp(RuntimeException())
                }
            }
        }
    }

    private fun urlEncodeUTF8(map: Map<String, String>): String {
        val sb = StringBuilder()
        for ((key, value) in map) {
            if (sb.isNotEmpty()) {
                sb.append("&")
            }
            sb.append(String.format("%s=%s",
                    urlEncodeUTF8(key),
                    urlEncodeUTF8(value)
            ))
        }
        return sb.toString()
    }

    @Throws(UnsupportedOperationException::class)
    private fun urlEncodeUTF8(s: String): String {
        return try {
            URLEncoder.encode(s, UTF8)
        } catch (e: UnsupportedEncodingException) {
            throw UnsupportedOperationException(e)
        }
    }

    private val subscriberRegisterFingerPrint: Subscriber<Boolean>
        get() = object : Subscriber<Boolean>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable?) {
                view?.hideProgressDialog()
                view?.onErrorRegisterFingerPrint()
            }

            override fun onNext(isSuccess: Boolean) {
                view?.hideProgressDialog()
                if (isSuccess) {
                    view?.onSuccessRegisterFingerPrint()
                } else {
                    view?.onErrorRegisterFingerPrint()
                }
            }
        }

    private val subscriberPaymentFingerPrint: Subscriber<ResponsePaymentFingerprint?>
        get() = object : Subscriber<ResponsePaymentFingerprint?>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable?) {
                view?.hideProgressDialog()
                view?.onErrorPaymentFingerPrint()
            }

            override fun onNext(responsePaymentFingerprint: ResponsePaymentFingerprint?) {
                view?.hideProgressDialog()
                if (responsePaymentFingerprint?.isSuccess == true) {
                    view?.onSuccessPaymentFingerprint(responsePaymentFingerprint.url, responsePaymentFingerprint.paramEncode)
                } else {
                    view?.onErrorPaymentFingerPrint()
                }
            }
        }

    override fun detachView() {
        paymentFingerprintUseCase.unsubscribe()
        saveFingerPrintUseCase.unsubscribe()
        compositeSubscription.unsubscribe()
        super.detachView()
    }

    companion object {
        private const val UTF8 = "UTF-8"
    }
}