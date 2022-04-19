package com.tokopedia.talk.feature.inbox

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.talk.feature.inbox.analytics.TalkInboxTracking
import com.tokopedia.talk.feature.inbox.domain.usecase.TalkInboxListUseCase
import com.tokopedia.talk.feature.inbox.presentation.viewmodel.TalkInboxViewModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class TalkInboxViewModelTestFixture {

    @RelaxedMockK
    lateinit var talkInboxListUseCase: TalkInboxListUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var talkInboxTracking: TalkInboxTracking

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: TalkInboxViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TalkInboxViewModel(CoroutineTestDispatchersProvider, talkInboxListUseCase, userSession, talkInboxTracking)
        viewModel.inboxList.observeForever {  }
    }
}