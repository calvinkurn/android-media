package com.tokopedia.purchase_platform.features.cart.view.subscriber

import com.tokopedia.purchase_platform.features.cart.view.ICartListView
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use.ValidateUsePromoRevampUiModel
import rx.Subscriber

/**
 * Created by fwidjaja on 07/03/20.
 */
class ValidateUseSubscriber(private val view: ICartListView?) : Subscriber<ValidateUsePromoRevampUiModel>() {
    override fun onCompleted() {}

    override fun onError(e: Throwable?) {
        view?.showPromoCheckoutStickyButtonInactive()
    }

    override fun onNext(t: ValidateUsePromoRevampUiModel?) {
        t?.promoUiModel?.let {
            view?.updateListRedPromos(t)
            view?.updatePromoCheckoutStickyButton(it) }
    }
}