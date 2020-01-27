package com.tokopedia.purchase_platform.features.express_checkout.view.variant.subscriber

import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.purchase_platform.common.domain.model.CheckoutData
import com.tokopedia.purchase_platform.common.view.error_bottomsheet.ErrorBottomsheets.Companion.RETRY_ACTION_RELOAD_CHECKOUT_FOR_PAYMENT
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.CheckoutVariantContract
import rx.Subscriber
import timber.log.Timber

/**
 * Created by Irfan Khoirul on 29/01/19.
 */

class DoCheckoutSubscriber(val view: CheckoutVariantContract.View?, val presenter: CheckoutVariantContract.Presenter)
    : Subscriber<CheckoutData>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        view?.hideLoadingDialog()
        view?.showErrorAPI(RETRY_ACTION_RELOAD_CHECKOUT_FOR_PAYMENT)
        Timber.d(e)
    }

    override fun onNext(checkoutData: CheckoutData) {
        view?.hideLoadingDialog()
        if (!checkoutData.isError()) {
            val paymentPassData = PaymentPassData()
            paymentPassData.redirectUrl = checkoutData.redirectUrl
            paymentPassData.transactionId = checkoutData.transactionId
            paymentPassData.paymentId = checkoutData.paymentId
            paymentPassData.callbackSuccessUrl = checkoutData.callbackSuccessUrl
            paymentPassData.callbackFailedUrl = checkoutData.callbackFailedUrl
            paymentPassData.queryString = checkoutData.queryString
            view?.navigateCheckoutToPayment(paymentPassData)
        } else {
            view?.showErrorAPI(RETRY_ACTION_RELOAD_CHECKOUT_FOR_PAYMENT)
        }
    }

}