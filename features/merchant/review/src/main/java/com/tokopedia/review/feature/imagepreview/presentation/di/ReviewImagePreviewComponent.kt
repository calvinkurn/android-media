package com.tokopedia.review.feature.imagepreview.presentation.di

import com.tokopedia.review.common.di.ReviewComponent
import com.tokopedia.review.feature.imagepreview.presentation.fragment.ReviewImagePreviewFragment
import dagger.Component

@Component(modules = [ReviewImagePreviewViewModelModule::class, ReviewImagePreviewModule::class], dependencies = [ReviewComponent::class])
@ReviewImagePreviewScope
interface ReviewImagePreviewComponent {
    fun inject(reviewImagePreviewFragment: ReviewImagePreviewFragment)
}