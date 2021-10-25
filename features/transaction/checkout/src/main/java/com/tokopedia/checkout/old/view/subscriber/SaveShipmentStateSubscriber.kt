package com.tokopedia.checkout.old.view.subscriber

import com.tokopedia.checkout.old.domain.model.saveshipmentstate.SaveShipmentStateData
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