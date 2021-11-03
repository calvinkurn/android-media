package com.tokopedia.review.feature.gallery.di

import com.tokopedia.review.common.di.ReviewComponent
import com.tokopedia.review.feature.gallery.presentation.fragment.ReviewGalleryFragment
import com.tokopedia.review.feature.imagepreview.presentation.di.ReviewImagePreviewScope
import dagger.Component

@Component(modules = [ReviewGalleryViewModelModule::class], dependencies = [ReviewComponent::class])
@ReviewImagePreviewScope
interface ReviewGalleryComponent {
    fun inject(reviewGalleryFragment: ReviewGalleryFragment)
}