package com.tokopedia.review.feature.gallery.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.review.feature.gallery.domain.usecase.GetReviewImagesUseCase
import com.tokopedia.review.feature.reading.domain.usecase.GetProductRatingAndTopicsUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class ReviewGalleryViewModelTestFixture {

    @RelaxedMockK
    lateinit var getProductRatingAndTopicsUseCase: GetProductRatingAndTopicsUseCase

    @RelaxedMockK
    lateinit var getReviewImagesUseCase: GetReviewImagesUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: ReviewGalleryViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ReviewGalleryViewModel(
            getProductRatingAndTopicsUseCase,
            getReviewImagesUseCase,
            CoroutineTestDispatchersProvider
        )
    }
}