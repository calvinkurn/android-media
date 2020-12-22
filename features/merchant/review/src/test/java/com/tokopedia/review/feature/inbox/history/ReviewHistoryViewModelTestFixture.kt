package com.tokopedia.review.feature.inbox.history

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.review.feature.inbox.history.domain.usecase.ProductrevFeedbackHistoryUseCase
import com.tokopedia.review.feature.inbox.history.presentation.viewmodel.ReviewHistoryViewModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class ReviewHistoryViewModelTestFixture {

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var productrevFeedbackHistoryUseCase: ProductrevFeedbackHistoryUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: ReviewHistoryViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ReviewHistoryViewModel(CoroutineTestDispatchersProvider, userSession, productrevFeedbackHistoryUseCase)
        viewModel.reviewList.observeForever {  }
    }
}