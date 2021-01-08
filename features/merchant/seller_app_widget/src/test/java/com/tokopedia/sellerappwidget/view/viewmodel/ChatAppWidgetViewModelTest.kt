package com.tokopedia.sellerappwidget.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.sellerappwidget.domain.usecase.GetChatUseCase
import com.tokopedia.sellerappwidget.view.model.ChatUiModel
import com.tokopedia.sellerappwidget.view.viewmodel.view.AppWidgetView
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By @ilhamsuaib on 21/12/20
 */

@ExperimentalCoroutinesApi
class ChatAppWidgetViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    lateinit var getChatUseCase: GetChatUseCase

    @RelaxedMockK
    lateinit var mView: AppWidgetView<ChatUiModel>

    private lateinit var mViewModel: ChatAppWidgetViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mViewModel = ChatAppWidgetViewModel(getChatUseCase, coroutineTestRule.dispatchers)
        mViewModel.bindView(mView)
    }

    @Test
    fun `returns success result and notify the UI when get chat list`() = coroutineTestRule.runBlockingTest {
        getChatUseCase.params = GetChatUseCase.creteParams()

        val chatModel = ChatUiModel()

        coEvery {
            getChatUseCase.executeOnBackground()
        } returns chatModel

        mViewModel.getChatList()

        coVerify {
            getChatUseCase.executeOnBackground()
        }

        coVerify {
            mView.onSuccess(chatModel)
        }

        val actualResult = getChatUseCase.executeOnBackground()

        assertEquals(chatModel, actualResult)
    }

    @Test
    fun `throw Exception and notify the UI when failed to get chat list`() = coroutineTestRule.runBlockingTest {
        val throwable = RuntimeException("")
        getChatUseCase.params = GetChatUseCase.creteParams()

        coEvery {
            getChatUseCase.executeOnBackground()
        } throws throwable

        mViewModel.getChatList()

        coVerify {
            getChatUseCase.executeOnBackground()
        }

        coVerify {
            mView.onError(any())
        }
    }
}