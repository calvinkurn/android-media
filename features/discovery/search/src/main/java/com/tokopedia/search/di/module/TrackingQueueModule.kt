package com.tokopedia.search.di.module

import android.content.Context
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.trackingoptimizer.TrackingQueue
import dagger.Module
import dagger.Provides

@Module
class TrackingQueueModule {

    @Provides
    @SearchScope
    fun provideTrackingQueue(@SearchContext context: Context): TrackingQueue {
        return TrackingQueue(context)
    }
}