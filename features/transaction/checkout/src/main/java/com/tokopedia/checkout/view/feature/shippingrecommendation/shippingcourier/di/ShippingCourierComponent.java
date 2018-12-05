package com.tokopedia.checkout.view.feature.shippingrecommendation.shippingcourier.di;

import com.tokopedia.checkout.view.feature.shippingrecommendation.shippingcourier.view.ShippingCourierBottomsheet;

import dagger.Component;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

@ShippingCourierScope
@Component(modules = ShippingCourierModule.class)
public interface ShippingCourierComponent {
    void inject(ShippingCourierBottomsheet shippingCourierBottomsheet);
}
