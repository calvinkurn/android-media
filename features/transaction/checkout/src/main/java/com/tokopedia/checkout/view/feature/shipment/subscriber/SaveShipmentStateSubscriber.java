package com.tokopedia.checkout.view.feature.shipment.subscriber;

import com.tokopedia.checkout.domain.datamodel.saveshipmentstate.SaveShipmentStateData;
import com.tokopedia.checkout.view.feature.shipment.ShipmentContract;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 25/09/18.
 */

public class SaveShipmentStateSubscriber extends Subscriber<SaveShipmentStateData> {

    private final ShipmentContract.View view;

    public SaveShipmentStateSubscriber(ShipmentContract.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onNext(SaveShipmentStateData saveShipmentStateData) {
        // Expected to do nothing
    }

}
