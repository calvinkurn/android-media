package com.tokopedia.checkout.view.di.component;

import com.tokopedia.checkout.view.di.module.AddShipmentAddressModule;
import com.tokopedia.checkout.view.di.scope.AddShipmentAddressScope;
import com.tokopedia.checkout.view.feature.multipleaddressform.AddShipmentAddressFragment;

import dagger.Component;

/**
 * Created by kris on 3/1/18. Tokopedia
 */

@AddShipmentAddressScope
@Component(modules = AddShipmentAddressModule.class, dependencies = CartComponent.class)
public interface AddShipmentAddressComponent {
    void inject(AddShipmentAddressFragment addShipmentAddressFragment);
}
