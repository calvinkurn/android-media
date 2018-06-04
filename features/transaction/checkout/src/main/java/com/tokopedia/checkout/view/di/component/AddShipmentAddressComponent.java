package com.tokopedia.checkout.view.di.component;

import com.tokopedia.checkout.view.di.module.AddShipmentAddressModule;
import com.tokopedia.checkout.view.di.scope.AddShipmentAddressScope;
import com.tokopedia.checkout.view.view.multipleaddressform.AddShipmentAddressActivity;
import com.tokopedia.checkout.view.view.multipleaddressform.AddShipmentAddressFragment;

import dagger.Component;

/**
 * Created by kris on 3/1/18. Tokopedia
 */

@AddShipmentAddressScope
@Component(modules = AddShipmentAddressModule.class)
public interface AddShipmentAddressComponent {
    void inject(AddShipmentAddressFragment addShipmentAddressFragment);
}
