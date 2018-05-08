package com.tokopedia.checkout.view.view.shipment.shippingoptions.di;

import com.tokopedia.checkout.view.di.component.CartComponent;
import com.tokopedia.checkout.view.view.shipment.shippingoptions.CourierBottomsheet;

import dagger.Component;

/**
 * @author Irfan Khoirul on 04/05/18.
 */

@CourierScope
@Component(modules = CourierModule.class, dependencies = CartComponent.class)
public interface CourierComponent {
    void inject(CourierBottomsheet courierBottomsheet);
}
