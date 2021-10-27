package com.tokopedia.checkout.view.subscriber

import com.tokopedia.checkout.domain.model.saveshipmentstate.SaveShipmentStateData
import rx.Subscriber
import timber.log.Timber

class SaveShipmentStateSubscriber : Subscriber<SaveShipmentStateData>() {
    override fun onCompleted() {}
    override fun onError(e: Throwable) {
        Timber.d(e)
    }

    override fun onNext(saveShipmentStateData: SaveShipmentStateData) {
        // No-op
    }
}