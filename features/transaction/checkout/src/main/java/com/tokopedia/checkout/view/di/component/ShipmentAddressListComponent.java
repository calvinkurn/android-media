package com.tokopedia.checkout.view.di.component;

import com.tokopedia.checkout.view.di.module.ShipmentAddressListModule;
import com.tokopedia.checkout.view.di.scope.ShipmentAddressListScope;
import com.tokopedia.checkout.view.feature.addressoptions.ShipmentAddressListFragment;
import com.tokopedia.checkout.view.feature.cornerlist.CornerListFragment;

import dagger.Component;

/**
 * @author Aghny A. Putra on 31/01/18.
 */

@ShipmentAddressListScope
@Component(modules = ShipmentAddressListModule.class, dependencies = CartComponent.class)
public interface ShipmentAddressListComponent {

    void inject(ShipmentAddressListFragment shipmentAddressListFragment);

    void inject(CornerListFragment shipmentAddressListFragment);

}
