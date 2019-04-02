package com.tokopedia.checkout.view.feature.shipment.di;

import com.tokopedia.checkout.view.di.component.CartComponent;
import com.tokopedia.checkout.view.feature.shipment.ShipmentFragment;

import dagger.Component;

/**
 * Created by Irfan Khoirul on 09/02/18.
 */

@ShipmentScope
@Component(modules = ShipmentModule.class, dependencies = CartComponent.class)
public interface ShipmentComponent {
    void inject(ShipmentFragment shipmentFragment);
}