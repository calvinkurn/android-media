package com.tokopedia.tokochat.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tokochat.domain.usecase.TokoChatChannelUseCase
import com.tokopedia.tokochat.view.chatlist.TokoChatListUiMapper
import com.tokopedia.tokochat.view.chatlist.TokoChatListViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class TokoChatListViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    @RelaxedMockK
    protected lateinit var chatChannelUseCase: TokoChatChannelUseCase

    @RelaxedMockK
    protected lateinit var mapper: TokoChatListUiMapper

    protected lateinit var viewModel: TokoChatListViewModel
    protected val throwableDummy = Throwable("Oops!")

    @Before
    open fun setup() {
        MockKAnnotations.init(this)
        viewModel = spyk(
            TokoChatListViewModel(
                chatChannelUseCase,
                mapper,
                CoroutineTestDispatchersProvider
            )
        )
    }

    @After
    fun finish() {
        unmockkAll()
    }
}
