package com.tokopedia.checkout.view.di.component;

import com.tokopedia.checkout.view.di.module.ShipmentDetailModule;
import com.tokopedia.checkout.view.di.scope.ShipmentDetailScope;
import com.tokopedia.checkout.view.view.shippingoptions.ShipmentDetailFragment;

import dagger.Component;

/**
 * Created by Irfan Khoirul on 09/02/18.
 */

@ShipmentDetailScope
@Component(modules = ShipmentDetailModule.class, dependencies = CartComponent.class)
public interface ShipmentDetailComponent {
    void inject(ShipmentDetailFragment shipmentDetailFragment);
}
