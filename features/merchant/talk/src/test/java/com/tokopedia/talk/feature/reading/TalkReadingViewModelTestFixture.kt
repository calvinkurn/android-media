package com.tokopedia.talk.feature.reading

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.tokopedia.talk.coroutines.TestCoroutineDispatchers
import com.tokopedia.talk.feature.reading.domain.usecase.GetDiscussionAggregateUseCase
import com.tokopedia.talk.feature.reading.domain.usecase.GetDiscussionDataUseCase
import com.tokopedia.talk.feature.reading.presentation.viewmodel.TalkReadingViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule

abstract class TalkReadingViewModelTestFixture {

    @RelaxedMockK
    lateinit var getDiscussionAggregateUseCase: GetDiscussionAggregateUseCase

    @RelaxedMockK
    lateinit var getDiscussionDataUseCase: GetDiscussionDataUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: TalkReadingViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TalkReadingViewModel(getDiscussionAggregateUseCase, getDiscussionDataUseCase, userSession, TestCoroutineDispatchers)
    }

    protected fun LiveData<*>.verifyValueEquals(expected: Any) {
        val actual = value
        assertEquals(expected, actual)
    }

    protected fun LiveData<*>.verifySuccessEquals(expected: Success<*>) {
        val expectedResult = expected.data
        val actualResult = (value as Success<*>).data
        assertEquals(expectedResult, actualResult)
    }

    protected fun LiveData<*>.verifyErrorEquals(expected: Fail) {
        val expectedResult = expected.throwable::class.java
        val actualResult = (value as Fail).throwable::class.java
        assertEquals(expectedResult, actualResult)

    }
}