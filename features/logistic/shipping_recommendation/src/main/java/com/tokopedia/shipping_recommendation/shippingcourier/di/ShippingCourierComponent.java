package com.tokopedia.shipping_recommendation.shippingcourier.di;

import com.tokopedia.shipping_recommendation.shippingcourier.view.ShippingCourierBottomsheet;

import dagger.Component;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

@ShippingCourierScope
@Component(modules = ShippingCourierModule.class)
public interface ShippingCourierComponent {
    void inject(ShippingCourierBottomsheet shippingCourierBottomsheet);
}
