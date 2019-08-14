package com.tokopedia.purchase_platform.common.di.component;

import com.tokopedia.purchase_platform.common.di.module.ShipmentAddressListModule;
import com.tokopedia.purchase_platform.common.di.scope.ShipmentAddressListScope;
import com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.view.ShipmentAddressListFragment;
import com.tokopedia.purchase_platform.features.checkout.subfeature.corner_list.CornerListFragment;

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
