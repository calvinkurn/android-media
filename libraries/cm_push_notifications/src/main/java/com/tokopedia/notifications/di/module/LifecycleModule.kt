package com.tokopedia.notifications.di.module

import android.app.Application
import com.tokopedia.notifications.di.scope.CMNotificationScope
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import dagger.Module
import dagger.Provides

@Module class LifecycleModule(private val application: Application) {

    @Provides
    @CMNotificationScope
    fun provideRemoteConfig(): RemoteConfig {
        return FirebaseRemoteConfigImpl(application.applicationContext)
    }

}