package com.tokopedia.review.feature.reading.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.review.feature.reading.domain.usecase.GetProductRatingAndTopicsUseCase
import com.tokopedia.review.feature.reading.domain.usecase.GetProductReviewListUseCase
import com.tokopedia.review.common.domain.usecase.ToggleLikeReviewUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class ReadReviewViewModelTestFixture {

    @RelaxedMockK
    lateinit var getProductRatingAndTopicsUseCase: GetProductRatingAndTopicsUseCase

    @RelaxedMockK
    lateinit var getProductReviewListUseCase: GetProductReviewListUseCase

    @RelaxedMockK
    lateinit var toggleLikeReviewUseCase: ToggleLikeReviewUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: ReadReviewViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ReadReviewViewModel(getProductRatingAndTopicsUseCase, getProductReviewListUseCase, toggleLikeReviewUseCase, CoroutineTestDispatchersProvider)
        viewModel.productReviews.observeForever {  }
        viewModel.ratingAndTopic.observeForever {  }
    }
}