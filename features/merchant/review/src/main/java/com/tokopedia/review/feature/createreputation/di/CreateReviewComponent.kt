package com.tokopedia.review.feature.createreputation.di

import com.tokopedia.mediauploader.common.di.MediaUploaderModule
import com.tokopedia.review.common.di.ReviewComponent
import com.tokopedia.review.feature.createreputation.presentation.bottomsheet.CreateReviewBottomSheet
import com.tokopedia.review.feature.createreputation.presentation.bottomsheet.CreateReviewTextAreaBottomSheet
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
@Component(modules = [CreateReviewViewModelModule::class, MediaUploaderModule::class, CreateReviewModule::class], dependencies = [ReviewComponent::class])
@CreateReviewScope
interface CreateReviewComponent {
    fun inject(createReviewBottomSheet: CreateReviewBottomSheet)
    fun inject(createReviewTextAreaBottomSheet: CreateReviewTextAreaBottomSheet)
}