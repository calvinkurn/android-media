package com.tokopedia.review.feature.media.detail.di.module

import com.tokopedia.review.feature.media.detail.analytic.ReviewDetailTrackerImpl
import com.tokopedia.review.feature.media.detail.analytic.ReviewDetailTracker
import com.tokopedia.review.feature.media.detail.analytic.ReviewDetailUserProfileTrackerImpl
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.util.ReviewMediaGalleryRouter
import dagger.Module
import dagger.Provides

/**
 * Created By : Jonathan Darwin on May 29, 2023
 */
@Module
class ReviewDetailTrackerModule(
    @ReviewMediaGalleryRouter.PageSource private val pageSource: Int,
) {

    @Provides
    fun provideReviewDetailTracker(): ReviewDetailTracker {
        return if (pageSource == ReviewMediaGalleryRouter.PageSource.USER_PROFILE) {
            ReviewDetailUserProfileTrackerImpl()
        } else {
            ReviewDetailTrackerImpl()
        }
    }
}
