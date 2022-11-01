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
import io.mockk.verify
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

    private val testShopId = "123"

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mViewModel = ChatAppWidgetViewModel(getChatUseCase, coroutineTestRule.dispatchers)
        mViewModel.bindView(mView)
    }

    @Test
    fun `returns success result and notify the UI when get chat list`() {
        coroutineTestRule.runBlockingTest {
            getChatUseCase.params = GetChatUseCase.creteParams(testShopId)

            val chatModel = ChatUiModel()

            coEvery {
                getChatUseCase.executeOnBackground()
            } returns chatModel

            mViewModel.getChatList(testShopId)

            coVerify {
                getChatUseCase.executeOnBackground()
            }

            verify {
                mView.onSuccess(chatModel)
            }

            val actualResult = getChatUseCase.executeOnBackground()

            assertEquals(chatModel, actualResult)
        }
    }

    @Test
    fun `given view null value when get chat data then return success result but will not notify the UI`() {
        coroutineTestRule.runBlockingTest {
            getChatUseCase.params = GetChatUseCase.creteParams(testShopId)

            val chatModel = ChatUiModel()

            coEvery {
                getChatUseCase.executeOnBackground()
            } returns chatModel

            mViewModel.unbind()
            mViewModel.getChatList(testShopId)

            coVerify {
                getChatUseCase.executeOnBackground()
            }

            verify(inverse = true) {
                mView.onSuccess(chatModel)
            }
        }
    }

    @Test
    fun `throw Exception and notify the UI when failed to get chat list`() {
        coroutineTestRule.runBlockingTest {
            val throwable = RuntimeException("")
            getChatUseCase.params = GetChatUseCase.creteParams(testShopId)

            coEvery {
                getChatUseCase.executeOnBackground()
            } throws throwable

            mViewModel.getChatList(testShopId)

            coVerify {
                getChatUseCase.executeOnBackground()
            }

            verify {
                mView.onError(any())
            }
        }
    }

    @Test
    fun `given view null value when get chat list then throw Exception will not notify the UI`() {
        coroutineTestRule.runBlockingTest {
            val exception = RuntimeException("")
            getChatUseCase.params = GetChatUseCase.creteParams(testShopId)

            coEvery {
                getChatUseCase.executeOnBackground()
            } throws exception

            mViewModel.unbind()
            mViewModel.getChatList(testShopId)

            coVerify {
                getChatUseCase.executeOnBackground()
            }

            verify(inverse = true) {
                mView.onError(any())
            }
        }
    }
}
