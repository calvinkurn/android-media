package com.tokopedia.checkout.view.di.component;

import com.tokopedia.checkout.view.di.module.MultipleAddressShipmentModule;
import com.tokopedia.checkout.view.di.scope.MultipleAddressShipmentScope;
import com.tokopedia.checkout.view.view.shipmentform.MultipleAddressShipmentFragment;

import dagger.Component;

/**
 * Created by kris on 2/5/18. Tokopedia
 */
@MultipleAddressShipmentScope
@Component(modules = MultipleAddressShipmentModule.class, dependencies = CartComponent.class)
public interface MultipleAddressShipmentComponent {

    void inject(MultipleAddressShipmentFragment multipleAddressShipmentFragment);
}
