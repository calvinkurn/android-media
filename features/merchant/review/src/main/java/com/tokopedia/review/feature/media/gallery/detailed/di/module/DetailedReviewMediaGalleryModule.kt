package com.tokopedia.review.feature.media.gallery.detailed.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.review.feature.media.gallery.detailed.di.scope.DetailedReviewMediaGalleryScope
import com.tokopedia.trackingoptimizer.TrackingQueue
import dagger.Module
import dagger.Provides

@Module
class DetailedReviewMediaGalleryModule {
    @Provides
    @DetailedReviewMediaGalleryScope
    fun provideTrackingQueue(@ApplicationContext context: Context): TrackingQueue {
        return TrackingQueue(context)
    }
}