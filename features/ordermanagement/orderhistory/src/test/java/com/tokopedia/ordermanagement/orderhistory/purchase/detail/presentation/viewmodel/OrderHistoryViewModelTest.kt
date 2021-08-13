package com.tokopedia.ordermanagement.orderhistory.purchase.detail.presentation.viewmodel

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.OrderHistoryResult
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.viewmodel.OrderHistoryData
import com.tokopedia.unit.test.ext.verifyValueEquals
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString

class OrderHistoryViewModelTest : OrderHistoryViewModelTestFixture() {

    @Test
    fun `when getOrderHistory success should set expected response`() {
        val expectedResponse = OrderHistoryData()

        onGetOrderHistorySuccess_thenReturn(expectedResponse)

        viewModel.getOrderHistory(anyString(), anyInt())

        verifyOrderHistoryUseCaseCalled()
        verifyOrderHistoryValueEquals(OrderHistoryResult.OrderHistorySuccess(expectedResponse))
    }

    @Test
    fun `when getOrderHistory fail should set expected response`() {
        val expectedResponse = MessageErrorException()

        onGetOrderHistoryFail_thenReturn(expectedResponse)

        viewModel.getOrderHistory(anyString(), anyInt())

        verifyOrderHistoryUseCaseCalled()
        verifyOrderHistoryValueEquals(OrderHistoryResult.OrderHistoryFail(expectedResponse))
    }

    private fun onGetOrderHistorySuccess_thenReturn(orderHistoryData: OrderHistoryData) {
        coEvery { orderHistoryUseCase.executeOnBackground() } returns orderHistoryData
    }

    private fun onGetOrderHistoryFail_thenReturn(throwable: Throwable) {
        coEvery { orderHistoryUseCase.executeOnBackground() } throws throwable
    }

    private fun verifyOrderHistoryUseCaseCalled() {
        coVerify { orderHistoryUseCase.executeOnBackground() }
    }

    private fun verifyOrderHistoryValueEquals(result: OrderHistoryResult) {
        viewModel.orderHistory.verifyValueEquals(result)
    }
}