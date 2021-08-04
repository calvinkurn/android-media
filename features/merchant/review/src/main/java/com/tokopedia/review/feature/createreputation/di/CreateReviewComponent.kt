package com.tokopedia.review.feature.createreputation.di


import com.tokopedia.mediauploader.di.MediaUploaderModule
import com.tokopedia.review.common.di.ReviewComponent
import com.tokopedia.review.feature.createreputation.presentation.bottomsheet.CreateReviewBottomSheet
import com.tokopedia.review.feature.createreputation.presentation.fragment.CreateReviewFragment
import dagger.Component

@Component(modules = [CreateReviewViewModelModule::class, MediaUploaderModule::class], dependencies = [ReviewComponent::class])
@CreateReviewScope
interface CreateReviewComponent {
    fun inject(createReviewFragment: CreateReviewFragment)
    fun inject(createReviewBottomSheet: CreateReviewBottomSheet)
}