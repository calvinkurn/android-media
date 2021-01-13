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
                               private val forceExpandCollapsedUnavailableItems: Boolean,
                               private val isMoveToWishlist: Boolean,
                               private val isFromGlobalCheckbox: Boolean) : Subscriber<DeleteCartData>() {
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
                view.onDeleteCartDataSuccess(toBeDeletedCartIds, removeAllItems, forceExpandCollapsedUnavailableItems, isMoveToWishlist, isFromGlobalCheckbox)

                val params = view.generateGeneralParamValidateUse()
                if (!removeAllItems && (view.checkHitValidateUseIsNeeded(params))) {
                    view.showPromoCheckoutStickyButtonLoading()
                    presenter.doUpdateCartAndValidateUse(params)
                }
                presenter.processUpdateCartCounter()
            } else {
                view.hideProgressLoading()
                view.showToastMessageRed(deleteCartData.message ?: "")
            }
        }
    }
}