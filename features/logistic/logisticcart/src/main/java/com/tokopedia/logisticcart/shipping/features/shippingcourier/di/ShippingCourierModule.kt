package com.tokopedia.logisticcart.shipping.features.shippingcourier.di

import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierAdapter
import dagger.Module
import dagger.Provides

/**
 * Created by Irfan Khoirul on 08/08/18.
 */
@Module
class ShippingCourierModule {
    @Provides
    @ShippingCourierScope
    fun provideShippingCourierAdapter(): ShippingCourierAdapter {
        return ShippingCourierAdapter()
    }
}