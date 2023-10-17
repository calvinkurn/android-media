package com.tokopedia.review.feature.media.gallery.detailed.di.component

import android.graphics.Bitmap
import android.util.LruCache
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.review.feature.media.detail.analytic.ReviewDetailTracker
import com.tokopedia.review.feature.media.detail.di.module.ReviewDetailTrackerModule
import com.tokopedia.review.feature.media.gallery.base.analytic.ReviewMediaGalleryTracker
import com.tokopedia.review.feature.media.gallery.base.di.module.ReviewMediaGalleryTrackerModule
import com.tokopedia.review.feature.media.gallery.detailed.di.module.DetailedReviewMediaGalleryModule
import com.tokopedia.review.feature.media.gallery.detailed.di.module.DetailedReviewMediaGalleryViewModelModule
import com.tokopedia.review.feature.media.gallery.detailed.di.qualifier.DetailedReviewMediaGalleryViewModelFactory
import com.tokopedia.review.feature.media.gallery.detailed.di.scope.DetailedReviewMediaGalleryScope
import com.tokopedia.review.feature.media.gallery.detailed.presentation.activity.DetailedReviewMediaGalleryActivity
import com.tokopedia.review.feature.media.gallery.detailed.presentation.bottomsheet.ActionMenuBottomSheet
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.widget.ReviewVideoPlayer
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component

@Component(
    modules = [
        DetailedReviewMediaGalleryViewModelModule::class,
        DetailedReviewMediaGalleryModule::class,
        ReviewDetailTrackerModule::class,
        ReviewMediaGalleryTrackerModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
@DetailedReviewMediaGalleryScope
interface DetailedReviewMediaGalleryComponent {
    fun inject(activity: DetailedReviewMediaGalleryActivity)
    fun inject(bottomSheet: ActionMenuBottomSheet)
    @DetailedReviewMediaGalleryViewModelFactory
    fun viewModelFactory(): ViewModelProvider.Factory
    fun trackingQueue(): TrackingQueue
    fun userSession(): UserSessionInterface
    fun reviewVideoPlayer(): ReviewVideoPlayer
    fun bitmapCache(): LruCache<String, Bitmap>
    fun reviewDetailTracker(): ReviewDetailTracker
    fun reviewMediaGalleryTracker(): ReviewMediaGalleryTracker
}
