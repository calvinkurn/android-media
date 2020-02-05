package com.tokopedia.purchase_platform.features.cart.view.subscriber

import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateAndReloadCartListData
import com.tokopedia.purchase_platform.features.cart.view.ICartListPresenter
import com.tokopedia.purchase_platform.features.cart.view.ICartListView
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