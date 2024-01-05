package com.tokopedia.tokopedianow.searchcategory.di

import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import dagger.Module
import dagger.Provides

@Module
class RemoteConfigModule {
    @Provides
    fun provideRemoteConfig(): RemoteConfig = RemoteConfigInstance.getInstance().abTestPlatform
}
