package com.tokopedia.cart.view.subscriber

import com.tokopedia.cart.domain.model.cartlistcheckboxstate.CartlistCheckboxStateData
import rx.Subscriber

class SetCartlistCheckboxStateSubscriber constructor(): Subscriber<CartlistCheckboxStateData>() {

    override fun onCompleted() {
        // No-op
    }

    override fun onError(e: Throwable?) {
        // No-op
    }

    override fun onNext(data: CartlistCheckboxStateData) {
        // No-op
    }

}