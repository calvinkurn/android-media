package com.tokopedia.shipping_recommendation.shippingduration.di;

import com.tokopedia.shipping_recommendation.shippingduration.view.ShippingDurationBottomsheet;

import dagger.Component;

/**
 * Created by Irfan Khoirul on 07/08/18.
 */

@ShippingDurationScope
@Component(modules = ShippingDurationModule.class)
public interface ShippingDurationComponent {
    void inject(ShippingDurationBottomsheet shippingDurationBottomsheet);
}
