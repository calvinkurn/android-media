package com.tokopedia.review.feature.historydetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.review.common.domain.usecase.ProductrevGetReviewDetailUseCase
import com.tokopedia.review.coroutine.TestCoroutineDispatchers
import com.tokopedia.review.feature.historydetails.presentation.viewmodel.ReviewDetailViewModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class ReviewDetailViewModelTestFixture {

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var productrevGetReviewDetailUseCase: ProductrevGetReviewDetailUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: ReviewDetailViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ReviewDetailViewModel(TestCoroutineDispatchers, userSession, productrevGetReviewDetailUseCase)
        viewModel.reviewDetails.observeForever {  }
    }
}