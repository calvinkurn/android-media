package com.tokopedia.logisticcart.shippingcourier.di;

import com.tokopedia.logisticcart.shippingcourier.view.ShippingCourierBottomsheet;

import dagger.Component;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

@ShippingCourierScope
@Component(modules = ShippingCourierModule.class)
public interface ShippingCourierComponent {
    void inject(ShippingCourierBottomsheet shippingCourierBottomsheet);
}
