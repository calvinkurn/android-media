package com.tokopedia.cart.view.subscriber

import com.tokopedia.cart.domain.model.updatecart.UpdateAndReloadCartListData
import com.tokopedia.cart.view.ICartListPresenter
import com.tokopedia.cart.view.ICartListView
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 2019-12-18.
 */

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
        view?.let {
            it.hideProgressLoading()
            updateAndReloadCartListData.cartListData?.let { cartListData ->
                presenter?.setCartListData(cartListData)
                it.renderLoadGetCartDataFinish()
                it.renderInitialGetCartListDataSuccess(cartListData)
            }
        }
    }
}