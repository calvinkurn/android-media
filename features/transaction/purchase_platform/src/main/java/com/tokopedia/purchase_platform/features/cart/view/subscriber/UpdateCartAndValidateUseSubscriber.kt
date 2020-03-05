package com.tokopedia.purchase_platform.features.cart.view.subscriber

import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateAndValidateUseData
import com.tokopedia.purchase_platform.features.cart.view.ICartListView
import rx.Subscriber

/**
 * Created by fwidjaja on 2020-03-05.
 */
class UpdateCartAndValidateUseSubscriber(private val view: ICartListView?) : Subscriber<UpdateAndValidateUseData>() {
    override fun onNext(t: UpdateAndValidateUseData?) {
        t?.let {
            it.updateCartData?.let { updateCartData ->
                if (updateCartData.isSuccess) {
                    it.additionalInfoUiModel?.let { additional ->
                        view?.updatePromoCheckoutStickyButton(additional)
                    }
                }
            }
        }
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable?) {
        // do nothing
    }
}