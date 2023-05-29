package com.tokopedia.review.feature.media.gallery.detailed.di.component

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.review.feature.media.detail.di.module.ReviewDetailTrackerModule
import com.tokopedia.review.feature.media.gallery.detailed.di.module.DetailedReviewMediaGalleryModule
import com.tokopedia.review.feature.media.gallery.detailed.di.module.DetailedReviewMediaGalleryViewModelModule
import com.tokopedia.review.feature.media.gallery.detailed.di.qualifier.DetailedReviewMediaGalleryViewModelFactory
import com.tokopedia.review.feature.media.gallery.detailed.di.scope.DetailedReviewMediaGalleryScope
import com.tokopedia.review.feature.media.gallery.detailed.presentation.activity.DetailedReviewMediaGalleryActivity
import com.tokopedia.review.feature.media.gallery.detailed.presentation.bottomsheet.ActionMenuBottomSheet
import com.tokopedia.trackingoptimizer.TrackingQueue
import dagger.Component

@Component(
    modules = [
        DetailedReviewMediaGalleryViewModelModule::class,
        DetailedReviewMediaGalleryModule::class,
        ReviewDetailTrackerModule::class,
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
}
