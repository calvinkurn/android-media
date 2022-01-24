package com.tokopedia.cart.view.subscriber

import com.tokopedia.cart.domain.model.updatecart.UpdateCartData
import com.tokopedia.cart.view.ICartListView
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