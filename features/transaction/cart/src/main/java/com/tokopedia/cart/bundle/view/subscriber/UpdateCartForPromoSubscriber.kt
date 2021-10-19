package com.tokopedia.cart.bundle.view.subscriber

import com.tokopedia.cart.bundle.domain.model.updatecart.UpdateCartData
import com.tokopedia.cart.bundle.view.ICartListView
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 2019-12-18.
 */

class UpdateCartForPromoSubscriber(private val view: ICartListView?) : Subscriber<UpdateCartData>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
    }

    override fun onNext(data: UpdateCartData) {
    }

}