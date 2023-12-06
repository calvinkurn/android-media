package com.tokopedia.tokochat.test.chatroom

import app.cash.turbine.test
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokochat.base.TokoChatViewModelTestFixture
import com.tokopedia.tokochat.common.util.TokoChatCommonValueUtil
import com.tokopedia.tokochat.domain.response.orderprogress.TokoChatOrderProgressResponse
import com.tokopedia.tokochat.domain.response.orderprogress.param.TokoChatOrderProgressParam
import com.tokopedia.tokochat.utils.JsonResourcesUtil
import com.tokopedia.tokochat.view.chatroom.TokoChatViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.TestCase
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class TokoChatOrderProgressViewModelTest : TokoChatViewModelTestFixture() {

    @Test
    fun `when fetchOrderDetail should return set live data success`() {
        runTest {
            val tokoChatOrderProgressResponse =
                JsonResourcesUtil.createSuccessResponse<TokoChatOrderProgressResponse>(
                    ORDER_TRACKING_SUCCESS
                )

            coEvery {
                getTokoChatOrderProgressUseCase(
                    TokoChatOrderProgressParam(
                        TKPD_ORDER_ID_DUMMY,
                        TokoChatCommonValueUtil.SOURCE_TOKOFOOD
                    )
                )
            } returns tokoChatOrderProgressResponse

            viewModel.loadOrderCompletedStatus(
                TKPD_ORDER_ID_DUMMY,
                TokoChatCommonValueUtil.SOURCE_TOKOFOOD
            )

            coVerify {
                getTokoChatOrderProgressUseCase(
                    TokoChatOrderProgressParam(
                        TKPD_ORDER_ID_DUMMY,
                        TokoChatCommonValueUtil.SOURCE_TOKOFOOD
                    )
                )
            }

            val actualResult = (viewModel.orderTransactionStatus.value as Success).data
            Assert.assertEquals(tokoChatOrderProgressResponse, actualResult)
        }
    }

    @Test
    fun `when fetchOrderDetail should set live data error`() {
        runTest {
            val errorException = MessageErrorException()

            coEvery {
                getTokoChatOrderProgressUseCase(
                    TokoChatOrderProgressParam(
                        TKPD_ORDER_ID_DUMMY,
                        TokoChatCommonValueUtil.SOURCE_TOKOFOOD
                    )
                )
            } throws errorException

            viewModel.loadOrderCompletedStatus(
                TKPD_ORDER_ID_DUMMY,
                TokoChatCommonValueUtil.SOURCE_TOKOFOOD
            )

            coVerify {
                getTokoChatOrderProgressUseCase(
                    TokoChatOrderProgressParam(
                        TKPD_ORDER_ID_DUMMY,
                        TokoChatCommonValueUtil.SOURCE_TOKOFOOD
                    )
                )
            }

            val actualResult =
                (viewModel.orderTransactionStatus.value as Fail).throwable::class.java
            val expectedResult = errorException::class.java
            TestCase.assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when updateOrderStatusParam is still in progress should return set live data success`() {
        runTest {
            val tokochatOrderProgressResponse =
                JsonResourcesUtil.createSuccessResponse<TokoChatOrderProgressResponse>(
                    ORDER_TRACKING_OTW_DESTINATION
                )

            coEvery {
                getTokoChatOrderProgressUseCase(
                    TokoChatOrderProgressParam(
                        TKPD_ORDER_ID_DUMMY,
                        TokoChatCommonValueUtil.SOURCE_TOKOFOOD
                    )
                )
            } returns tokochatOrderProgressResponse

            val result = async {
                viewModel.updateOrderTransactionStatus.first()
            }

            viewModel.updateOrderStatusParam(TKPD_ORDER_ID_DUMMY to TokoChatCommonValueUtil.SOURCE_TOKOFOOD)
            advanceTimeBy(TokoChatViewModel.DELAY_UPDATE_ORDER_STATE + 1000)

            val actualResult = (result.await() as Success).data

            Assert.assertEquals(tokochatOrderProgressResponse, actualResult)

            coVerify {
                getTokoChatOrderProgressUseCase(
                    TokoChatOrderProgressParam(
                        TKPD_ORDER_ID_DUMMY,
                        TokoChatCommonValueUtil.SOURCE_TOKOFOOD
                    )
                )
            }

            result.cancel()
        }
    }

    @Test
    fun `when updateOrderStatusParam is still in progress should return set live data error`() {
        runTest {
            val errorException = MessageErrorException()

            coEvery {
                getTokoChatOrderProgressUseCase(
                    TokoChatOrderProgressParam(
                        TKPD_ORDER_ID_DUMMY,
                        TokoChatCommonValueUtil.SOURCE_TOKOFOOD
                    )
                )
            } throws errorException

            val result = async {
                viewModel.updateOrderTransactionStatus.first()
            }

            viewModel.updateOrderStatusParam(TKPD_ORDER_ID_DUMMY to TokoChatCommonValueUtil.SOURCE_TOKOFOOD)
            advanceTimeBy(TokoChatViewModel.DELAY_UPDATE_ORDER_STATE + 1000)

            val actualResult = result.await() as Fail
            TestCase.assertEquals(errorException::class.java, actualResult.throwable::class.java)

            coVerify {
                getTokoChatOrderProgressUseCase(
                    TokoChatOrderProgressParam(
                        TKPD_ORDER_ID_DUMMY,
                        TokoChatCommonValueUtil.SOURCE_TOKOFOOD
                    )
                )
            }

            result.cancel()
        }
    }

    @Test
    fun `given orderId is empty and source is empty when updateOrderStatusParam should return set live data success`() {
        runTest {
            val tokochatOrderProgressResponse =
                JsonResourcesUtil.createSuccessResponse<TokoChatOrderProgressResponse>(
                    ORDER_TRACKING_OTW_DESTINATION
                )

            coEvery {
                getTokoChatOrderProgressUseCase(TokoChatOrderProgressParam("", ""))
            } returns tokochatOrderProgressResponse

            viewModel.updateOrderStatusParam("" to "")
            advanceTimeBy(TokoChatViewModel.DELAY_UPDATE_ORDER_STATE + 1000)

            coVerify(exactly = 0) {
                getTokoChatOrderProgressUseCase(TokoChatOrderProgressParam("", ""))
            }
        }
    }

    @Test
    fun `when translate order id gojek, should give tokopedia order id`() {
        runTest {
            // Given
            coEvery {
                getTokopediaOrderIdUseCase(any())
            } returns flowOf(TKPD_ORDER_ID_DUMMY)

            viewModel.isTkpdOrderStatus.test {
                // When
                viewModel.translateGojekOrderId(GOJEK_ORDER_ID_DUMMY)

                // Then
                Assert.assertEquals(
                    true,
                    awaitItem()
                )
                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `when fail to translate order id gojek, should give error`() {
        runTest {
            // Given
            coEvery {
                getTokopediaOrderIdUseCase(any())
            } throws throwableDummy

            viewModel.isTkpdOrderStatus.test {
                // When
                viewModel.translateGojekOrderId(GOJEK_ORDER_ID_DUMMY)

                // Then
                Assert.assertEquals(
                    false,
                    awaitItem()
                )
                cancelAndConsumeRemainingEvents()
            }
        }
    }
}
