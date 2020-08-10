package com.tokopedia.reviewseller.inboxreview

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.LiveData
import com.tokopedia.reviewseller.coroutine.TestCoroutineDispatchers
import com.tokopedia.reviewseller.feature.inboxreview.domain.usecase.GetInboxReviewUseCase
import com.tokopedia.reviewseller.feature.inboxreview.presentation.viewmodel.InboxReviewViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule

abstract class InboxReviewViewModelTestTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var getInboxReviewUseCase: GetInboxReviewUseCase

    protected lateinit var viewModel: InboxReviewViewModel
    protected lateinit var lifecycle: LifecycleRegistry

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = InboxReviewViewModel(TestCoroutineDispatchers,
                getInboxReviewUseCase, userSession)
        lifecycle = LifecycleRegistry(mockk()).apply {
            handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        }
    }

    protected fun LiveData<*>.verifyValueEquals(expected: Any) {
        val actual = value
        TestCase.assertEquals(expected, actual)
    }

    protected fun LiveData<*>.verifyErrorEquals(expected: Fail) {
        val expectedResult = expected.throwable::class.java
        val actualResult = (value as Fail).throwable::class.java
        TestCase.assertEquals(expectedResult, actualResult)
    }
}