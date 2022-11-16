package com.tokopedia.tokochat

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokochat.domain.response.orderprogress.TokoChatOrderProgressResponse
import com.tokopedia.tokochat.domain.response.orderprogress.param.TokoChatOrderProgressParam
import com.tokopedia.tokochat.domain.response.ticker.TokochatRoomTickerResponse
import com.tokopedia.tokochat.utils.JsonResourcesUtil
import com.tokopedia.tokochat.utils.observeAwaitValue
import com.tokopedia.tokochat_common.util.TokoChatUrlUtil
import com.tokopedia.tokochat_common.util.TokoChatValueUtil
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.verify
import junit.framework.TestCase
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class TokoChatViewModelTest : TokoChatViewModelTestFixture() {

    @Test
    fun `when initializeConversationProfileProfile, this method should be called`() {
        // when
        viewModel.initializeProfile()

        // then
        verify {
            profileUseCase.initializeConversationProfile()
        }
    }

    @Test
    fun `when initializeConversationProfile, this method should not be called`() {
        val errorException = Throwable()

        every {
            profileUseCase.initializeConversationProfile()
        } throws errorException

        // when
        viewModel.initializeProfile()

        // then
        verify {
            profileUseCase.initializeConversationProfile()
        }

        val actualResult = viewModel.error.observeAwaitValue()!!

        val expectedResult = errorException::class.java
        assertEquals(expectedResult, actualResult::class.java)
    }

    @Test
    fun `when getProfileUserId, this method should return string value`() {
        val userId = "12345"

        // given
        every {
            profileUseCase.getUserId()
        } returns userId

        // when
        val actualResult = viewModel.getUserId()

        // then
        verify {
            profileUseCase.getUserId()
        }

        assertEquals(userId, actualResult)
    }

    @Test
    fun `when getTokoChatBackground, this method should return livedata success`() {
        runBlocking {
            val expectedImageUrl = TokoChatUrlUtil.IC_TOKOFOOD_SOURCE

            // given
            coEvery {
                getTokoChatBackgroundUseCase(Unit)
            } returns flowOf(expectedImageUrl)

            // when
            viewModel.getTokoChatBackground()

            // then
            coVerify {
                getTokoChatBackgroundUseCase(Unit)
            }

            val actualResult = (viewModel.chatBackground.observeAwaitValue() as Success).data

            assertEquals(expectedImageUrl, actualResult)
        }
    }

    @Test
    fun `when getTokoChatBackground, this method should return livedata fail`() {
        runBlocking {
            val errorException = Throwable()

            // given
            coEvery {
                getTokoChatBackgroundUseCase(Unit)
            } throws errorException

            // when
            viewModel.getTokoChatBackground()

            // then
            coVerify {
                getTokoChatBackgroundUseCase(Unit)
            }

            val actualResult = (viewModel.chatBackground.observeAwaitValue() as Fail).throwable::class.java

            val expectedResult = errorException::class.java
            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when loadChatRoomTicker, this method should return livedata success`() {
        runBlocking {
            val expectedTicker = TokochatRoomTickerResponse(
                tokochatRoomTicker = TokochatRoomTickerResponse.TokochatRoomTicker(
                    enable = true,
                    message = "Hi",
                    tickerType = 1
                )
            )

            // given
            coEvery {
                getTokoChatRoomTickerUseCase(TokoChatValueUtil.TOKOFOOD)
            } returns expectedTicker

            // when
            viewModel.loadChatRoomTicker()

            // then
            coVerify {
                getTokoChatRoomTickerUseCase(TokoChatValueUtil.TOKOFOOD)
            }

            val actualResult = (viewModel.chatRoomTicker.observeAwaitValue() as Success).data

            assertEquals(expectedTicker, actualResult)
        }
    }

    @Test
    fun `when loadChatRoomTicker, this method should return livedata fail`() {
        runBlocking {
            val errorException = Throwable()

            // given
            coEvery {
                getTokoChatRoomTickerUseCase(TokoChatValueUtil.TOKOFOOD)
            } throws errorException

            // when
            viewModel.loadChatRoomTicker()

            // then
            coVerify {
                getTokoChatRoomTickerUseCase(TokoChatValueUtil.TOKOFOOD)
            }

            val actualResult = (viewModel.chatRoomTicker.observeAwaitValue() as Fail).throwable::class.java
            val expectedResult = errorException::class.java

            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when fetchOrderDetail should return set live data success`() {
        runBlocking {
            val tokoChatOrderProgressResponse = JsonResourcesUtil.createSuccessResponse<TokoChatOrderProgressResponse>(
                ORDER_TRACKING_SUCCESS
            )

            coEvery {
                getTokoChatOrderProgressUseCase(TokoChatOrderProgressParam(TKPD_ORDER_ID_DUMMY, GOJEK_ORDER_ID_DUMMY, TokoChatValueUtil.TOKOFOOD))
            } returns tokoChatOrderProgressResponse

            viewModel.loadOrderCompletedStatus(TokoChatOrderProgressParam(TKPD_ORDER_ID_DUMMY, GOJEK_ORDER_ID_DUMMY, TokoChatValueUtil.TOKOFOOD))

            coVerify {
                getTokoChatOrderProgressUseCase(TokoChatOrderProgressParam(TKPD_ORDER_ID_DUMMY, GOJEK_ORDER_ID_DUMMY, TokoChatValueUtil.TOKOFOOD))
            }

            val actualResult = (viewModel.orderTransactionStatus.value as Success).data
            assertEquals(tokoChatOrderProgressResponse, actualResult)
        }
    }

    @Test
    fun `when fetchOrderDetail should set live data error`() {
        runBlocking {
            val errorException = MessageErrorException()

            coEvery {
                getTokoChatOrderProgressUseCase(TokoChatOrderProgressParam(TKPD_ORDER_ID_DUMMY, GOJEK_ORDER_ID_DUMMY, TokoChatValueUtil.TOKOFOOD))
            } throws errorException

            viewModel.loadOrderCompletedStatus(TokoChatOrderProgressParam(TKPD_ORDER_ID_DUMMY, GOJEK_ORDER_ID_DUMMY, TokoChatValueUtil.TOKOFOOD))

            coVerify {
                getTokoChatOrderProgressUseCase(TokoChatOrderProgressParam(TKPD_ORDER_ID_DUMMY, GOJEK_ORDER_ID_DUMMY, TokoChatValueUtil.TOKOFOOD))
            }

            val actualResult = (viewModel.orderTransactionStatus.value as Fail).throwable::class.java
            val expectedResult = errorException::class.java
            TestCase.assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when updateOrderStatusParam is still in progress should return set live data success`() {
        runBlocking {
            val tokochatOrderProgressResponse = JsonResourcesUtil.createSuccessResponse<TokoChatOrderProgressResponse>(
                ORDER_TRACKING_OTW_DESTINATION
            )

            coEvery {
                getTokoChatOrderProgressUseCase(TokoChatOrderProgressParam(TKPD_ORDER_ID_DUMMY, GOJEK_ORDER_ID_DUMMY, TokoChatValueUtil.TOKOFOOD))
            } returns tokochatOrderProgressResponse

            val result = async {
                viewModel.updateOrderTransactionStatus.first()
            }

            viewModel.updateOrderStatusParam(TokoChatOrderProgressParam(TKPD_ORDER_ID_DUMMY, GOJEK_ORDER_ID_DUMMY, TokoChatValueUtil.TOKOFOOD))
            delay(5000L)

            val actualResult = (result.await() as Success).data

            assertEquals(tokochatOrderProgressResponse, actualResult)

            coVerify {
                getTokoChatOrderProgressUseCase(TokoChatOrderProgressParam(TKPD_ORDER_ID_DUMMY, GOJEK_ORDER_ID_DUMMY, TokoChatValueUtil.TOKOFOOD))
            }

            result.cancel()
        }
    }

    @Test
    fun `when updateOrderStatusParam is still in progress should return set live data error`() {
        runBlocking {
            val errorException = MessageErrorException()

            coEvery {
                getTokoChatOrderProgressUseCase(TokoChatOrderProgressParam(TKPD_ORDER_ID_DUMMY, GOJEK_ORDER_ID_DUMMY, TokoChatValueUtil.TOKOFOOD))
            } throws errorException

            val result = async {
                viewModel.updateOrderTransactionStatus.first()
            }

            viewModel.updateOrderStatusParam(TokoChatOrderProgressParam(TKPD_ORDER_ID_DUMMY, GOJEK_ORDER_ID_DUMMY, TokoChatValueUtil.TOKOFOOD))
            delay(5000L)

            val actualResult = result.await() as Fail
            TestCase.assertEquals(errorException::class.java, actualResult.throwable::class.java)

            coVerify {
                getTokoChatOrderProgressUseCase(TokoChatOrderProgressParam(TKPD_ORDER_ID_DUMMY, GOJEK_ORDER_ID_DUMMY, TokoChatValueUtil.TOKOFOOD))
            }

            result.cancel()
        }
    }

    @Test
    fun `given orderId is empty and source is empty when updateOrderStatusParam should return set live data success`() {
        runBlocking {
            val tokochatOrderProgressResponse = JsonResourcesUtil.createSuccessResponse<TokoChatOrderProgressResponse>(
                ORDER_TRACKING_OTW_DESTINATION
            )

            coEvery {
                getTokoChatOrderProgressUseCase(TokoChatOrderProgressParam("", "", ""))
            } returns tokochatOrderProgressResponse

            viewModel.updateOrderStatusParam(TokoChatOrderProgressParam("", "", ""))
            delay(5000L)

            coVerify(exactly = 0) {
                getTokoChatOrderProgressUseCase(TokoChatOrderProgressParam("", "", ""))
            }
        }
    }
}
