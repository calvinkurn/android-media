package com.tokopedia.checkout.view.view.shippingrecommendation.di;

import com.tokopedia.checkout.view.di.component.CartComponent;
import com.tokopedia.checkout.view.view.shippingoptions.di.CourierScope;
import com.tokopedia.checkout.view.view.shippingrecommendation.ShipmentRecommendationDurationBottomsheet;

import dagger.Component;

/**
 * Created by Irfan Khoirul on 07/08/18.
 */

@ShipmentRecommendationScope
@Component(modules = ShipmentRecommendationModule.class, dependencies = CartComponent.class)
public interface ShipmentRecommendationComponent {
    void inject(ShipmentRecommendationDurationBottomsheet shipmentRecommendationDurationBottomsheet);
}
