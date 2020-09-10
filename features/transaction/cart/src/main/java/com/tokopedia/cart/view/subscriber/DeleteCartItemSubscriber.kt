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
                               private val removeInsurance: Boolean,
                               private val forceExpandCollapsedUnavailableItems: Boolean) : Subscriber<DeleteCartData>() {
    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        view?.let {
            if (forceExpandCollapsedUnavailableItems) {
                it.reCollapseExpandedDeletedUnavailableItems()
            }
            it.hideProgressLoading()
            e.printStackTrace()
            it.showToastMessageRed(e)
        }
    }

    override fun onNext(deleteCartData: DeleteCartData) {
        view?.let { view ->
            view.renderLoadGetCartDataFinish()

            if (deleteCartData.isSuccess) {
                if (removeInsurance) {
                    view.getInsuranceCartShopData()?.let {
                        presenter.processDeleteCartInsurance(it, false)
                    }
                }

                view.onDeleteCartDataSuccess(toBeDeletedCartIds, removeAllItems, forceExpandCollapsedUnavailableItems)

                val params = view.generateGeneralParamValidateUse()
                if ((view.checkHitValidateUseIsNeeded(params))) {
                    view.showPromoCheckoutStickyButtonLoading()
                    presenter.doUpdateCartAndValidateUse(params)
                }
                view.updateCartCounter(deleteCartData.cartCounter)
            } else {
                view.hideProgressLoading()
                view.showToastMessageRed(deleteCartData.message ?: "")
            }
        }
    }
}