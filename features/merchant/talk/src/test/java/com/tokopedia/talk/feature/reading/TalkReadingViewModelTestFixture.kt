package com.tokopedia.talk.feature.reading

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.talk.feature.reading.domain.usecase.GetDiscussionAggregateUseCase
import com.tokopedia.talk.feature.reading.domain.usecase.GetDiscussionDataUseCase
import com.tokopedia.talk.feature.reading.presentation.viewmodel.TalkReadingViewModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
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
        viewModel = TalkReadingViewModel(getDiscussionAggregateUseCase, getDiscussionDataUseCase, userSession, CoroutineTestDispatchersProvider)
    }
}