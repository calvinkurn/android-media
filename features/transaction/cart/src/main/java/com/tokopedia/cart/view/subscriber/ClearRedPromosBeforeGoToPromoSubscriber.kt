package com.tokopedia.cart.view.subscriber

import com.tokopedia.cart.view.ICartListView
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import rx.Subscriber

class ClearRedPromosBeforeGoToPromoSubscriber(val view: ICartListView?) : Subscriber<ClearPromoUiModel>() {
    override fun onNext(t: ClearPromoUiModel?) {
        view?.hideProgressLoading()
        view?.onSuccessClearRedPromosThenGoToPromo()
    }

    override fun onCompleted() {}

    override fun onError(e: Throwable?) {
        view?.hideProgressLoading()
        view?.onSuccessClearRedPromosThenGoToPromo()
    }
}
