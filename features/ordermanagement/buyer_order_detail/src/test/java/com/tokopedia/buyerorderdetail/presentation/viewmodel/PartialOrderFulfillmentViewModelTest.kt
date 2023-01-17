package com.tokopedia.buyerorderdetail.presentation.viewmodel

import com.tokopedia.buyerorderdetail.presentation.model.ApprovePartialOrderFulfillmentUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PartialOrderFulfillmentWrapperUiModel
import com.tokopedia.buyerorderdetail.presentation.model.RejectPartialOrderFulfillmentUiModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class PartialOrderFulfillmentViewModelTest : PartialOrderFulfillmentViewModelTestFixture() {

    @Test
    fun `when getPartialOrderFulfillment should set live data success`() {
        runBlocking {
            // given
            val orderIdLong = orderId.toLong()
            val partialOrderFulfillmentWrapperUiModel = PartialOrderFulfillmentWrapperUiModel()
            onGetPartialOrderFulfillmentUseCase_thenReturn(
                partialOrderFulfillmentWrapperUiModel,
                orderIdLong
            )

            // when
            viewModel.fetchPartialOrderFulfillment(orderIdLong)

            // then
            verifyGetOrderExtensionRespondInfoUseCaseCalled(orderIdLong)
            val actualResult = (viewModel.partialOrderFulfillmentRespondInfo.value as Success).data
            assertEquals(partialOrderFulfillmentWrapperUiModel, actualResult)
            assertEquals(
                partialOrderFulfillmentWrapperUiModel.partialOrderFulfillmentUiModelList.size,
                actualResult.partialOrderFulfillmentUiModelList.size
            )
        }
    }

    @Test
    fun `when getPartialOrderFulfillment should set live data error`() {
        runBlocking {
            // given
            val orderIdLong = orderId.toLong()
            val errorException = MessageErrorException()
            onGetPartialOrderFulfillmentUseCase_thenReturnError(orderIdLong, errorException)

            // when
            viewModel.fetchPartialOrderFulfillment(orderIdLong)

            // then
            verifyGetOrderExtensionRespondInfoUseCaseCalled(orderIdLong)

            val actualResult =
                (viewModel.partialOrderFulfillmentRespondInfo.value as Fail).throwable::class.java
            val expectedResult = errorException::class.java
            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when approvePartialOrderFulfillment should set live data success`() {
        runBlocking {
            // given
            val orderIdLong = orderId.toLong()
            val approvePartialOrderFulfillmentUiModel = ApprovePartialOrderFulfillmentUiModel(
                isSuccess = true
            )
            onApprovePartialOrderFulfillmentUseCase_thenReturn(approvePartialOrderFulfillmentUiModel, orderIdLong)

            // when
            viewModel.approvePartialOrderFulfillment(orderIdLong)

            // then
            verifyApprovePartialOrderFulfillmentUseCaseCalled(orderIdLong)
            val actualResult = (viewModel.approvePartialOrderFulfillment.value as Success).data
            assertEquals(approvePartialOrderFulfillmentUiModel.isSuccess, actualResult.isSuccess)
        }
    }

    @Test
    fun `when approvePartialOrderFulfillment should set live data error`() {
        runBlocking {
            // given
            val orderIdLong = orderId.toLong()
            val errorException = MessageErrorException()
            onApprovePartialOrderFulfillmentUseCase_thenReturnError(errorException, orderIdLong)

            // when
            viewModel.approvePartialOrderFulfillment(orderIdLong)

            // then
            verifyApprovePartialOrderFulfillmentUseCaseCalled(orderIdLong)

            val actualResult = (viewModel.approvePartialOrderFulfillment.value as Fail).throwable::class.java
            val expectedResult = errorException::class.java
            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when rejectPartialOrderFulfillment should set live data success`() {
        runBlocking {
            // given
            val orderIdLong = orderId.toLong()
            val rejectPartialOrderFulfillmentUiModel = RejectPartialOrderFulfillmentUiModel(
                isSuccess = true
            )
            onRejectPartialOrderFulfillmentUseCase_thenReturn(rejectPartialOrderFulfillmentUiModel, orderIdLong)

            // when
            viewModel.rejectPartialOrderFulfillment(orderIdLong)

            // then
            verifyRejectPartialOrderFulfillmentUseCaseCalled(orderIdLong)
            val actualResult = (viewModel.rejectPartialOrderFulfillment.value as Success).data
            assertEquals(rejectPartialOrderFulfillmentUiModel.isSuccess, actualResult.isSuccess)
        }
    }

    @Test
    fun `when rejectPartialOrderFulfillment should set live data error`() {
        runBlocking {
            // given
            val orderIdLong = orderId.toLong()
            val errorException = MessageErrorException()
            onRejectPartialOrderFulfillmentUseCase_thenReturnError(errorException, orderIdLong)

            // when
            viewModel.rejectPartialOrderFulfillment(orderIdLong)

            // then
            verifyRejectPartialOrderFulfillmentUseCaseCalled(orderIdLong)
            val actualResult = (viewModel.rejectPartialOrderFulfillment.value as Fail).throwable::class.java
            val expectedResult = errorException::class.java
            assertEquals(expectedResult, actualResult)
        }
    }
}
