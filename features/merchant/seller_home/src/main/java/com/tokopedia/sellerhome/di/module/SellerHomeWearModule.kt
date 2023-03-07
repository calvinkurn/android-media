package com.tokopedia.sellerhome.di.module

import android.content.Context
import androidx.wear.remote.interactions.RemoteActivityHelper
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.NodeClient
import com.google.android.gms.wearable.Wearable
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.sellerhome.di.scope.SellerHomeScope
import dagger.Module
import dagger.Provides

@Module
class SellerHomeWearModule {

    @SellerHomeScope
    @Provides
    fun provideCapabilityClient(
        @ApplicationContext context: Context
    ): CapabilityClient = Wearable.getCapabilityClient(context)

    @SellerHomeScope
    @Provides
    fun provideNodeClient(
        @ApplicationContext context: Context
    ): NodeClient = Wearable.getNodeClient(context)

    @SellerHomeScope
    @Provides
    fun provideRemoteActivityHelper(
        @ApplicationContext context: Context
    ): RemoteActivityHelper = RemoteActivityHelper(context)
}