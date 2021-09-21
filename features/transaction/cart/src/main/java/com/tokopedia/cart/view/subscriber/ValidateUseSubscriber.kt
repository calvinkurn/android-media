package com.tokopedia.cart.view.subscriber

import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.cart.domain.model.updatecart.UpdateAndValidateUseData
import com.tokopedia.cart.view.CartListPresenter
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import rx.Subscriber

/**
 * Created by fwidjaja on 07/03/20.
 */
class ValidateUseSubscriber(private val view: ICartListView?,
                            private val presenter: CartListPresenter) : Subscriber<ValidateUsePromoRevampUiModel>() {
    override fun onCompleted() {}

    override fun onError(e: Throwable?) {
        if (e is AkamaiErrorException) {
            view?.showToastMessageRed(e)
        }
        view?.showPromoCheckoutStickyButtonInactive()
    }

    override fun onNext(response: ValidateUsePromoRevampUiModel?) {
        response?.promoUiModel?.let {
            view?.updateListRedPromos(response)
            presenter.setUpdateCartAndValidateUseLastResponse(UpdateAndValidateUseData().apply {
                promoUiModel = response.promoUiModel
            })
            presenter.isLastApplyResponseStillValid = false
            view?.updatePromoCheckoutStickyButton(it)
        }
    }
}