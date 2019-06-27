package com.tokopedia.expresscheckout.view.variant.subscriber

import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantContract
import com.tokopedia.transaction.common.sharedata.AddToCartResult
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 23/01/19.
 */

class DoOneClickShipmentAtcSubscriber(val view: CheckoutVariantContract.View?, val presenter: CheckoutVariantContract.Presenter)
    : Subscriber<AddToCartResult>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.hideLoadingDialog()
        view?.showToasterError(ErrorHandler.getErrorMessage(view.getActivityContext(), e))
    }

    override fun onNext(response: AddToCartResult) {
        view?.hideLoadingDialog()
        view?.navigateCheckoutToOcs()
    }

}