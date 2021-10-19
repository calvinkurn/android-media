package com.tokopedia.cart.old.view.subscriber

import com.tokopedia.cart.old.domain.model.cartlistcheckboxstate.CartlistCheckboxStateData
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