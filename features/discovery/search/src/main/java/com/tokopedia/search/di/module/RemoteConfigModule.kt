package com.tokopedia.search.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides

@Module
class RemoteConfigModule {
    @SearchScope
    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context?): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }
}