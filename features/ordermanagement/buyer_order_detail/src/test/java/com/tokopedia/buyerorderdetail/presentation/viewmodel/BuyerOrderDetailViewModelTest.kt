package com.tokopedia.buyerorderdetail.presentation.viewmodel

import com.tokopedia.buyerorderdetail.domain.models.FinishOrderParams
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataParams
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.model.MultiATCState
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.ActionButtonsUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.BuyerOrderDetailUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.OrderStatusUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.ProductListUiState
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test

class BuyerOrderDetailViewModelTest : BuyerOrderDetailViewModelTestFixture() {

    @Test
    fun `getBuyerOrderDetailData should execute UseCase once with expected params`() {
        runCollectingUiState {
            val expectedParams = GetBuyerOrderDetailDataParams(
                cart = cart,
                orderId = orderId,
                paymentId = paymentId,
                shouldCheckCache = false
            )

            viewModel.getBuyerOrderDetailData(orderId, paymentId, cart, false)

            coVerify(exactly = 1) { getBuyerOrderDetailDataUseCase(expectedParams) }
        }
    }

    @Test
    fun `UI state should equals to Showing when getP0DataRequestState is Success`() =
        runCollectingUiState { uiStates ->
            createSuccessGetBuyerOrderDetailDataResult(
                getBuyerOrderDetailResult = mockk(relaxed = true) {
                    every { getPodInfo() } returns null
                }
            )

            viewModel.getBuyerOrderDetailData(
                orderId = orderId,
                paymentId = paymentId,
                cart = cart,
                shouldCheckCache = false
            )

            assertTrue(uiStates[0] is BuyerOrderDetailUiState.FullscreenLoading)
            assertTrue(uiStates[1] is BuyerOrderDetailUiState.HasData.Showing) // showing without P1 data
            assertTrue(uiStates[2] is BuyerOrderDetailUiState.HasData.Showing) // showing with P1 data
        }

    @Test
    fun `UI state should equals to Error when getP0DataRequestState is Error`() =
        runCollectingUiState { uiStates ->
            createFailedGetBuyerOrderDetailDataResult()

            viewModel.getBuyerOrderDetailData(orderId, paymentId, cart, false)

            assertTrue(uiStates[0] is BuyerOrderDetailUiState.FullscreenLoading)
            assertTrue(uiStates[1] is BuyerOrderDetailUiState.Error)
        }

    @Test
    fun `UI state should equals to PullRefreshLoading when reloading P0 data`() =
        runCollectingUiState { uiStates ->
            createSuccessGetBuyerOrderDetailDataResult(
                getBuyerOrderDetailResult = mockk(relaxed = true) {
                    every { getPodInfo() } returns null
                }
            )

            viewModel.getBuyerOrderDetailData(
                orderId = orderId,
                paymentId = paymentId,
                cart = cart,
                shouldCheckCache = false
            )
            // reload
            viewModel.getBuyerOrderDetailData(
                orderId = orderId,
                paymentId = paymentId,
                cart = cart,
                shouldCheckCache = false
            )

            assertTrue(uiStates[0] is BuyerOrderDetailUiState.FullscreenLoading)
            assertTrue(uiStates[1] is BuyerOrderDetailUiState.HasData.Showing) // showing without P1 data
            assertTrue(uiStates[2] is BuyerOrderDetailUiState.HasData.Showing) // showing with P1 data
            for (i in 3 until uiStates.size.dec()) {
                assertTrue(uiStates[i] is BuyerOrderDetailUiState.HasData.PullRefreshLoading)
            }
            assertTrue(uiStates[uiStates.size - 1] is BuyerOrderDetailUiState.HasData.Showing) // showing with P1 data
        }

    @Test
    fun `finishOrder should execute UseCase with expected params`() = runCollectingUiState {
        val expectedParams = FinishOrderParams(
            orderId = orderId,
            userId = userId,
            action = "event_dialog_deliver_finish"
        )
        val orderStatusShowingState = mockk<OrderStatusUiState.HasData.Showing>(relaxed = true) {
            every { data.orderStatusHeaderUiModel.orderStatusId } returns "540"
            every { data.orderStatusHeaderUiModel.orderId } returns orderId
        }

        createSuccessGetBuyerOrderDetailDataResult()
        createSuccessFinishOrderResult()
        mockOrderStatusUiStateMapper(showingState = orderStatusShowingState) {
            viewModel.getBuyerOrderDetailData(orderId, paymentId, cart, false)
            viewModel.finishOrder()

            coVerify {
                finishOrderUseCase.execute(expectedParams)
            }
        }
    }

    @Test
    fun `finishOrder should success when set order as delivered`() = runCollectingUiState {
        val orderStatusShowingState = mockk<OrderStatusUiState.HasData.Showing>(relaxed = true) {
            every { data.orderStatusHeaderUiModel.orderStatusId } returns "540"
            every { data.orderStatusHeaderUiModel.orderId } returns orderId
        }

        createSuccessGetBuyerOrderDetailDataResult()
        mockOrderStatusUiStateMapper(showingState = orderStatusShowingState) {
            viewModel.getBuyerOrderDetailData(orderId, paymentId, cart, false)
            viewModel.finishOrder()

            assertTrue(viewModel.finishOrderResult.value is Success)
        }
    }

    @Test
    fun `finishOrder should success when set order as arrived`() = runCollectingUiState {
        val orderStatusShowingState = mockk<OrderStatusUiState.HasData.Showing>(relaxed = true) {
            every { data.orderStatusHeaderUiModel.orderStatusId } returns "600"
            every { data.orderStatusHeaderUiModel.orderId } returns orderId
        }

        createSuccessGetBuyerOrderDetailDataResult()
        createSuccessFinishOrderResult()
        mockOrderStatusUiStateMapper(showingState = orderStatusShowingState) {
            viewModel.getBuyerOrderDetailData(orderId, paymentId, cart, false)
            viewModel.finishOrder()

            assertTrue(viewModel.finishOrderResult.value is Success)
        }
    }

    @Test
    fun `finishOrder should failed when orderId is invalid and use case throw an exception`() =
        runCollectingUiState {
            val expectedErrorMessage = "Tidak dapat menyelesaikan pesanan, silahkan muat ulang dan coba lagi!"
            val expectedException = MessageErrorException(expectedErrorMessage)
            val orderStatusShowingState = mockk<OrderStatusUiState.HasData.Showing>(relaxed = true) {
                every { data.orderStatusHeaderUiModel.orderStatusId } returns "600"
                every { data.orderStatusHeaderUiModel.orderId } returns orderId
            }

            createSuccessGetBuyerOrderDetailDataResult()
            createFailedFinishOrderResult(expectedException)
            mockOrderStatusUiStateMapper(showingState = orderStatusShowingState) {
                viewModel.getBuyerOrderDetailData(orderId, paymentId, cart, false)
                viewModel.finishOrder()

                val result = viewModel.finishOrderResult.value as Fail
                assertTrue(result.throwable is MessageErrorException)
                assertEquals(expectedErrorMessage, result.throwable.message)
            }
        }

    @Test
    fun `addSingleToCart should execute UseCase with expected params`() = runCollectingUiState {
        val productListShowingState = mockk<ProductListUiState.HasData.Showing>(relaxed = true) {
            every { data.productListHeaderUiModel.shopId } returns shopId
        }

        createSuccessGetBuyerOrderDetailDataResult()
        createSuccessATCResult()
        mockProductListUiStateMapper(showingState = productListShowingState) {
            viewModel.getBuyerOrderDetailData(
                orderId = orderId,
                paymentId = paymentId,
                cart = cart,
                shouldCheckCache = false
            )
            viewModel.addSingleToCart(product)

            coVerify(exactly = 1) { atcUseCase.execute(userId, "", atcExpectedParams) }
        }
    }

    @Test
    fun `addSingleToCart should success when atc use case return expected data`() =
        runCollectingUiState {
            val productListShowingState = mockk<ProductListUiState.HasData.Showing>(relaxed = true) {
                every { data.productListHeaderUiModel.shopId } returns shopId
            }

            createSuccessGetBuyerOrderDetailDataResult()
            createSuccessATCResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                viewModel.getBuyerOrderDetailData(
                orderId = orderId,
                paymentId = paymentId,
                cart = cart,
                shouldCheckCache = false
            )
                viewModel.addSingleToCart(product)

                val result = viewModel.singleAtcResult.value
                assertNotNull(result)
                assertEquals(product, result?.first)
                assertTrue(result?.second is Success)
            }
        }

    @Test
    fun `addSingleToCart should failed when atc use case throw an exception`() =
        runCollectingUiState {
            val productListShowingState = mockk<ProductListUiState.HasData.Showing>(relaxed = true) {
                every { data.productListHeaderUiModel.shopId } returns shopId
            }

            createSuccessGetBuyerOrderDetailDataResult()
            createFailedATCResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                viewModel.getBuyerOrderDetailData(
                orderId = orderId,
                paymentId = paymentId,
                cart = cart,
                shouldCheckCache = false
            )
                viewModel.addSingleToCart(product)

                val result = viewModel.singleAtcResult.value
                assertNotNull(result)
                assertEquals(product, result?.first)
                assertTrue(result?.second is Fail)
            }
        }

    @Test
    fun `addMultipleToCart should execute UseCase with expected params when UI state is equals to Showing`() =
        runCollectingUiState {
            val productListShowingState = mockk<ProductListUiState.HasData.Showing>(relaxed = true) {
                every { data.productListHeaderUiModel.shopId } returns shopId
                every { data.productList } returns listOf(product)
            }

            createSuccessGetBuyerOrderDetailDataResult()
            createSuccessATCResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                viewModel.getBuyerOrderDetailData(
                orderId = orderId,
                paymentId = paymentId,
                cart = cart,
                shouldCheckCache = false
            )
                viewModel.addMultipleToCart()

                coVerify(exactly = 1) { atcUseCase.execute(userId, "", atcExpectedParams) }
            }
        }

    @Test
    fun `addMultipleToCart should not execute UseCase when UI state is not equals to Showing`() =
        runCollectingUiState {
            createFailedGetBuyerOrderDetailDataResult()

            viewModel.getBuyerOrderDetailData(
                orderId = orderId,
                paymentId = paymentId,
                cart = cart,
                shouldCheckCache = false
            )
            viewModel.addMultipleToCart()

            coVerify(inverse = true) { atcUseCase.execute(any(), any(), any()) }
        }

    @Test
    fun `addMultipleToCart should success when atc use case return expected data`() =
        runCollectingUiState {
            val productListShowingState = mockk<ProductListUiState.HasData.Showing>(relaxed = true) {
                every { data.productListHeaderUiModel.shopId } returns shopId
                every { data.productList } returns listOf(product)
            }

            createSuccessGetBuyerOrderDetailDataResult()
            createSuccessATCResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                viewModel.getBuyerOrderDetailData(orderId, paymentId, cart, false)
                viewModel.addMultipleToCart()

                val result = viewModel.multiAtcResult.value
                assertNotNull(result)
                assertTrue(result is MultiATCState.Success)
            }
        }

    @Test
    fun `addMultipleToCart should failed when atc use case return fail`() = runCollectingUiState {
        val productListShowingState = mockk<ProductListUiState.HasData.Showing>(relaxed = true) {
            every { data.productListHeaderUiModel.shopId } returns shopId
            every { data.productList } returns listOf(product)
        }

        createSuccessGetBuyerOrderDetailDataResult()
        createSuccessATCResult(Fail(mockk(relaxed = true)))
        mockProductListUiStateMapper(showingState = productListShowingState) {
            viewModel.getBuyerOrderDetailData(orderId, paymentId, cart, false)
            viewModel.addMultipleToCart()

            val result = viewModel.multiAtcResult.value
            assertNotNull(result)
            assertTrue(result is MultiATCState.Fail)
        }
    }

    @Test
    fun `addMultipleToCart should failed when atc use case throw an exception`() =
        runCollectingUiState {
            val productListShowingState = mockk<ProductListUiState.HasData.Showing>(relaxed = true) {
                every { data.productListHeaderUiModel.shopId } returns shopId
                every { data.productList } returns listOf(product)
            }

            createSuccessGetBuyerOrderDetailDataResult()
            createFailedATCResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                viewModel.getBuyerOrderDetailData(orderId, paymentId, cart, false)
                viewModel.addMultipleToCart()

                val result = viewModel.multiAtcResult.value
                assertNotNull(result)
                assertTrue(result is MultiATCState.Fail)
            }
        }

    @Test
    fun `addMultipleToCart should failed when ui state is not equals to Showing`() =
        runCollectingUiState {
            createFailedGetBuyerOrderDetailDataResult()

            viewModel.getBuyerOrderDetailData(orderId, paymentId, cart, false)
            viewModel.addMultipleToCart()

            val result = viewModel.multiAtcResult.value
            assertNotNull(result)
            assertTrue(result is MultiATCState.Fail)
        }

    @Test
    fun `getSecondaryActionButtons should return list of ActionButton when UI state is equals to Showing`() =
        runCollectingUiState {
            val actionButton = mockk<ActionButtonsUiModel.ActionButton>(relaxed = true)
            val actionButtonsShowingState = mockk<ActionButtonsUiState.HasData.Showing>(relaxed = true) {
                every { data.secondaryActionButtons } returns listOf(actionButton)
            }

            createSuccessGetBuyerOrderDetailDataResult()
            mockActionButtonsUiStateMapper(showingState = actionButtonsShowingState) {
                viewModel.getBuyerOrderDetailData(orderId, paymentId, cart, false)

                val actionButtonList = viewModel.getSecondaryActionButtons()
                assertEquals(actionButton, actionButtonList.firstOrNull())
            }
        }

    @Test
    fun `getSecondaryActionButtons should return empty list when ui state is not equals to Showing`() =
        runCollectingUiState {
            createFailedGetBuyerOrderDetailDataResult()

            viewModel.getBuyerOrderDetailData(orderId, paymentId, cart, false)

            assertTrue(viewModel.getSecondaryActionButtons().isEmpty())
        }

    @Test
    fun `getProducts should return list of products when UI state is Showing`() =
        runCollectingUiState {
            val productListShowingState = mockk<ProductListUiState.HasData.Showing>(relaxed = true) {
                every { data.productList } returns listOf(product)
            }

            createSuccessGetBuyerOrderDetailDataResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                viewModel.getBuyerOrderDetailData(orderId, paymentId, cart, false)

                assertEquals(product, viewModel.getProducts().firstOrNull())
            }
        }

    @Test
    fun `getProducts should return empty products list when ui state is not equals to Showing`() =
        runCollectingUiState {
            createFailedGetBuyerOrderDetailDataResult()

            viewModel.getBuyerOrderDetailData(orderId, paymentId, cart, false)

            assertTrue(viewModel.getProducts().isEmpty())
        }

    @Test
    fun `getShopId should return shop id when UI state is equals to Showing`() =
        runCollectingUiState {
            val productListShowingState = mockk<ProductListUiState.HasData.Showing>(relaxed = true) {
                every { data.productListHeaderUiModel.shopId } returns shopId
            }

            createSuccessGetBuyerOrderDetailDataResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                viewModel.getBuyerOrderDetailData(orderId, paymentId, cart, false)

                assertEquals(shopId, viewModel.getShopId())
            }
        }

    @Test
    fun `getShopId should return 0 when ui state is not equals to Showing`() =
        runCollectingUiState {
            createFailedGetBuyerOrderDetailDataResult()

            viewModel.getBuyerOrderDetailData(orderId, paymentId, cart, false)

            assertEquals("0", viewModel.getShopId())
        }

    @Test
    fun `getShopName should return shop name when UI state is equals to Showing`() =
        runCollectingUiState {
            val productListShowingState = mockk<ProductListUiState.HasData.Showing>(relaxed = true) {
                every { data.productListHeaderUiModel.shopName } returns shopName
            }

            createSuccessGetBuyerOrderDetailDataResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                viewModel.getBuyerOrderDetailData(orderId, paymentId, cart, false)

                assertEquals(shopName, viewModel.getShopName())
            }
        }

    @Test
    fun `getShopName should return empty shop name when ui state is not equals to Showing`() =
        runCollectingUiState {
            createFailedGetBuyerOrderDetailDataResult()

            viewModel.getBuyerOrderDetailData(orderId, paymentId, cart, false)

            assertEquals("", viewModel.getShopName())
        }

    @Test
    fun `getShopType should return shop type when ui state is equals to Showing`() =
        runCollectingUiState {
            val productListShowingState = mockk<ProductListUiState.HasData.Showing>(relaxed = true) {
                every { data.productListHeaderUiModel.shopType } returns shopType
            }

            createSuccessGetBuyerOrderDetailDataResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                viewModel.getBuyerOrderDetailData(orderId, paymentId, cart, false)

                assertEquals(shopType, viewModel.getShopType())
            }
        }

    @Test
    fun `getShopType should return 0 shop type when ui state is not equals to Showing`() =
        runCollectingUiState {
            createFailedGetBuyerOrderDetailDataResult()

            viewModel.getBuyerOrderDetailData(orderId, paymentId, cart, false)

            assertEquals(0, viewModel.getShopType())
        }

    @Test
    fun `getCategoryId should return category id when ui state is not equals to Showing`() =
        runCollectingUiState {
            val productListShowingState = mockk<ProductListUiState.HasData.Showing>(relaxed = true) {
                every { data.productList } returns listOf(product)
            }

            createSuccessGetBuyerOrderDetailDataResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                viewModel.getBuyerOrderDetailData(orderId, paymentId, cart, false)

                val categoryId = viewModel.getCategoryId()
                assertEquals(1, categoryId.size)
                assertEquals(10, categoryId[0])
            }
        }

    @Test
    fun `getCategoryId should return unique category id when ui state is not equals to Showing`() =
        runCollectingUiState {
            val anotherProduct = ProductListUiModel.ProductUiModel(
                button = ActionButtonsUiModel.ActionButton(
                    key = "test_buy_again_button_key",
                    label = "Beli Lagi",
                    popUp = ActionButtonsUiModel.ActionButton.PopUp(
                        actionButton = emptyList(),
                        body = "",
                        title = ""
                    ),
                    variant = "ghost",
                    type = "main",
                    url = ""
                ),
                category = "Pakaian Atas",
                categoryId = "13",
                orderDetailId = "20531238",
                orderStatusId = "220",
                orderId = "166835036",
                price = 500000.0,
                priceText = "Rp500.000",
                productId = "2147819914",
                productName = "Hengpong jadul",
                productNote = "Test product note",
                productThumbnailUrl = "https://images.tokopedia.net/img/cache/100-square/VqbcmM/2021/5/28/ab64b25e-a59f-4938-a08b-c49ec140eb43.jpg",
                quantity = 1,
                totalPrice = "500000",
                totalPriceText = "Rp500.000",
                isProcessing = false
            )
            val productListShowingState = mockk<ProductListUiState.HasData.Showing>(relaxed = true) {
                every { data.productList } returns listOf(
                    product,
                    product,
                    anotherProduct
                )
            }

            createSuccessGetBuyerOrderDetailDataResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                viewModel.getBuyerOrderDetailData(orderId, paymentId, cart, false)

                val categoryId = viewModel.getCategoryId()
                assertEquals(2, categoryId.size)
                assertTrue(categoryId.contains(13))
                assertTrue(categoryId.contains(10))
            }
        }

    @Test
    fun `getCategoryId should return 0 shop type when ui state is not equals to Showing`() =
        runCollectingUiState {
            createFailedGetBuyerOrderDetailDataResult()

            viewModel.getBuyerOrderDetailData(orderId, paymentId, cart, false)

            assertTrue(viewModel.getCategoryId().isEmpty())
        }

    @Test
    fun `getCategoryId should return category id for product bundling when ui state is equals to Showing`() =
        runCollectingUiState {
            val productBundlingItem =
                ProductListUiModel.ProductBundlingUiModel(
                    bundleId = "123987456",
                    bundleName = "Bundle test",
                    bundleIconUrl = "www.icon.com",
                    totalPrice = 100.0,
                    totalPriceText = "Rp100.0",
                    bundleItemList = listOf(product)
                )
            val productListShowingState = mockk<ProductListUiState.HasData.Showing>(relaxed = true) {
                every { data.productBundlingList } returns listOf(productBundlingItem)
            }

            createSuccessGetBuyerOrderDetailDataResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                viewModel.getBuyerOrderDetailData(orderId, paymentId, cart, false)

                val categoryId = viewModel.getCategoryId()
                assertEquals(1, categoryId.size)
                assertEquals(10, categoryId[0])
            }
        }

    @Test
    fun `getUserId should return user id`() {
        assertEquals(userId, viewModel.getUserId())
    }

    @Test
    fun `getOrderStatusId should not empty when ui state is equals to Showing`() =
        runCollectingUiState {
            val orderStatusShowingState = mockk<OrderStatusUiState.HasData.Showing>(relaxed = true) {
                every { data.orderStatusHeaderUiModel.orderStatusId } returns orderStatusId
            }

            createSuccessGetBuyerOrderDetailDataResult()
            mockOrderStatusUiStateMapper(showingState = orderStatusShowingState) {
                viewModel.getBuyerOrderDetailData(orderId, paymentId, cart, false)

                assertEquals(orderStatusId, viewModel.getOrderStatusId())
            }
        }

    @Test
    fun `getOrderStatusId should 0 when ui state is not equals to Showing`() =
        runCollectingUiState {
            createFailedGetBuyerOrderDetailDataResult()

            viewModel.getBuyerOrderDetailData(orderId, paymentId, cart, false)

            assertEquals("0", viewModel.getOrderStatusId())
        }

    @Test
    fun `getOrderId should not empty when ui state is equals to Showing`() =
        runCollectingUiState {
            val orderStatusShowingState = mockk<OrderStatusUiState.HasData.Showing>(relaxed = true) {
                every { data.orderStatusHeaderUiModel.orderId } returns orderId
            }

            createSuccessGetBuyerOrderDetailDataResult()
            mockOrderStatusUiStateMapper(showingState = orderStatusShowingState) {
                viewModel.getBuyerOrderDetailData(orderId, paymentId, cart, false)

                assertEquals(orderId, viewModel.getOrderId())
            }
        }

    @Test
    fun `getOrderId should 0 when ui state is not equals to Showing`() =
        runCollectingUiState {
            createFailedGetBuyerOrderDetailDataResult()

            viewModel.getBuyerOrderDetailData(orderId, paymentId, cart, false)

            assertEquals("0", viewModel.getOrderId())
        }
}
