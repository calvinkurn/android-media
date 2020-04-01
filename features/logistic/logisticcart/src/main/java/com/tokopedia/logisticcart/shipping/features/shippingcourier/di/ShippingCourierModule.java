package com.tokopedia.logisticcart.shipping.features.shippingcourier.di;

import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierAdapter;
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

@Module
public class ShippingCourierModule {

    @Provides
    @ShippingCourierScope
    ShippingCourierAdapter provideShippingCourierAdapter() {
        return new ShippingCourierAdapter();
    }

    @Provides
    @ShippingCourierScope
    ShippingCourierConverter provideShippingCourierConverter() {
        return new ShippingCourierConverter();
    }

}
