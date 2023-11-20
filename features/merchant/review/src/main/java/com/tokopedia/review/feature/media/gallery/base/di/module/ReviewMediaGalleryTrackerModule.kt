package com.tokopedia.review.feature.media.gallery.base.di.module

import com.tokopedia.review.feature.media.gallery.base.analytic.ReviewMediaGalleryTracker
import com.tokopedia.review.feature.media.gallery.base.analytic.ReviewMediaGalleryTrackerImpl
import com.tokopedia.review.feature.media.gallery.base.analytic.ReviewMediaGalleryUserProfileTrackerImpl
import com.tokopedia.review.feature.media.gallery.detailed.di.scope.DetailedReviewMediaGalleryScope
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.util.ReviewMediaGalleryRouter
import com.tokopedia.trackingoptimizer.TrackingQueue
import dagger.Module
import dagger.Provides

/**
 * Created By : Jonathan Darwin on May 29, 2023
 */
@Module
class ReviewMediaGalleryTrackerModule(
    @ReviewMediaGalleryRouter.PageSource private val pageSource: Int,
) {

    @Provides
    @DetailedReviewMediaGalleryScope
    fun provideReviewMediaGalleryTracker(trackingQueue: TrackingQueue): ReviewMediaGalleryTracker {
        return if (pageSource == ReviewMediaGalleryRouter.PageSource.USER_PROFILE) {
            ReviewMediaGalleryUserProfileTrackerImpl(trackingQueue)
        } else {
            ReviewMediaGalleryTrackerImpl(trackingQueue)
        }
    }
}
