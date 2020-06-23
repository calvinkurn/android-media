package com.tokopedia.cart.view.subscriber

import com.tokopedia.atc_common.domain.model.response.atcexternal.AddToCartExternalDataModel
import com.tokopedia.cart.view.ICartListView
import rx.Subscriber

class AddToCartExternalSubscriber(val view: ICartListView?) : Subscriber<AddToCartExternalDataModel>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        if (view != null) {
            view.hideProgressLoading()
            view.showToastMessageRed(e)
            view.refreshCart()
        }
    }

    override fun onNext(model: AddToCartExternalDataModel) {
        if (view != null) {
            view.hideProgressLoading()
            if (model.message.isNotEmpty()) {
                view.showToastMessageGreen(model.message[0])
            }
            view.refreshCart()
        }
    }

}