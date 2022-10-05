package com.tokopedia.autocompletecomponent.universal.di

import android.content.Context
import com.tokopedia.trackingoptimizer.TrackingQueue
import dagger.Module
import dagger.Provides

@Module
class UniversalTrackingQueueModule {

    @UniversalSearchScope
    @Provides
    fun provideTrackingQueue(
        @UniversalSearchContext context: Context
    ): TrackingQueue {
        return TrackingQueue(context)
    }
}