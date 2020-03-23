package com.tokopedia.purchase_platform.features.cart.view.subscriber

import com.tokopedia.promocheckout.common.domain.model.clearpromo.ClearCacheAutoApplyStackResponse
import com.tokopedia.purchase_platform.features.cart.view.ICartListView
import rx.Subscriber

/**
 * Created by fwidjaja on 09/03/20.
 */
class ClearRedPromosBeforeGoToCheckoutSubscriber(val view: ICartListView?) : Subscriber<ClearCacheAutoApplyStackResponse>() {
    override fun onNext(t: ClearCacheAutoApplyStackResponse?) {
        view?.onSuccessClearRedPromosThenGoToCheckout()
    }

    override fun onCompleted() {}

    override fun onError(e: Throwable?) {
        view?.onSuccessClearRedPromosThenGoToCheckout()
    }
}