package com.tokopedia.review.common.reviewreplyinsert.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.tokopedia.review.common.reviewreplyinsert.domain.usecase.ReviewReplyInsertUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule

abstract class ReviewReplyInsertViewModelTestFixture {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var reviewReplyInsertUseCase: ReviewReplyInsertUseCase

    protected lateinit var viewModel: ReviewReplyInsertViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ReviewReplyInsertViewModel(CoroutineTestDispatchersProvider, reviewReplyInsertUseCase)
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