package com.tokopedia.buyerorderdetail.presentation.viewmodel

import com.tokopedia.buyerorderdetail.presentation.model.OrderExtensionRespondInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OrderExtensionRespondUiModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class BuyerOrderExtensionViewModelTest : BuyerOrderExtensionViewModelTestFixture() {

    @Test
    fun `when getOrderExtensionRespondInfo should set live data success`() {
        runBlocking {
            val orderExtensionRespondInfoUiModel = OrderExtensionRespondInfoUiModel(
                orderId = orderId,
                confirmationTitle = "Estimasi proses pesananmu",
                rejectText = "Pesanan tidak akan diproses",
                newDeadline = "Senin, 13 Sep 2021",
                reasonExtension = "Kendala Operational",
                messageCode = 1,
                message = "Success"
            )
            onGetOrderExtensionRespondInfoUseCase_thenReturn(orderExtensionRespondInfoUiModel)
            viewModel.requestRespondInfo(orderId)
            verifyGetOrderExtensionRespondInfoUseCaseCalled()
            val actualResult = (viewModel.orderExtensionRespondInfo.value as Success).data
            assertEquals(orderExtensionRespondInfoUiModel, actualResult)
            assertEquals(orderExtensionRespondInfoUiModel.confirmationTitle, actualResult.confirmationTitle)
            assertEquals(orderExtensionRespondInfoUiModel.rejectText, actualResult.rejectText)
            assertEquals(orderExtensionRespondInfoUiModel.message, actualResult.message)
            assertEquals(orderExtensionRespondInfoUiModel.messageCode, actualResult.messageCode)
        }
    }

    @Test
    fun `when getOrderExtensionRespondInfo should set live data error`() {
        runBlocking {
            val errorException = MessageErrorException()
            onGetOrderExtensionRespondInfoUseCase_thenReturn(errorException)
            viewModel.requestRespondInfo(orderId)
            verifyGetOrderExtensionRespondInfoUseCaseCalled()
            val actualResult = (viewModel.orderExtensionRespondInfo.value as Fail).throwable::class.java
            val expectedResult = errorException::class.java
            assertEquals(expectedResult, actualResult)
        }
    }


    @Test
    fun `when insertOrderExtensionRespond should set live data success`() {
        runBlocking {
            val actionType = 1
            val orderExtensionRespondUiModel = OrderExtensionRespondUiModel(
                messageCode = 1,
                message = "Perpanjang waktu proses pesanan diterima.",
                actionType = actionType
            )
            onInsertOrderExtensionRespondUseCase_thenReturn(orderExtensionRespondUiModel)
            viewModel.requestRespond(orderId, actionType)
            verifyInsertOrderExtensionRespondUseCaseCalled()
            val actualResult = (viewModel.orderExtensionRespond.value as Success).data
            assertEquals(orderExtensionRespondUiModel, actualResult)
            assertEquals(orderExtensionRespondUiModel.message, actualResult.message)
            assertEquals(orderExtensionRespondUiModel.messageCode, actualResult.messageCode)
            assertEquals(orderExtensionRespondUiModel.actionType, actualResult.actionType)
        }
    }

    @Test
    fun `when insertOrderExtensionRespond should set live data error`() {
        runBlocking {
            val errorException = MessageErrorException()
            onInsertOrderExtensionRespondUseCaseError_thenReturn(errorException)
            viewModel.requestRespond(orderId, 1)
            verifyInsertOrderExtensionRespondUseCaseCalled()
            val actualResult = (viewModel.orderExtensionRespond.value as Fail).throwable::class.java
            val expectedResult = errorException::class.java
            assertEquals(expectedResult, actualResult)
        }
    }
}