package com.tokopedia.cart.view.subscriber

import com.tokopedia.cart.domain.model.cartlist.DeleteCartData
import com.tokopedia.cart.view.ICartListPresenter
import com.tokopedia.cart.view.ICartListView
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
            it.showToastMessageRed(e)
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
                    view.resetRecentViewList()
                    presenter.processInitialGetCartData(view.getCartId(), false, false)
                } else {
                    view.onDeleteCartDataSuccess(toBeDeletedCartIds)
                }

                val params = view.generateGeneralParamValidateUse()
                if (!removeAllItems && (view.checkHitValidateUseIsNeeded(params))) {
                    view.showPromoCheckoutStickyButtonLoading()
                    presenter.doUpdateCartAndValidateUse(params)
                }
                view.updateCartCounter(deleteCartData.cartCounter)
            } else {
                view.showToastMessageRed(deleteCartData.message ?: "")
            }
        }
    }
}