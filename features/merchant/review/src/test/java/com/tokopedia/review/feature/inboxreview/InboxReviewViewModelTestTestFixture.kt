package com.tokopedia.review.feature.inboxreview

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.review.feature.inboxreview.domain.usecase.GetInboxReviewUseCase
import com.tokopedia.review.feature.inboxreview.domain.usecase.GetInboxReviewCounterUseCase
import com.tokopedia.review.feature.inboxreview.presentation.viewmodel.InboxReviewViewModel
import com.tokopedia.review.feature.reviewreminder.domain.ProductrevGetReminderCounterUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule

abstract class InboxReviewViewModelTestTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var getInboxReviewUseCase: GetInboxReviewUseCase

    @RelaxedMockK
    lateinit var getInboxReviewCounterUseCase: GetInboxReviewCounterUseCase

    @RelaxedMockK
    lateinit var productrevGetReminderCounterUseCase: ProductrevGetReminderCounterUseCase

    protected lateinit var viewModel: InboxReviewViewModel
    protected lateinit var lifecycle: LifecycleRegistry

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = InboxReviewViewModel(CoroutineTestDispatchersProvider,
                getInboxReviewUseCase, getInboxReviewCounterUseCase, productrevGetReminderCounterUseCase, userSession)
        lifecycle = LifecycleRegistry(mockk()).apply {
            handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        }
    }
}