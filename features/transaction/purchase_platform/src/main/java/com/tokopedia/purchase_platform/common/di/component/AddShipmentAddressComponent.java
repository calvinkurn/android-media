package com.tokopedia.purchase_platform.common.di.component;

import com.tokopedia.purchase_platform.common.di.module.AddShipmentAddressModule;
import com.tokopedia.purchase_platform.common.di.scope.AddShipmentAddressScope;
import com.tokopedia.purchase_platform.checkout.view.feature.multipleaddressform.AddShipmentAddressFragment;

import dagger.Component;

/**
 * Created by kris on 3/1/18. Tokopedia
 */

@AddShipmentAddressScope
@Component(modules = AddShipmentAddressModule.class, dependencies = CartComponent.class)
public interface AddShipmentAddressComponent {
    void inject(AddShipmentAddressFragment addShipmentAddressFragment);
}
