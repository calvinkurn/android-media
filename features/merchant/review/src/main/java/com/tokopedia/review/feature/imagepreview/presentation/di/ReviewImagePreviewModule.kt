package com.tokopedia.review.feature.imagepreview.presentation.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.trackingoptimizer.TrackingQueue
import dagger.Module
import dagger.Provides

@Module
class ReviewImagePreviewModule {

    @ReviewImagePreviewScope
    @Provides
    fun provideTrackingQueue(@ApplicationContext context: Context) : TrackingQueue {
        return TrackingQueue(context)
    }
}