package com.tokopedia.review.feature.inbox.buyerreview.view.viewmodel.inboxdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.tokopedia.review.feature.reviewreply.insert.domain.usecase.ReviewReplyInsertUseCase
import com.tokopedia.review.feature.reviewreply.update.domain.usecase.ReviewReplyUpdateUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule

abstract class InboxReputationDetailViewModelTestFixture {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var reviewReplyInsertUseCase: ReviewReplyInsertUseCase

    @RelaxedMockK
    lateinit var reviewReplyUpdateUseCase: ReviewReplyUpdateUseCase

    protected lateinit var viewModel: InboxReputationDetailViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = InboxReputationDetailViewModel(
            CoroutineTestDispatchersProvider,
            reviewReplyInsertUseCase,
            reviewReplyUpdateUseCase
        )
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