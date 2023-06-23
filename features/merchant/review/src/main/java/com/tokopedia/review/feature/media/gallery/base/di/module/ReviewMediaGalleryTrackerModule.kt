package com.tokopedia.review.feature.media.gallery.base.di.module

import com.tokopedia.review.feature.media.gallery.base.analytic.ReviewMediaGalleryTracker
import com.tokopedia.review.feature.media.gallery.base.analytic.ReviewMediaGalleryTrackerImpl
import com.tokopedia.review.feature.media.gallery.base.di.scope.ReviewMediaGalleryScope
import com.tokopedia.trackingoptimizer.TrackingQueue
import dagger.Module
import dagger.Provides

/**
 * Created By : Jonathan Darwin on May 29, 2023
 */
@Module
class ReviewMediaGalleryTrackerModule() {

    @Provides
    @ReviewMediaGalleryScope
    fun provideReviewMediaGalleryTracker(trackingQueue: TrackingQueue): ReviewMediaGalleryTracker {
        return ReviewMediaGalleryTrackerImpl(trackingQueue)
    }
}
