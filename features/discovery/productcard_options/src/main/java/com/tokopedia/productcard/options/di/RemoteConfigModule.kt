package com.tokopedia.productcard.options.di

import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import dagger.Module
import dagger.Provides

@Module
object RemoteConfigModule {
    @Provides
    @JvmStatic
    @ProductCardOptionsScope
    fun provideAbTestRemoteConfig() : RemoteConfig {
        return RemoteConfigInstance.getInstance().abTestPlatform
    }
}
