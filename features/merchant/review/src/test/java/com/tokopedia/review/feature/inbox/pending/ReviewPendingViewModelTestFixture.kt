package com.tokopedia.review.feature.inbox.pending

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.review.coroutines.TestCoroutineDispatchers
import com.tokopedia.review.feature.inbox.pending.domain.usecase.ProductrevWaitForFeedbackUseCase
import com.tokopedia.review.feature.inbox.pending.presentation.viewmodel.ReviewPendingViewModel
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class ReviewPendingViewModelTestFixture {

    @RelaxedMockK
    lateinit var productrevWaitForFeedbackUseCase: ProductrevWaitForFeedbackUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: ReviewPendingViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ReviewPendingViewModel(TestCoroutineDispatchers, productrevWaitForFeedbackUseCase)
        viewModel.reviewViewState.observeForever{}
    }
}