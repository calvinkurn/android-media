package com.tokopedia.cart.bundle.view.subscriber

import com.tokopedia.cart.bundle.view.ICartListView
import rx.Subscriber

class UpdateCartCounterSubscriber constructor(val view: ICartListView?): Subscriber<Int>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable?) {
        // Do nothing
    }

    override fun onNext(counter: Int?) {
        view?.updateCartCounter(counter ?: 0)
    }

}