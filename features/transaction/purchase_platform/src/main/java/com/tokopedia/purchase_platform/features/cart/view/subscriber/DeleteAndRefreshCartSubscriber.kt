package com.tokopedia.purchase_platform.features.cart.view.subscriber

import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.purchase_platform.common.data.api.CartResponseErrorException
import com.tokopedia.purchase_platform.features.cart.domain.model.DeleteAndRefreshCartListData
import com.tokopedia.purchase_platform.features.cart.view.ICartListPresenter
import com.tokopedia.purchase_platform.features.cart.view.ICartListView
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 2019-12-18.
 */

class DeleteAndRefreshCartSubscriber(private val view: ICartListView?,
                                     private val presenter: ICartListPresenter,
                                     private val toBeDeletedCartIds: List<Int>,
                                     private val removeAllItems: Boolean,
                                     private val removeInsurance: Boolean) : Subscriber<DeleteAndRefreshCartListData>() {
    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        view?.let {
            it.hideProgressLoading()
            e.printStackTrace()
            var errorMessage = e.message
            if (e !is CartResponseErrorException) {
                errorMessage = ErrorHandler.getErrorMessage(it.getActivityObject(), e)
            }
            it.showToastMessageRed(errorMessage ?: "")
        }
    }

    override fun onNext(deleteAndRefreshCartListData: DeleteAndRefreshCartListData) {
        view?.let { view ->
            view.hideProgressLoading()
            view.renderLoadGetCartDataFinish()

            if (deleteAndRefreshCartListData.deleteCartData?.isSuccess  == true) {
                if (removeInsurance) {
                    view.getInsuranceCartShopData()?.let {
                        presenter.processDeleteCartInsurance(it, false)
                    }
                }

                if (removeAllItems) {
                    presenter.processInitialGetCartData(view.getCartId(), false, false)
                } else {
                    view.onDeleteCartDataSuccess(toBeDeletedCartIds)
                }
            } else {
                view.showToastMessageRed(
                        deleteAndRefreshCartListData.deleteCartData?.message ?: ""
                )
            }
        }
    }
}