package com.tokopedia.review.feature.gallery.presentation.di

import com.tokopedia.review.common.di.ReviewComponent
import com.tokopedia.review.feature.gallery.presentation.fragment.ReviewGalleryFragment
import com.tokopedia.review.feature.gallery.presentation.fragment.ReviewGridGalleryFragment
import dagger.Component

@Component(modules = [ReviewGalleryViewModelModule::class, ReviewGridGalleryViewModelModule::class], dependencies = [ReviewComponent::class])
@ReviewGalleryScope
interface ReviewGalleryComponent {
    fun inject(reviewGalleryFragment: ReviewGalleryFragment)
    fun inject(reviewGalleryFragment: ReviewGridGalleryFragment)
}