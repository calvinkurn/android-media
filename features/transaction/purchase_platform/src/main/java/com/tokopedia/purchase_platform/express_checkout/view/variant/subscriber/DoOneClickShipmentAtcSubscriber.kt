package com.tokopedia.purchase_platform.express_checkout.view.variant.subscriber

import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.purchase_platform.express_checkout.view.variant.CheckoutVariantContract
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 23/01/19.
 */

class DoOneClickShipmentAtcSubscriber(val view: CheckoutVariantContract.View?,
                                      val presenter: CheckoutVariantContract.Presenter)
    : Subscriber<AddToCartDataModel>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.hideLoadingDialog()
        view?.showToasterError(ErrorHandler.getErrorMessage(view.getActivityContext(), e))
    }

    override fun onNext(response: AddToCartDataModel) {
        view?.hideLoadingDialog()
        view?.navigateCheckoutToOcs()
    }

}