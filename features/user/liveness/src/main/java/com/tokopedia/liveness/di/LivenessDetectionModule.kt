package com.tokopedia.liveness.di

import com.tokopedia.liveness.analytics.LivenessDetectionAnalytics
import dagger.Module
import dagger.Provides

@Module
class LivenessDetectionModule {

    @LivenessDetectionScope
    @Provides
    fun provideAnalytics(): LivenessDetectionAnalytics {
        return LivenessDetectionAnalytics()
    }
}