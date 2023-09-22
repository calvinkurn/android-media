package com.tokopedia.logisticcart.shipping.features.shippingduration.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationAdapter
import dagger.Module
import dagger.Provides

/**
 * Created by Irfan Khoirul on 07/08/18.
 */
@Module
class ShippingDurationModule {
    @Provides
    @ActivityScope
    fun provideShippingDurationAdapter(): ShippingDurationAdapter {
        return ShippingDurationAdapter()
    }
}
