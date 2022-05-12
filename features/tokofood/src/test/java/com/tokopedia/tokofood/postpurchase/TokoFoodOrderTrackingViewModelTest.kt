package com.tokopedia.tokofood.postpurchase

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.feature.ordertracking.domain.model.TokoFoodOrderDetailResponse
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.DriverPhoneNumberUiModel
import com.tokopedia.tokofood.utils.JsonResourcesUtil
import com.tokopedia.tokofood.utils.observeAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString

class TokoFoodOrderTrackingViewModelTest: TokoFoodOrderTrackingViewModelTestFixture() {

    @Test
    fun `when fetchOrderDetail should return set live data success`() {
        runBlocking {
            val jsonResponse = JsonResourcesUtil.createSuccessResponse<TokoFoodOrderDetailResponse>(ORDER_TRACKING_SUCCESS).tokofoodOrderDetail
            val orderDetailResultUiModel = tokoFoodOrderDetailMapper.mapToOrderDetailResultUiModel(jsonResponse)
            coEvery {
                getTokoFoodOrderDetailUseCase.get().executeTemp(anyInt())
            } returns orderDetailResultUiModel

            viewModel.fetchOrderDetail(anyString(), anyInt())

            coVerify {
                getTokoFoodOrderDetailUseCase.get().executeTemp(anyInt())
            }

            val expectedResult = (viewModel.orderDetailResult.observeAwaitValue() as Success).data
            assertEquals(expectedResult.orderStatus, orderDetailResultUiModel.orderStatus)
            assertEquals(expectedResult.orderDetailList, orderDetailResultUiModel.orderDetailList)
            assertEquals(expectedResult.foodItemList, orderDetailResultUiModel.foodItemList)
            assertTrue(viewModel.getFoodItems().isNotEmpty())
        }
    }

    @Test
    fun `when fetchOrderDetail should set live data error`() {
        runBlocking {
            val errorException = MessageErrorException()

            coEvery {
                getTokoFoodOrderDetailUseCase.get().executeTemp(anyInt())
            } throws errorException

            viewModel.fetchOrderDetail(anyString(), anyInt())

            coVerify {
                getTokoFoodOrderDetailUseCase.get().executeTemp(anyInt())
            }

            val actualResult = (viewModel.orderDetailResult.value as Fail).throwable::class.java
            val expectedResult = errorException::class.java
            Assert.assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when fetchDriverPhoneNumber should return set live data success`() {
        runBlocking {
            val driverPhoneNumberUiModel = DriverPhoneNumberUiModel(isCallable = true, phoneNumber = "0812345646")

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
            Assert.assertEquals(expectedResult, actualResult)
        }
    }
}