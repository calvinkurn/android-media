package com.tokopedia.search.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class RemoteConfigModule {
    @SearchScope
    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context?): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @SearchScope
    @Provides
    @Named(SearchConstant.AB_TEST_REMOTE_CONFIG)
    fun provideAbTestRemoteConfig() : RemoteConfig {
        return RemoteConfigInstance.getInstance().abTestPlatform
    }
}