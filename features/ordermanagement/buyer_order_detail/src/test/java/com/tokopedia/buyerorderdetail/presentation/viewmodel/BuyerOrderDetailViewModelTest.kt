package com.tokopedia.buyerorderdetail.presentation.viewmodel

import com.tokopedia.buyerorderdetail.domain.models.FinishOrderParams
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailParams
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.model.BuyerOrderDetailUiModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

class BuyerOrderDetailViewModelTest: BuyerOrderDetailViewModelTestFixture() {
    @Test
    fun `getBuyerOrderDetail should success when use case return expected data`() {
        val expectedParams = GetBuyerOrderDetailParams(orderId = orderId, paymentId = paymentId, cart = cart)

        createSuccessBuyerOrderDetailResult(mockk())

        coVerify {
            getBuyerOrderDetailUseCase.execute(expectedParams)
        }

        val result = viewModel.buyerOrderDetailResult.value
        assert(result is Success)
    }

    @Test
    fun `getBuyerOrderDetail should fail when use case throw an exception`() {
        val expectedParams = GetBuyerOrderDetailParams(orderId = orderId, paymentId = paymentId, cart = cart)

        createFailedBuyerOrderDetailResult()

        coVerify {
            getBuyerOrderDetailUseCase.execute(expectedParams)
        }

        assert(viewModel.buyerOrderDetailResult.value is Fail)
    }

    @Test
    fun `finishOrder should success when set order as delivered`() {
        val expectedParams = FinishOrderParams(orderId = orderId, userId = userId, action = "event_dialog_deliver_finish")
        val buyerOrderDetailResult = mockk<BuyerOrderDetailUiModel>(relaxed = true) {
            every { orderStatusUiModel.orderStatusHeaderUiModel.orderStatusId } returns "540"
            every { orderStatusUiModel.orderStatusHeaderUiModel.orderId } returns orderId
        }

        createSuccessBuyerOrderDetailResult(buyerOrderDetailResult)
        viewModel.finishOrder()

        coVerify {
            finishOrderUseCase.execute(expectedParams)
        }

        val result = viewModel.finishOrderResult.value
        assert(result is Success)
    }

    @Test
    fun `finishOrder should success when set order as arrived`() {
        val expectedParams = FinishOrderParams(orderId = orderId, userId = userId, action = "")
        val buyerOrderDetailResult = mockk<BuyerOrderDetailUiModel>(relaxed = true) {
            every { orderStatusUiModel.orderStatusHeaderUiModel.orderStatusId } returns "600"
            every { orderStatusUiModel.orderStatusHeaderUiModel.orderId } returns orderId
        }

        createSuccessBuyerOrderDetailResult(buyerOrderDetailResult)
        viewModel.finishOrder()

        coVerify {
            finishOrderUseCase.execute(expectedParams)
        }

        val result = viewModel.finishOrderResult.value
        assert(result is Success)
    }

    @Test
    fun `finishOrder should failed when orderId is invalid and use case throw an exception`() {
        val expectedErrorMessage = "Tidak dapat menyelesaikan pesanan, silahkan muat ulang dan coba lagi!"
        val expectedException = MessageErrorException(expectedErrorMessage)
        val expectedParams = FinishOrderParams(orderId = "0", userId = userId, action = "")

        createFailedBuyerOrderDetailResult()

        coEvery {
            finishOrderUseCase.execute(any())
        } throws expectedException

        viewModel.finishOrder()

        coVerify {
            finishOrderUseCase.execute(expectedParams)
        }

        val result = viewModel.finishOrderResult.value
        assert(result is Fail && result.throwable is MessageErrorException && result.throwable.message == expectedErrorMessage)
    }

    @Test
    fun `addSingleToCart should success when atc use case return expected data`() {
        val buyerOrderDetailResult = mockk<BuyerOrderDetailUiModel>(relaxed = true) {
            every { productListUiModel.productListHeaderUiModel.shopId } returns shopId
        }

        coEvery { atcUseCase.execute(any(), any(), any()) } returns Success(mockk())

        createSuccessBuyerOrderDetailResult(buyerOrderDetailResult)
        viewModel.addSingleToCart(product)

        coVerify { atcUseCase.execute(userId, "", atcExpectedParams) }

        val result = viewModel.singleAtcResult.value
        assert(result != null && result.first == product && result.second is Success)
    }

    @Test
    fun `addSingleToCart should failed when atc use case throw an exception`() {
        coEvery { atcUseCase.execute(any(), any(), any()) } throws Throwable()

        createFailedBuyerOrderDetailResult()
        viewModel.addSingleToCart(product)

        coVerify { atcUseCase.execute(userId, "", any()) }

        val result = viewModel.singleAtcResult.value
        assert(result != null && result.first == product && result.second is Fail)
    }

    @Test
    fun `addMultipleToCart should success when atc use case return expected data`() {
        val buyerOrderDetailResult = mockk<BuyerOrderDetailUiModel>(relaxed = true) {
            every { productListUiModel.productListHeaderUiModel.shopId } returns shopId
            every { productListUiModel.productList } returns listOf(product)
        }

        coEvery { atcUseCase.execute(any(), any(), any()) } returns Success(mockk())

        createSuccessBuyerOrderDetailResult(buyerOrderDetailResult)
        viewModel.addMultipleToCart()

        coVerify { atcUseCase.execute(userId, "", atcExpectedParams) }

        val result = viewModel.multiAtcResult.value
        assert(result != null && result is Success)
    }

    @Test
    fun `addMultipleToCart should failed when atc use case throw an exception`() {
        val buyerOrderDetailResult = mockk<BuyerOrderDetailUiModel>(relaxed = true) {
            every { productListUiModel.productListHeaderUiModel.shopId } returns shopId
            every { productListUiModel.productList } returns listOf(product)
        }

        coEvery { atcUseCase.execute(any(), any(), any()) } throws Throwable()

        createSuccessBuyerOrderDetailResult(buyerOrderDetailResult)
        viewModel.addMultipleToCart()

        coVerify { atcUseCase.execute(userId, "", atcExpectedParams) }

        val result = viewModel.multiAtcResult.value
        assert(result != null && result is Fail)
    }

    @Test
    fun `addMultipleToCart should failed when getBuyerOrderDetail result is not success`() {
        coEvery { atcUseCase.execute(any(), any(), any()) } throws Throwable()

        createFailedBuyerOrderDetailResult()
        viewModel.addMultipleToCart()

        coVerify(inverse = true) { atcUseCase.execute(any(), any(), any()) }

        val result = viewModel.multiAtcResult.value
        assert(result != null && result is Fail)
    }

    @Test
    fun `getSecondaryActionButtons should return list of ActionButton when getBuyerOrderDetail result is success`() {
        val actionButton = mockk<ActionButtonsUiModel.ActionButton>(relaxed = true)
        val buyerOrderDetailResult = mockk<BuyerOrderDetailUiModel>(relaxed = true) {
            every { actionButtonsUiModel.secondaryActionButtons } returns listOf(actionButton)
        }

        createSuccessBuyerOrderDetailResult(buyerOrderDetailResult)
        val actionButtonList = viewModel.getSecondaryActionButtons()
        assert(actionButtonList.isNotEmpty() && actionButtonList.firstOrNull() == actionButton)
    }

    @Test
    fun `getSecondaryActionButtons should return empty list when getBuyerOrderDetail result is fail`() {
        createFailedBuyerOrderDetailResult()
        val actionButtonList = viewModel.getSecondaryActionButtons()
        assert(actionButtonList.isEmpty())
    }
}