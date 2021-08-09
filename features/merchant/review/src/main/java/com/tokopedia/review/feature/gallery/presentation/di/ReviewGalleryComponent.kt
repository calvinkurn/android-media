package com.tokopedia.review.feature.gallery.presentation.di

import com.tokopedia.review.common.di.ReviewComponent
import com.tokopedia.review.feature.gallery.presentation.fragment.ReviewGalleryFragment
import dagger.Component

@Component(modules = [ReviewGalleryViewModelModule::class], dependencies = [ReviewComponent::class])
@ReviewGalleryScope
interface ReviewGalleryComponent {
    fun inject(reviewGalleryFragment: ReviewGalleryFragment)
}