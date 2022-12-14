package com.tokopedia.tokochat.test

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokochat.base.TokoChatViewModelTestFixture
import com.tokopedia.tokochat.domain.response.orderprogress.TokoChatOrderProgressResponse
import com.tokopedia.tokochat.domain.response.orderprogress.param.TokoChatOrderProgressParam
import com.tokopedia.tokochat.utils.JsonResourcesUtil
import com.tokopedia.tokochat_common.util.TokoChatValueUtil
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.TestCase
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class TokoChatOrderProgressViewModelTest : TokoChatViewModelTestFixture() {

    @Test
    fun `when fetchOrderDetail should return set live data success`() {
        runBlocking {
            val tokoChatOrderProgressResponse =
                JsonResourcesUtil.createSuccessResponse<TokoChatOrderProgressResponse>(
                    ORDER_TRACKING_SUCCESS
                )

            coEvery {
                getTokoChatOrderProgressUseCase(
                    TokoChatOrderProgressParam(
                        TKPD_ORDER_ID_DUMMY,
                        TokoChatValueUtil.TOKOFOOD
                    )
                )
            } returns tokoChatOrderProgressResponse

            viewModel.loadOrderCompletedStatus(
                TKPD_ORDER_ID_DUMMY,
                TokoChatValueUtil.TOKOFOOD
            )

            coVerify {
                getTokoChatOrderProgressUseCase(
                    TokoChatOrderProgressParam(
                        TKPD_ORDER_ID_DUMMY,
                        TokoChatValueUtil.TOKOFOOD
                    )
                )
            }

            val actualResult = (viewModel.orderTransactionStatus.value as Success).data
            Assert.assertEquals(tokoChatOrderProgressResponse, actualResult)
        }
    }

    @Test
    fun `when fetchOrderDetail should set live data error`() {
        runBlocking {
            val errorException = MessageErrorException()

            coEvery {
                getTokoChatOrderProgressUseCase(
                    TokoChatOrderProgressParam(
                        TKPD_ORDER_ID_DUMMY,
                        TokoChatValueUtil.TOKOFOOD
                    )
                )
            } throws errorException

            viewModel.loadOrderCompletedStatus(
                TKPD_ORDER_ID_DUMMY,
                TokoChatValueUtil.TOKOFOOD
            )

            coVerify {
                getTokoChatOrderProgressUseCase(
                    TokoChatOrderProgressParam(
                        TKPD_ORDER_ID_DUMMY,
                        TokoChatValueUtil.TOKOFOOD
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
        runBlocking {
            val tokochatOrderProgressResponse =
                JsonResourcesUtil.createSuccessResponse<TokoChatOrderProgressResponse>(
                    ORDER_TRACKING_OTW_DESTINATION
                )

            coEvery {
                getTokoChatOrderProgressUseCase(
                    TokoChatOrderProgressParam(
                        TKPD_ORDER_ID_DUMMY,
                        TokoChatValueUtil.TOKOFOOD
                    )
                )
            } returns tokochatOrderProgressResponse

            val result = async {
                viewModel.updateOrderTransactionStatus.first()
            }

            viewModel.updateOrderStatusParam(TKPD_ORDER_ID_DUMMY to TokoChatValueUtil.TOKOFOOD)
            delay(5000L)

            val actualResult = (result.await() as Success).data

            Assert.assertEquals(tokochatOrderProgressResponse, actualResult)

            coVerify {
                getTokoChatOrderProgressUseCase(
                    TokoChatOrderProgressParam(
                        TKPD_ORDER_ID_DUMMY,
                        TokoChatValueUtil.TOKOFOOD
                    )
                )
            }

            result.cancel()
        }
    }

    @Test
    fun `when updateOrderStatusParam is still in progress should return set live data error`() {
        runBlocking {
            val errorException = MessageErrorException()

            coEvery {
                getTokoChatOrderProgressUseCase(
                    TokoChatOrderProgressParam(
                        TKPD_ORDER_ID_DUMMY,
                        TokoChatValueUtil.TOKOFOOD
                    )
                )
            } throws errorException

            val result = async {
                viewModel.updateOrderTransactionStatus.first()
            }

            viewModel.updateOrderStatusParam(TKPD_ORDER_ID_DUMMY to TokoChatValueUtil.TOKOFOOD)
            delay(5000L)

            val actualResult = result.await() as Fail
            TestCase.assertEquals(errorException::class.java, actualResult.throwable::class.java)

            coVerify {
                getTokoChatOrderProgressUseCase(
                    TokoChatOrderProgressParam(
                        TKPD_ORDER_ID_DUMMY,
                        TokoChatValueUtil.TOKOFOOD
                    )
                )
            }

            result.cancel()
        }
    }

    @Test
    fun `given orderId is empty and source is empty when updateOrderStatusParam should return set live data success`() {
        runBlocking {
            val tokochatOrderProgressResponse =
                JsonResourcesUtil.createSuccessResponse<TokoChatOrderProgressResponse>(
                    ORDER_TRACKING_OTW_DESTINATION
                )

            coEvery {
                getTokoChatOrderProgressUseCase(TokoChatOrderProgressParam("", ""))
            } returns tokochatOrderProgressResponse

            viewModel.updateOrderStatusParam("" to "")
            delay(5000L)

            coVerify(exactly = 0) {
                getTokoChatOrderProgressUseCase(TokoChatOrderProgressParam("", ""))
            }
        }
    }
}
