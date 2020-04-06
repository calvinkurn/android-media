package com.tokopedia.purchase_platform.features.cart.view.subscriber

import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateAndValidateUseData
import com.tokopedia.purchase_platform.features.cart.view.CartListPresenter
import com.tokopedia.purchase_platform.features.cart.view.ICartListView
import rx.Subscriber

/**
 * Created by fwidjaja on 2020-03-05.
 */
class UpdateCartAndValidateUseSubscriber(private val view: ICartListView?,
                                         private val presenter: CartListPresenter) : Subscriber<UpdateAndValidateUseData>() {
    override fun onNext(t: UpdateAndValidateUseData?) {
        t?.let {
            it.updateCartData?.let { updateCartData ->
                if (updateCartData.isSuccess) {
                    it.promoUiModel?.let { promoUiModel ->
                        presenter.isLastApplyResponseStillValid = false
                        view?.updatePromoCheckoutStickyButton(promoUiModel)
                    }
                }
            }
        }
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable?) {
        view?.showPromoCheckoutStickyButtonInactive()
    }
}