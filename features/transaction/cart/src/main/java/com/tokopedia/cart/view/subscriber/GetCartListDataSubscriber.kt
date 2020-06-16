package com.tokopedia.cart.view.subscriber

import com.tokopedia.cart.domain.model.cartlist.CartListData
import com.tokopedia.cart.view.ICartListPresenter
import com.tokopedia.cart.view.ICartListView
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 2019-12-18.
 */

class GetCartListDataSubscriber(val view: ICartListView?,
                                val presenter: ICartListPresenter?,
                                val initialLoad: Boolean) : Subscriber<CartListData>() {
    override fun onCompleted() {}

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.let {
            if (!initialLoad) {
                it.hideProgressLoading()
            }
            it.renderLoadGetCartDataFinish()
            it.renderErrorInitialGetCartListData(e)
            it.stopCartPerformanceTrace()
        }
    }

    override fun onNext(cartListData: CartListData) {
        view?.let {
            presenter?.setLastApplyValid()
            presenter?.setValidateUseLastResponse(null)
            presenter?.setUpdateCartAndValidateUseLastResponse(null)
            if (!initialLoad) {
                it.hideProgressLoading()
            }
            presenter?.setCartListData(cartListData)
            it.renderLoadGetCartDataFinish()
            it.renderInitialGetCartListDataSuccess(cartListData)
            it.stopCartPerformanceTrace()
        }
    }
}