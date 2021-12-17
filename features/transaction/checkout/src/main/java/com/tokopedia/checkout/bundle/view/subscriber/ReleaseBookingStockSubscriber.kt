package com.tokopedia.checkout.bundle.view.subscriber

import com.tokopedia.checkout.bundle.data.model.response.ReleaseBookingResponse
import rx.Subscriber
import timber.log.Timber

class ReleaseBookingStockSubscriber : Subscriber<ReleaseBookingResponse>() {
    override fun onCompleted() {}
    override fun onError(e: Throwable) {
        Timber.d(e)
    }

    override fun onNext(data: ReleaseBookingResponse) {
        // No-op
    }
}