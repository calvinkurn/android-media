package com.tokopedia.review.feature.reading.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.trackingoptimizer.TrackingQueue
import dagger.Module
import dagger.Provides

@Module
class ReadReviewModule {

    @ReadReviewScope
    @Provides
    fun provideTrackingQueue(@ApplicationContext context: Context) : TrackingQueue {
        return TrackingQueue(context)
    }
}