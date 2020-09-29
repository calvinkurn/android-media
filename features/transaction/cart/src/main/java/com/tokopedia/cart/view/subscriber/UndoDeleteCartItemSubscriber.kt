package com.tokopedia.cart.view.subscriber

import com.tokopedia.cart.domain.model.cartlist.UndoDeleteCartData
import com.tokopedia.cart.view.ICartListView
import rx.Subscriber
import timber.log.Timber

class UndoDeleteCartItemSubscriber(private val view: ICartListView?) : Subscriber<UndoDeleteCartData>() {
    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        view?.let {
            Timber.e(e)
            it.hideProgressLoading()
            it.showToastMessageRed(e)
        }
    }

    override fun onNext(undoDeleteCartData: UndoDeleteCartData) {
        view?.let { view ->
            view.hideProgressLoading()

            if (undoDeleteCartData.isSuccess) {
                view.onUndoDeleteCartDataSuccess(undoDeleteCartData)
            } else {
                view.showToastMessageRed(undoDeleteCartData.message ?: "")
            }
        }
    }
}