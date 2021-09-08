package com.tokopedia.cart.bundle.view.subscriber

import com.tokopedia.cart.bundle.domain.model.updatecart.UpdateAndReloadCartListData
import com.tokopedia.cart.bundle.view.ICartListPresenter
import com.tokopedia.cart.bundle.view.ICartListView
import rx.Subscriber

class UpdateAndReloadCartSubscriber(private val view: ICartListView?,
                                    private val presenter: ICartListPresenter?) : Subscriber<UpdateAndReloadCartListData>() {
    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        view?.let {
            it.hideProgressLoading()
            it.showToastMessageRed(e)
        }
    }

    override fun onNext(updateAndReloadCartListData: UpdateAndReloadCartListData) {
        view?.hideProgressLoading()
        presenter?.processInitialGetCartData(updateAndReloadCartListData.cartId, false, true, updateAndReloadCartListData.getCartState)
    }
}