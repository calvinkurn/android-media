package com.tokopedia.notifications.di.module

import android.content.Context
import com.tokopedia.notifications.di.scope.CMNotificationScope
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import dagger.Module
import dagger.Provides

@Module class LifecycleModule(private val context: Context) {

    @Provides
    @CMNotificationScope
    fun provideRemoteConfig(): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

}