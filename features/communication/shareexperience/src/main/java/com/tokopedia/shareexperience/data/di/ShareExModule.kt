package com.tokopedia.shareexperience.data.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shareexperience.data.util.ShareExResourceProvider
import com.tokopedia.shareexperience.data.util.ShareExResourceProviderImpl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
object ShareExModule {

    @Provides
    @ActivityScope
    fun provideResourceProvider(
        @ApplicationContext context: Context
    ): ShareExResourceProvider {
        return ShareExResourceProviderImpl(context)
    }

    @Provides
    @ActivityScope
    fun provideRemoteConfig(
        @ApplicationContext context: Context
    ): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @Provides
    @ActivityScope
    fun provideUserSession(
        @ApplicationContext context: Context
    ): UserSessionInterface {
        return UserSession(context)
    }
}
