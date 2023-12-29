package com.tokopedia.tokofood.postpurchase

import app.cash.turbine.test
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokochat.config.util.TokoChatResult
import com.tokopedia.tokochat.config.util.TokoChatServiceType
import com.tokopedia.tokofood.feature.ordertracking.domain.model.TokoFoodOrderDetailResponse
import com.tokopedia.tokofood.feature.ordertracking.domain.model.TokoFoodOrderStatusResponse
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.DriverPhoneNumberUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewmodel.TokoFoodOrderTrackingViewModel
import com.tokopedia.tokofood.utils.JsonResourcesUtil
import com.tokopedia.tokofood.utils.observeAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class TokoFoodOrderTrackingViewModelTest : TokoFoodOrderTrackingViewModelTestFixture() {

    @Test
    fun `when fetchOrderDetail should return set live data success`() {
        runBlocking {
            val jsonResponse = JsonResourcesUtil.createSuccessResponse<TokoFoodOrderDetailResponse>(
                ORDER_TRACKING_SUCCESS
            ).tokofoodOrderDetail
            val orderDetailResultUiModel =
                tokoFoodOrderDetailMapper.mapToOrderDetailResultUiModel(jsonResponse)
            coEvery {
                getTokoFoodOrderDetailUseCase.get().execute(anyString())
            } returns orderDetailResultUiModel

            viewModel.fetchOrderDetail(anyString())

            coVerify {
                getTokoFoodOrderDetailUseCase.get().execute(anyString())
            }

            val actualResult = (viewModel.orderDetailResult.observeAwaitValue() as Success).data
            assertEquals(orderDetailResultUiModel.orderStatusKey, actualResult.orderStatusKey)
            assertEquals(orderDetailResultUiModel.orderDetailList, actualResult.orderDetailList)
            assertEquals(orderDetailResultUiModel.foodItemList, actualResult.foodItemList)
            assertEquals(viewModel.getMerchantData()?.merchantId, orderDetailResultUiModel.merchantData.merchantId)
            assertEquals(viewModel.getMerchantData()?.merchantName, orderDetailResultUiModel.merchantData.merchantName)
            assertTrue(viewModel.getFoodItems().isNotEmpty())
            assertTrue(viewModel.userSession.userId.isNullOrBlank())
            assertTrue(viewModel.getMerchantData() != null)
            assertTrue(viewModel.getOrderStatus().isNotBlank())
        }
    }

    @Test
    fun `when fetchOrderDetail should set live data error`() {
        runBlocking {
            val errorException = MessageErrorException()

            coEvery {
                getTokoFoodOrderDetailUseCase.get().execute(anyString())
            } throws errorException

            viewModel.fetchOrderDetail(anyString())

            coVerify {
                getTokoFoodOrderDetailUseCase.get().execute(anyString())
            }

            val actualResult = (viewModel.orderDetailResult.value as Fail).throwable::class.java
            val expectedResult = errorException::class.java
            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when set savedStateHandle should return the orderId has value`() {
        runBlocking {
            every {
                savedStateHandle.get<String>(TokoFoodOrderTrackingViewModel.ORDER_ID)
            } returns ORDER_ID_DUMMY

            every {
                savedStateHandle.get<String>(TokoFoodOrderTrackingViewModel.GOFOOD_ORDER_NUMBER)
            } returns GOFOOD_ORDER_NUMBER

            every {
                savedStateHandle.get<String>(TokoFoodOrderTrackingViewModel.CHANNEL_ID)
            } returns CHANNEL_ID

            every {
                savedStateHandle.get<Boolean>(IS_FROM_BUBBLE_KEY)
            } returns IS_FROM_BUBBLE

            viewModel.goFoodOrderNumber = GOFOOD_ORDER_NUMBER
            viewModel.channelId = CHANNEL_ID
            viewModel.isFromBubble = IS_FROM_BUBBLE
            viewModel.updateOrderId(ORDER_ID_DUMMY)
            viewModel.onSavedInstanceState()
            viewModel.onRestoreSavedInstanceState()

            assertEquals(ORDER_ID_DUMMY, viewModel.getOrderId())
            assertEquals(CHANNEL_ID, viewModel.channelId)
            assertEquals(GOFOOD_ORDER_NUMBER, viewModel.goFoodOrderNumber)
            assertEquals(IS_FROM_BUBBLE, viewModel.isFromBubble)
        }
    }

    @Test
    fun `when set savedStateHandle should return the orderId has subscription value`() {
        runBlocking {
            every<String?> {
                savedStateHandle.get(any())
            } returns null
            viewModel.onRestoreSavedInstanceState()
            assertEquals(Int.ONE, viewModel.orderIdFlow.subscriptionCount.value)
        }
    }

    @Test
    fun `when fetchDriverPhoneNumber should return set live data success`() {
        runBlocking {
            val driverPhoneNumberUiModel =
                DriverPhoneNumberUiModel(isCallable = true, phoneNumber = "0812345646")

            coEvery {
                getDriverPhoneNumberUseCase.get().execute(anyString())
            } returns driverPhoneNumberUiModel

            viewModel.fetchDriverPhoneNumber(anyString())

            coVerify {
                getDriverPhoneNumberUseCase.get().execute(anyString())
            }

            val expectedResult = (viewModel.driverPhoneNumber.observeAwaitValue() as Success).data
            assertEquals(expectedResult.phoneNumber, driverPhoneNumberUiModel.phoneNumber)
            assertEquals(expectedResult.isCallable, driverPhoneNumberUiModel.isCallable)
        }
    }

    @Test
    fun `when fetchDriverPhoneNumber should set live data error`() {
        runBlocking {
            val errorException = MessageErrorException()

            coEvery {
                getDriverPhoneNumberUseCase.get().execute(anyString())
            } throws errorException

            viewModel.fetchDriverPhoneNumber(anyString())

            coVerify {
                getDriverPhoneNumberUseCase.get().execute(anyString())
            }

            val actualResult = (viewModel.driverPhoneNumber.value as Fail).throwable::class.java
            val expectedResult = errorException::class.java
            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when fetchOrderLiveTracking is still in progress should return set live data success`() {
        runBlocking {
            val jsonResponse = JsonResourcesUtil.createSuccessResponse<TokoFoodOrderStatusResponse>(
                ORDER_TRACKING_OTW_DESTINATION
            ).tokofoodOrderDetail

            val orderDetailResultUiModel =
                tokoFoodOrderStatusMapper.mapToOrderStatusLiveTrackingUiModel(jsonResponse)

            coEvery {
                getTokoFoodOrderStatusUseCase.get().execute(ORDER_ID_DUMMY)
            } returns orderDetailResultUiModel

            val result = async {
                viewModel.orderLiveTrackingStatus.first()
            }

            viewModel.updateOrderId(ORDER_ID_DUMMY)
            delay(6000L)

            val actualResult = result.await() as Success
            assertEquals(orderDetailResultUiModel.orderStatusKey, actualResult.data.orderStatusKey)
            assertEquals(
                orderDetailResultUiModel.orderTrackingStatusInfoUiModel,
                actualResult.data.orderTrackingStatusInfoUiModel
            )
            assertEquals(
                orderDetailResultUiModel.toolbarLiveTrackingUiModel,
                actualResult.data.toolbarLiveTrackingUiModel
            )
            assertEquals(
                orderDetailResultUiModel.invoiceOrderNumberUiModel,
                actualResult.data.invoiceOrderNumberUiModel
            )
            assertEquals(ORDER_ID_DUMMY, viewModel.getOrderId())

            coVerify {
                getTokoFoodOrderStatusUseCase.get().execute(ORDER_ID_DUMMY)
            }

            result.cancel()
        }
    }

    @Test
    fun `when the orderId is empty then there is no action`() {
        runBlocking {
            val orderIdEmpty = ""
            viewModel.updateOrderId(orderIdEmpty)
            assertEquals(orderIdEmpty, viewModel.getOrderId())
        }
    }

    @Test
    fun `when fetchOrderLiveTracking is still in progress should return set live data error`() {
        runBlocking {
            val errorException = MessageErrorException()

            coEvery {
                getTokoFoodOrderStatusUseCase.get().execute(ORDER_ID_DUMMY)
            } throws errorException

            val result = async {
                viewModel.orderLiveTrackingStatus.first()
            }

            viewModel.updateOrderId(ORDER_ID_DUMMY)
            delay(6000L)

            val actualResult = result.await() as Fail
            assertEquals(errorException::class.java, actualResult.throwable::class.java)

            coVerify {
                getTokoFoodOrderStatusUseCase.get().execute(ORDER_ID_DUMMY)
            }

            result.cancel()
        }
    }

    @Test
    fun `when fetchOrderLiveTracking then cancelled order should return set live data success`() {
        runBlocking {
            val jsonOrderStatusResponse =
                JsonResourcesUtil.createSuccessResponse<TokoFoodOrderStatusResponse>(
                    ORDER_TRACKING_CANCELLED
                ).tokofoodOrderDetail

            val orderStatusResultUiModel =
                tokoFoodOrderStatusMapper.mapToOrderStatusLiveTrackingUiModel(
                    jsonOrderStatusResponse
                )

            val jsonResponse = JsonResourcesUtil.createSuccessResponse<TokoFoodOrderDetailResponse>(
                ORDER_TRACKING_SUCCESS
            ).tokofoodOrderDetail

            val orderDetailResultUiModel =
                tokoFoodOrderDetailMapper.mapToOrderDetailResultUiModel(jsonResponse)

            coEvery {
                getTokoFoodOrderStatusUseCase.get().execute(ORDER_ID_DUMMY)
            } returns orderStatusResultUiModel

            coEvery {
                getTokoFoodOrderDetailUseCase.get().execute(ORDER_ID_DUMMY)
            } returns orderDetailResultUiModel

            viewModel.updateOrderId(ORDER_ID_DUMMY)
            delay(6000L)

            coVerify {
                getTokoFoodOrderStatusUseCase.get().execute(ORDER_ID_DUMMY)
            }

            assertEquals(ORDER_ID_DUMMY, viewModel.getOrderId())

            coVerify {
                getTokoFoodOrderStatusUseCase.get().execute(ORDER_ID_DUMMY)
            }

            val actualResult =
                (viewModel.orderCompletedLiveTracking.observeAwaitValue() as Success).data
            assertEquals(orderDetailResultUiModel.orderStatusKey, actualResult.orderStatusKey)
            assertEquals(orderDetailResultUiModel.orderDetailList, actualResult.orderDetailList)
            assertEquals(orderDetailResultUiModel.foodItemList, actualResult.foodItemList)

            coVerify {
                getTokoFoodOrderDetailUseCase.get().execute(ORDER_ID_DUMMY)
            }
        }
    }

    @Test
    fun `when fetchOrderLiveTracking then cancelled order should return set live data error`() {
        runBlocking {
            val jsonOrderStatusResponse =
                JsonResourcesUtil.createSuccessResponse<TokoFoodOrderStatusResponse>(
                    ORDER_TRACKING_CANCELLED
                ).tokofoodOrderDetail

            val orderStatusResultUiModel =
                tokoFoodOrderStatusMapper.mapToOrderStatusLiveTrackingUiModel(
                    jsonOrderStatusResponse
                )

            coEvery {
                getTokoFoodOrderStatusUseCase.get().execute(ORDER_ID_DUMMY)
            } returns orderStatusResultUiModel

            val errorException = MessageErrorException()

            coEvery {
                getTokoFoodOrderDetailUseCase.get().execute(ORDER_ID_DUMMY)
            } throws errorException

            viewModel.updateOrderId(ORDER_ID_DUMMY)
            delay(6000L)

            val actualResult = (viewModel.orderCompletedLiveTracking.observeAwaitValue() as Fail)
            assertEquals(errorException::class.java, actualResult.throwable::class.java)

            coVerify {
                getTokoFoodOrderStatusUseCase.get().execute(ORDER_ID_DUMMY)
            }
        }
    }

    @Test
    fun `when fetchOrderLiveTracking then success order should return set live data success`() {
        runBlocking {
            val jsonOrderStatusResponse =
                JsonResourcesUtil.createSuccessResponse<TokoFoodOrderStatusResponse>(
                    ORDER_TRACKING_SUCCESS
                ).tokofoodOrderDetail

            val orderStatusResultUiModel =
                tokoFoodOrderStatusMapper.mapToOrderStatusLiveTrackingUiModel(
                    jsonOrderStatusResponse
                )

            val jsonResponse = JsonResourcesUtil.createSuccessResponse<TokoFoodOrderDetailResponse>(
                ORDER_TRACKING_SUCCESS
            ).tokofoodOrderDetail

            val orderDetailResultUiModel =
                tokoFoodOrderDetailMapper.mapToOrderDetailResultUiModel(jsonResponse)

            coEvery {
                getTokoFoodOrderStatusUseCase.get().execute(ORDER_ID_DUMMY)
            } returns orderStatusResultUiModel

            coEvery {
                getTokoFoodOrderDetailUseCase.get().execute(ORDER_ID_DUMMY)
            } returns orderDetailResultUiModel

            viewModel.updateOrderId(ORDER_ID_DUMMY)
            delay(6000L)

            assertEquals(ORDER_ID_DUMMY, viewModel.getOrderId())

            val actualResult =
                (viewModel.orderCompletedLiveTracking.observeAwaitValue() as Success).data
            assertEquals(orderDetailResultUiModel.orderStatusKey, actualResult.orderStatusKey)
            assertEquals(orderDetailResultUiModel.orderDetailList, actualResult.orderDetailList)
            assertEquals(orderDetailResultUiModel.foodItemList, actualResult.foodItemList)
            assertEquals(viewModel.getMerchantData()?.merchantId, orderDetailResultUiModel.merchantData.merchantId)
            assertEquals(viewModel.getMerchantData()?.merchantName, orderDetailResultUiModel.merchantData.merchantName)

            coVerify {
                getTokoFoodOrderStatusUseCase.get().execute(ORDER_ID_DUMMY)
            }

            coVerify {
                getTokoFoodOrderDetailUseCase.get().execute(ORDER_ID_DUMMY)
            }
        }
    }

    @Test
    fun `when fetchOrderLiveTracking then success order should return set live data error`() {
        runBlocking {
            val jsonOrderStatusResponse =
                JsonResourcesUtil.createSuccessResponse<TokoFoodOrderStatusResponse>(
                    ORDER_TRACKING_SUCCESS
                ).tokofoodOrderDetail

            val orderStatusResultUiModel =
                tokoFoodOrderStatusMapper.mapToOrderStatusLiveTrackingUiModel(
                    jsonOrderStatusResponse
                )

            val errorException = MessageErrorException()

            coEvery {
                getTokoFoodOrderStatusUseCase.get().execute(ORDER_ID_DUMMY)
            } returns orderStatusResultUiModel

            coEvery {
                getTokoFoodOrderDetailUseCase.get().execute(ORDER_ID_DUMMY)
            } throws errorException

            viewModel.updateOrderId(ORDER_ID_DUMMY)
            delay(6000L)

            val actualResult = (viewModel.orderCompletedLiveTracking.observeAwaitValue() as Fail)
            assertEquals(errorException::class.java, actualResult.throwable::class.java)

            coVerify {
                getTokoFoodOrderStatusUseCase.get().execute(ORDER_ID_DUMMY)
            }
        }
    }

    @Test
    fun `when getUnreadChatCount should return set live data success`() {
        runTest {
            // Given
            val expectedUnreadCount = 5
            every {
                tokoChatCounterUseCase.get().fetchUnreadCount(CHANNEL_ID)
            } returns flow {
                emit(TokoChatResult.Success(expectedUnreadCount))
            }

            viewModel.chatCounterUiState.test {
                // When
                viewModel.channelId = CHANNEL_ID
                viewModel.fetchUnReadChatCount()

                skipItems(1)

                // Then
                val updatedValue = awaitItem()
                assertEquals(updatedValue.counter, 5)
                assertEquals(updatedValue.error, null)
                cancelAndConsumeRemainingEvents()
            }

            verify {
                tokoChatCounterUseCase.get().fetchUnreadCount(CHANNEL_ID)
            }
        }
    }

    @Test
    fun `when getUnreadChatCount, return loading`() {
        runTest {
            // Given
            every {
                tokoChatCounterUseCase.get().fetchUnreadCount(CHANNEL_ID)
            } returns flow {
                emit(TokoChatResult.Loading)
            }

            viewModel.chatCounterUiState.test {
                // When
                viewModel.channelId = CHANNEL_ID
                viewModel.fetchUnReadChatCount()

                // Then
                val initialValue = awaitItem()
                assertEquals(initialValue.counter, 0)
                assertEquals(initialValue.error, null)

                cancelAndConsumeRemainingEvents()
            }

            verify {
                tokoChatCounterUseCase.get().fetchUnreadCount(CHANNEL_ID)
            }
        }
    }

    @Test
    fun `when getUnreadChatCount should set live data error`() {
        runTest {
            // Given
            val errorException = Throwable()
            every {
                tokoChatCounterUseCase.get().fetchUnreadCount(CHANNEL_ID)
            } returns flow {
                emit(TokoChatResult.Error(errorException))
            }

            viewModel.chatCounterUiState.test {
                // When
                viewModel.channelId = CHANNEL_ID
                viewModel.fetchUnReadChatCount()

                skipItems(1)

                // Then
                val updatedValue = awaitItem()
                assertEquals(updatedValue.counter, 0)
                assertEquals(updatedValue.error, errorException)

                cancelAndConsumeRemainingEvents()
            }

            verify {
                tokoChatCounterUseCase.get().fetchUnreadCount(CHANNEL_ID)
            }
        }
    }

    @Test
    fun `when getUnreadChatCount, throw error`() {
        runTest {
            // Given
            val expectedThrowable = Throwable("Oops!")
            every {
                tokoChatCounterUseCase.get().fetchUnreadCount(CHANNEL_ID)
            } throws expectedThrowable

            // When
            viewModel.channelId = CHANNEL_ID
            viewModel.fetchUnReadChatCount()

            // Then
            verify {
                tokoChatCounterUseCase.get().fetchUnreadCount(CHANNEL_ID)
            }
        }
    }

    @Test
    fun `when initGroupBooking, get success channel id`() {
        runTest {
            // Given
            val expectedChannelUrl = "testChannelUrl"
            every {
                tokoChatGroupBookingUseCase.get().initGroupBookingChat(
                    orderId = ORDER_ID_DUMMY,
                    serviceType = TokoChatServiceType.TOKOFOOD
                )
            } returns flow {
                emit(TokoChatResult.Success(expectedChannelUrl))
            }

            viewModel.groupBookingUiState.test {
                // When
                viewModel.initGroupBooking(ORDER_ID_DUMMY)

                // Then
                val updatedItem = awaitItem()
                assertEquals(updatedItem.channelUrl, expectedChannelUrl)
                assertEquals(updatedItem.error, null)

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `when initGroupBooking, get error conversation`() {
        runTest {
            // Given
            val expectedThrowable = Throwable()
            every {
                tokoChatGroupBookingUseCase.get().initGroupBookingChat(
                    orderId = ORDER_ID_DUMMY,
                    serviceType = TokoChatServiceType.TOKOFOOD
                )
            } returns flow {
                emit(TokoChatResult.Error(expectedThrowable))
            }

            viewModel.groupBookingUiState.test {
                // When
                viewModel.initGroupBooking(ORDER_ID_DUMMY)

                // Then
                val updatedItem = awaitItem()
                assertEquals(updatedItem.channelUrl, "")
                assertEquals(updatedItem.error, expectedThrowable)

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `when init group booking, get loading`() {
        runTest {
            // Given
            every {
                tokoChatGroupBookingUseCase.get().initGroupBookingChat(
                    orderId = ORDER_ID_DUMMY,
                    serviceType = TokoChatServiceType.TOKOFOOD
                )
            } returns flow {
                emit(TokoChatResult.Loading)
            }

            viewModel.groupBookingUiState.test {
                // When
                viewModel.initGroupBooking(ORDER_ID_DUMMY)

                // Then
                verify {
                    tokoChatGroupBookingUseCase.get().initGroupBookingChat(
                        orderId = ORDER_ID_DUMMY,
                        serviceType = TokoChatServiceType.TOKOFOOD
                    )
                }

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `when init group booking, throw error`() {
        runTest {
            // Given
            val expectedThrowable = Throwable("Oops!")
            every {
                tokoChatGroupBookingUseCase.get().initGroupBookingChat(
                    orderId = ORDER_ID_DUMMY,
                    serviceType = TokoChatServiceType.TOKOFOOD
                )
            } throws expectedThrowable

            // When
            viewModel.initGroupBooking(ORDER_ID_DUMMY)

            // Then
            val result = viewModel.mutationProfile.value as Fail
            assertEquals(result.throwable.message, expectedThrowable.message)
        }
    }
}
