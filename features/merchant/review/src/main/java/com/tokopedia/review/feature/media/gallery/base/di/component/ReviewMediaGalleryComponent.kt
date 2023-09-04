package com.tokopedia.review.feature.media.gallery.base.di.component

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.review.feature.media.gallery.base.di.module.ReviewMediaGalleryModule
import com.tokopedia.review.feature.media.gallery.base.di.module.ReviewMediaGalleryViewModelModule
import com.tokopedia.review.feature.media.gallery.base.di.qualifier.ReviewMediaGalleryViewModelFactory
import com.tokopedia.review.feature.media.gallery.base.di.scope.ReviewMediaGalleryScope
import com.tokopedia.review.feature.media.gallery.base.presentation.fragment.ReviewMediaGalleryFragment
import com.tokopedia.review.feature.media.gallery.detailed.di.component.DetailedReviewMediaGalleryComponent
import dagger.Component

@Component(
    modules = [ReviewMediaGalleryViewModelModule::class, ReviewMediaGalleryModule::class],
    dependencies = [BaseAppComponent::class, DetailedReviewMediaGalleryComponent::class]
)
@ReviewMediaGalleryScope
interface ReviewMediaGalleryComponent {
    fun inject(fragment: ReviewMediaGalleryFragment)

    @ReviewMediaGalleryViewModelFactory
    fun viewModelFactory(): ViewModelProvider.Factory
}
