package com.tokopedia.cart.view.subscriber

import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.cart.domain.model.updatecart.UpdateAndValidateUseData
import com.tokopedia.cart.view.ICartListPresenter
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import rx.Subscriber

/**
 * Created by fwidjaja on 2020-03-05.
 */
class UpdateCartAndValidateUseSubscriber(private val view: ICartListView?,
                                         private val presenter: ICartListPresenter?) : Subscriber<UpdateAndValidateUseData>() {
    override fun onNext(t: UpdateAndValidateUseData?) {
        t?.let {
            it.updateCartData?.let { updateCartData ->
                if (updateCartData.isSuccess) {
                    it.promoUiModel?.let { promoUiModel ->
                        presenter?.setLastApplyNotValid()
                        presenter?.setValidateUseLastResponse(ValidateUsePromoRevampUiModel(promoUiModel = promoUiModel))
                        presenter?.setUpdateCartAndValidateUseLastResponse(t)
                        view?.updatePromoCheckoutStickyButton(promoUiModel)
                    }
                }
            }
        }
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        if (e is AkamaiErrorException) {
            presenter?.doClearAllPromo()
            view?.showToastMessageRed(e)
        }
        view?.renderPromoCheckoutButtonActiveDefault(emptyList())
    }
}