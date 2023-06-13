package com.tokopedia.sessioncommon.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class RefreshProfileModule {
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    fun provideFirebaseRemoteConfig(@ApplicationContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }
}
