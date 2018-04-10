package com.tokopedia.checkout.view.di.component;

import com.tokopedia.checkout.view.di.module.ShipmentChoiceModule;
import com.tokopedia.checkout.view.di.scope.ShipmentChoiceScope;
import com.tokopedia.checkout.view.view.shippingoptions.ShipmentChoiceBottomSheet;

import dagger.Component;

/**
 * Created by Irfan Khoirul on 09/02/18.
 */

@ShipmentChoiceScope
@Component(modules = ShipmentChoiceModule.class)
public interface ShipmentChoiceComponent {
    void inject(ShipmentChoiceBottomSheet shipmentChoiceBottomSheet);
}