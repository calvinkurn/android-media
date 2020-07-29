package com.tokopedia.review.feature.inbox.pending

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.review.coroutine.TestCoroutineDispatchers
import com.tokopedia.review.feature.createreputation.domain.usecase.GetProductIncentiveOvo
import com.tokopedia.review.feature.inbox.pending.domain.usecase.ProductrevWaitForFeedbackUseCase
import com.tokopedia.review.feature.inbox.pending.presentation.viewmodel.ReviewPendingViewModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class ReviewPendingViewModelTestFixture {

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var productrevWaitForFeedbackUseCase: ProductrevWaitForFeedbackUseCase

    @RelaxedMockK
    lateinit var getProductIncentiveOvo: GetProductIncentiveOvo

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: ReviewPendingViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ReviewPendingViewModel(TestCoroutineDispatchers, userSession, productrevWaitForFeedbackUseCase, getProductIncentiveOvo)
    }
}