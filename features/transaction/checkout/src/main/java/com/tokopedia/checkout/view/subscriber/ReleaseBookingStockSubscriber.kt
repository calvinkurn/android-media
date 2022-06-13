package com.tokopedia.checkout.view.subscriber

import com.tokopedia.checkout.data.model.response.ReleaseBookingResponse
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