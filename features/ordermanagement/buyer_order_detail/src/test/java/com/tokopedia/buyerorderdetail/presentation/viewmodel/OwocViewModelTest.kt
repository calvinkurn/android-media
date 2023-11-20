package com.tokopedia.buyerorderdetail.presentation.viewmodel

import com.tokopedia.buyerorderdetail.presentation.model.OwocGroupedOrderWrapper
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class OwocViewModelTest : OwocViewModelTestFixture() {

    @Test
    fun `when fetchBomGroupedOrder should set live data success`() {
        runBlocking {
            // given
            val owocTitle = "3 Pesanan"
            val partialOrderFulfillmentWrapperUiModel = OwocGroupedOrderWrapper(owocTitle = owocTitle)
            onGetBomGroupedOrderUseCase_thenReturn(
                partialOrderFulfillmentWrapperUiModel,
                orderId,
                txId
            )

            // when
            viewModel.fetchBomGroupedOrder(txId, orderId)

            // then
            verifyGetBomGroupedOrderUseCaseUseCaseCalled(orderId, txId)
            val actualResult = (viewModel.owocGroupedOrderWrapper.value as Success).data
            assertEquals(partialOrderFulfillmentWrapperUiModel, actualResult)
            assertEquals(
                partialOrderFulfillmentWrapperUiModel.owocGroupedOrderList.size,
                actualResult.owocGroupedOrderList.size
            )
            assertEquals(owocTitle, actualResult.owocTitle)
        }
    }

    @Test
    fun `when fetchBomGroupedOrder should set live data error`() {
        runBlocking {
            // given
            val errorException = MessageErrorException()
            onGetBomGroupedOrderUseCase_thenReturnError(orderId, txId, errorException)

            // when
            viewModel.fetchBomGroupedOrder(txId, orderId)

            // then
            verifyGetBomGroupedOrderUseCaseUseCaseCalled(orderId, txId)

            val actualResult =
                (viewModel.owocGroupedOrderWrapper.value as Fail).throwable::class.java
            val expectedResult = errorException::class.java
            assertEquals(expectedResult, actualResult)
        }
    }
}
