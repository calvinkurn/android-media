package com.tokopedia.cart.view.subscriber

import com.tokopedia.atc_common.domain.model.response.atcexternal.AddToCartExternalModel
import com.tokopedia.cart.view.ICartListView
import rx.Subscriber
import timber.log.Timber

class AddToCartExternalSubscriber(val view: ICartListView?) : Subscriber<AddToCartExternalModel>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        Timber.d(e)
        if (view != null) {
            view.hideProgressLoading()
            view.showToastMessageRed(e)
            view.refreshCartWithSwipeToRefresh()
        }
    }

    override fun onNext(model: AddToCartExternalModel) {
        if (view != null) {
            view.hideProgressLoading()
            if (model.message.isNotEmpty()) {
                view.showToastMessageGreen(model.message[0])
            }
            view.refreshCartWithSwipeToRefresh()
        }
    }

}