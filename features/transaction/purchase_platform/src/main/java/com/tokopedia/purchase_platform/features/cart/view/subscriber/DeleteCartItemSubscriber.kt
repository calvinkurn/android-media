package com.tokopedia.purchase_platform.features.cart.view.subscriber

import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.purchase_platform.common.data.api.CartResponseErrorException
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.DeleteCartData
import com.tokopedia.purchase_platform.features.cart.view.ICartListPresenter
import com.tokopedia.purchase_platform.features.cart.view.ICartListView
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 2019-12-18.
 */

class DeleteCartItemSubscriber(private val view: ICartListView?,
                               private val presenter: ICartListPresenter,
                               private val toBeDeletedCartIds: List<String>,
                               private val removeAllItems: Boolean,
                               private val removeInsurance: Boolean) : Subscriber<DeleteCartData>() {
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

    override fun onNext(deleteCartData: DeleteCartData) {
        view?.let { view ->
            view.hideProgressLoading()
            view.renderLoadGetCartDataFinish()

            if (deleteCartData.isSuccess) {
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
                view.showToastMessageRed(deleteCartData.message ?: "")
            }
        }
    }
}