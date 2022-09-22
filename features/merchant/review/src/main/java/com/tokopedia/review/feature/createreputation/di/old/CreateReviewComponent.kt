package com.tokopedia.review.feature.createreputation.di.old


import com.tokopedia.mediauploader.common.di.MediaUploaderModule
import com.tokopedia.review.common.di.ReviewComponent
import com.tokopedia.review.feature.createreputation.presentation.fragment.CreateReviewFragment
import dagger.Component

@Component(modules = [CreateReviewViewModelModule::class, MediaUploaderModule::class, CreateReviewModule::class], dependencies = [ReviewComponent::class])
@CreateReviewScope
interface CreateReviewComponent {
    fun inject(createReviewFragment: CreateReviewFragment)
}