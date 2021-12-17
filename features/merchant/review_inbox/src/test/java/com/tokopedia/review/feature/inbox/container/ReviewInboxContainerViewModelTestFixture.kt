package com.tokopedia.review.feature.inbox.container

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.reputation.common.domain.usecase.ProductrevReviewTabCounterUseCase
import com.tokopedia.review.feature.inbox.container.presentation.viewmodel.ReviewInboxContainerViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkStatic
import org.junit.Before
import org.junit.Rule

abstract class ReviewInboxContainerViewModelTestFixture {

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var productrevReviewTabCounterUseCase: ProductrevReviewTabCounterUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: ReviewInboxContainerViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkStatic(RemoteConfigInstance::class)
        viewModel = ReviewInboxContainerViewModel(userSession, CoroutineTestDispatchersProvider, productrevReviewTabCounterUseCase)
        viewModel.reviewTabs.observeForever {  }
    }
}