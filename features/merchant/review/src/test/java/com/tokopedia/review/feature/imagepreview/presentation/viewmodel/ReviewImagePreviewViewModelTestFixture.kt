package com.tokopedia.review.feature.imagepreview.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.review.common.domain.usecase.ToggleLikeReviewUseCase
import com.tokopedia.review.feature.gallery.domain.usecase.GetReviewImagesUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class ReviewImagePreviewViewModelTestFixture {

    @RelaxedMockK
    lateinit var toggleLikeReviewUseCase: ToggleLikeReviewUseCase

    @RelaxedMockK
    lateinit var getReviewImagesUseCase: GetReviewImagesUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: ReviewImagePreviewViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ReviewImagePreviewViewModel(toggleLikeReviewUseCase, getReviewImagesUseCase, CoroutineTestDispatchersProvider)
        viewModel.reviewImages.observeForever {  }
    }
}