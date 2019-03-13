package com.tokopedia.expresscheckout.view.variant.subscriber

import com.tokopedia.abstraction.common.utils.view.CommonUtils
import com.tokopedia.expresscheckout.common.view.errorview.ErrorBottomsheets.Companion.RETRY_ACTION_RELOAD_CHECKOUT_FOR_PAYMENT
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantContract
import com.tokopedia.payment.model.PaymentPassData
import com.tokopedia.transactiondata.entity.shared.checkout.CheckoutData
import rx.Subscriber

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
        CommonUtils.dumper(e.stackTrace)
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