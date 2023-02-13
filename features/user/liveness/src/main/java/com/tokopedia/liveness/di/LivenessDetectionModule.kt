package com.tokopedia.liveness.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.liveness.analytics.LivenessDetectionAnalytics
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import dagger.Module
import dagger.Provides

@Module
class LivenessDetectionModule {

    @ActivityScope
    @Provides
    fun provideAnalytics(): LivenessDetectionAnalytics {
        return LivenessDetectionAnalytics()
    }

    @ActivityScope
    @Provides
    fun provideFirebaseRemoteConfig(@ApplicationContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }
}
