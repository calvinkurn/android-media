package com.tokopedia.purchase_platform.features.checkout.view.subscriber;

import com.tokopedia.purchase_platform.features.checkout.domain.model.saveshipmentstate.SaveShipmentStateData;
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentContract;

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
