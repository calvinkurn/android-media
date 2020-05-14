package com.tokopedia.purchase_platform.features.cart.view.subscriber

import com.tokopedia.promocheckout.common.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.features.cart.view.ICartListView
import rx.Subscriber

/**
 * Created by fwidjaja on 09/03/20.
 */
class ClearRedPromosBeforeGoToCheckoutSubscriber(val view: ICartListView?) : Subscriber<ClearPromoUiModel>() {
    override fun onNext(t: ClearPromoUiModel?) {
        view?.hideProgressLoading()
        view?.onSuccessClearRedPromosThenGoToCheckout()
    }

    override fun onCompleted() {}

    override fun onError(e: Throwable?) {
        view?.hideProgressLoading()
        view?.onSuccessClearRedPromosThenGoToCheckout()
    }
}