package com.tokopedia.checkout.view.view.shipment.shippingoptions.di;

import com.tokopedia.checkout.view.view.shipment.shippingoptions.CourierBottomsheet;

import dagger.Component;

@CourierScope
@Component(modules = CourierModule.class)
public interface CourierComponent {
    void inject(CourierBottomsheet courierBottomsheet);
}
