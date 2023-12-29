package com.tokopedia.tokochat.test.chatroom

import com.tokopedia.tokochat.base.TokoChatViewModelTestFixture
import com.tokopedia.tokochat.common.util.TokoChatCommonValueUtil.SOURCE_TOKOFOOD
import com.tokopedia.tokochat.domain.response.ticker.TokochatRoomTickerResponse
import com.tokopedia.tokochat.utils.observeAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class TokoChatTickerViewModelTest : TokoChatViewModelTestFixture() {

    override fun setup() {
        super.setup()
        viewModel.source = SOURCE_TOKOFOOD
    }

    @Test
    fun `when loadChatRoomTicker, this method should return livedata success`() {
        runTest {
            val expectedTicker = TokochatRoomTickerResponse(
                tokochatRoomTicker = TokochatRoomTickerResponse.TokochatRoomTicker(
                    enable = true,
                    message = "Hi",
                    tickerType = 1
                )
            )

            // given
            coEvery {
                getTokoChatRoomTickerUseCase(SOURCE_TOKOFOOD)
            } returns expectedTicker

            // when
            viewModel.loadChatRoomTicker()

            // then
            coVerify {
                getTokoChatRoomTickerUseCase(SOURCE_TOKOFOOD)
            }

            val actualResult = (viewModel.chatRoomTicker.observeAwaitValue() as Success).data

            Assert.assertEquals(expectedTicker, actualResult)
        }
    }

    @Test
    fun `when loadChatRoomTicker, this method should return livedata fail`() {
        runTest {
            val errorException = Throwable()

            // given
            coEvery {
                getTokoChatRoomTickerUseCase(SOURCE_TOKOFOOD)
            } throws errorException

            // when
            viewModel.loadChatRoomTicker()

            // then
            coVerify {
                getTokoChatRoomTickerUseCase(SOURCE_TOKOFOOD)
            }

            val actualResult = (viewModel.chatRoomTicker.observeAwaitValue() as Fail).throwable::class.java
            val expectedResult = errorException::class.java

            Assert.assertEquals(expectedResult, actualResult)
        }
    }
}
