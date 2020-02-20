package com.tokopedia.purchase_platform.features.cart.view.subscriber

import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.purchase_platform.common.data.api.CartResponseErrorException
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateAndReloadCartListData
import com.tokopedia.purchase_platform.features.cart.view.ICartListPresenter
import com.tokopedia.purchase_platform.features.cart.view.ICartListView
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 2019-12-18.
 */

class UpdateAndReloadCartSubscriber(private val view: ICartListView?,
                                    private val presenter: ICartListPresenter?,
                                    private val cartListData: CartListData?) : Subscriber<UpdateAndReloadCartListData>() {
    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        view?.let {
            it.hideProgressLoading()
            var errorMessage = e.message
            if (e !is CartResponseErrorException) {
                errorMessage = ErrorHandler.getErrorMessage(it.getActivityObject(), e)
            }
            it.showToastMessageRed(errorMessage ?: "")
        }
    }

    override fun onNext(updateAndReloadCartListData: UpdateAndReloadCartListData) {
        view?.let {
            it.hideProgressLoading()
            updateAndReloadCartListData.cartListData?.let {
                presenter?.setCartListData(it)
                view.renderLoadGetCartDataFinish()
                view.renderInitialGetCartListDataSuccess(cartListData)
            }
        }
    }
}