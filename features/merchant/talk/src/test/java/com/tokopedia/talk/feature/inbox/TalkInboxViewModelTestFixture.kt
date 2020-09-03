package com.tokopedia.talk.feature.inbox

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.talk.coroutines.TestCoroutineDispatchers
import com.tokopedia.talk.feature.inbox.domain.usecase.TalkInboxListUseCase
import com.tokopedia.talk.feature.inbox.presentation.viewmodel.TalkInboxViewModel
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class TalkInboxViewModelTestFixture {

    @RelaxedMockK
    lateinit var talkInboxListUseCase: TalkInboxListUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: TalkInboxViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TalkInboxViewModel(TestCoroutineDispatchers, talkInboxListUseCase)
        viewModel.inboxList.observeForever {  }
    }
}