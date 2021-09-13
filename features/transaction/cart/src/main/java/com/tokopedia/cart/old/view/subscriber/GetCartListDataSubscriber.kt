package com.tokopedia.cart.old.view.subscriber

import com.tokopedia.cart.old.domain.model.cartlist.CartListData
import com.tokopedia.cart.old.view.CartLogger
import com.tokopedia.cart.old.view.ICartListPresenter
import com.tokopedia.cart.old.view.ICartListView
import rx.Subscriber
import timber.log.Timber

/**
 * Created by Irfan Khoirul on 2019-12-18.
 */

class GetCartListDataSubscriber(val view: ICartListView?,
                                val presenter: ICartListPresenter?,
                                val initialLoad: Boolean) : Subscriber<CartListData>() {
    override fun onCompleted() {
    }

    override fun onError(e: Throwable) {
        Timber.e(e)
        view?.let {
            if (!initialLoad) {
                it.hideProgressLoading()
            }
            it.renderLoadGetCartDataFinish()
            it.renderErrorInitialGetCartListData(e)
            it.stopCartPerformanceTrace()
            CartLogger.logOnErrorLoadCartPage(e)
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
            presenter?.processUpdateCartCounter()
            presenter?.setCartListData(cartListData)
            it.renderLoadGetCartDataFinish()
            it.renderInitialGetCartListDataSuccess(cartListData)
            it.stopCartPerformanceTrace()
        }
    }
}