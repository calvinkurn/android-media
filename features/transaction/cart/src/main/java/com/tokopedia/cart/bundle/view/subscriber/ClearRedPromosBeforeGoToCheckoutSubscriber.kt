package com.tokopedia.cart.bundle.view.subscriber

import com.tokopedia.cart.bundle.view.ICartListView
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
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