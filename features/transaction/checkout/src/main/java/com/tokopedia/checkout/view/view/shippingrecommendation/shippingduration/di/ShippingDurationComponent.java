package com.tokopedia.checkout.view.view.shippingrecommendation.shippingduration.di;

import com.tokopedia.checkout.view.di.component.CartComponent;
import com.tokopedia.checkout.view.view.shippingrecommendation.shippingduration.view.ShippingDurationBottomsheet;

import dagger.Component;

/**
 * Created by Irfan Khoirul on 07/08/18.
 */

@ShippingDurationScope
@Component(modules = ShippingDurationModule.class, dependencies = CartComponent.class)
public interface ShippingDurationComponent {
    void inject(ShippingDurationBottomsheet shippingDurationBottomsheet);
}
