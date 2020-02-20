package com.tokopedia.purchase_platform.features.cart.view.subscriber

import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.purchase_platform.common.data.api.CartResponseErrorException
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData
import com.tokopedia.purchase_platform.features.cart.view.ICartListPresenter
import com.tokopedia.purchase_platform.features.cart.view.ICartListView
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
            var errorMessage = e.message
            if (e !is CartResponseErrorException) {
                errorMessage = ErrorHandler.getErrorMessage(it.getActivityObject(), e)
            }
            it.renderErrorInitialGetCartListData(errorMessage ?: "")
            it.stopCartPerformanceTrace()
        }
    }

    override fun onNext(cartListData: CartListData) {
        view?.let {
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