package com.tokopedia.logisticcart.shipping.features.shippingduration.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationAdapter
import com.tokopedia.user.session.UserSessionInterface
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

    @Provides
    @ActivityScope
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = com.tokopedia.user.session.UserSession(context)
}
