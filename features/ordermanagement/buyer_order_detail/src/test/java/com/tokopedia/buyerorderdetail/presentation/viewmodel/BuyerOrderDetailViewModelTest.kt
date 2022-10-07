package com.tokopedia.buyerorderdetail.presentation.viewmodel

import com.tokopedia.buyerorderdetail.common.extension.get
import com.tokopedia.buyerorderdetail.common.extension.put
import com.tokopedia.buyerorderdetail.domain.models.FinishOrderParams
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataParams
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataRequestState
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.model.MultiATCState
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.ActionButtonsUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.BuyerOrderDetailUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.OrderStatusUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.ProductListUiState
import com.tokopedia.cachemanager.CacheManager
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BuyerOrderDetailViewModelTest : BuyerOrderDetailViewModelTestFixture() {

    @Test
    fun `getP0Data should execute UseCase once with expected params`() {
        val expectedParams = GetP0DataParams(
            cart = cart,
            orderId = orderId,
            paymentId = paymentId
        )

        viewModel.getP0Data(orderId, paymentId, cart)

        coVerify(exactly = 1) { getP0DataUseCase.getP0Data(expectedParams) }
    }

    @Test
    fun `UI state should equals to Showing when getP0DataRequestState is Success`() =
        runCollectingUiState { uiStates ->
            createSuccessGetP0DataResult()

            viewModel.getP0Data(orderId = orderId, paymentId = paymentId, cart = cart)

            assertTrue(uiStates[0] is BuyerOrderDetailUiState.FullscreenLoading)
            assertTrue(uiStates[1] is BuyerOrderDetailUiState.Showing)
        }

    @Test
    fun `UI state should equals to Error when getP0DataRequestState is Error`() =
        runCollectingUiState { uiStates ->
            createFailedGetP0DataResult()

            viewModel.getP0Data(orderId, paymentId, cart)

            assertTrue(uiStates[0] is BuyerOrderDetailUiState.FullscreenLoading)
            assertTrue(uiStates[1] is BuyerOrderDetailUiState.Error)
        }

    @Test
    fun `UI state should equals to PullRefreshLoading when reloading P0 data`() =
        runCollectingUiState { uiStates ->
            createSuccessGetP0DataResult()

            viewModel.getP0Data(orderId = orderId, paymentId = paymentId, cart = cart)
            // reload
            viewModel.getP0Data(orderId = orderId, paymentId = paymentId, cart = cart)

            assertTrue(uiStates[0] is BuyerOrderDetailUiState.FullscreenLoading)
            assertTrue(uiStates[1] is BuyerOrderDetailUiState.Showing)
            for (i in 2 until uiStates.size.dec()) {
                assertTrue(uiStates[i] is BuyerOrderDetailUiState.PullRefreshLoading)
            }
            assertTrue(uiStates.last() is BuyerOrderDetailUiState.Showing)
        }

    @Test
    fun `finishOrder should execute UseCase with expected params`() = runCollectingUiState {
        val expectedParams = FinishOrderParams(
            orderId = orderId,
            userId = userId,
            action = "event_dialog_deliver_finish"
        )
        val orderStatusShowingState = mockk<OrderStatusUiState.Showing>(relaxed = true) {
            every { data.orderStatusHeaderUiModel.orderStatusId } returns "540"
            every { data.orderStatusHeaderUiModel.orderId } returns orderId
        }

        createSuccessGetP0DataResult()
        createSuccessFinishOrderResult()
        mockOrderStatusUiStateMapper(showingState = orderStatusShowingState) {
            viewModel.getP0Data(orderId, paymentId, cart)
            viewModel.finishOrder()

            coVerify {
                finishOrderUseCase.execute(expectedParams)
            }
        }
    }

    @Test
    fun `finishOrder should success when set order as delivered`() = runCollectingUiState {
        val orderStatusShowingState = mockk<OrderStatusUiState.Showing>(relaxed = true) {
            every { data.orderStatusHeaderUiModel.orderStatusId } returns "540"
            every { data.orderStatusHeaderUiModel.orderId } returns orderId
        }

        createSuccessGetP0DataResult()
        mockOrderStatusUiStateMapper(showingState = orderStatusShowingState) {
            viewModel.getP0Data(orderId, paymentId, cart)
            viewModel.finishOrder()

            assertTrue(viewModel.finishOrderResult.value is Success)
        }
    }

    @Test
    fun `finishOrder should success when set order as arrived`() = runCollectingUiState {
        val orderStatusShowingState = mockk<OrderStatusUiState.Showing>(relaxed = true) {
            every { data.orderStatusHeaderUiModel.orderStatusId } returns "600"
            every { data.orderStatusHeaderUiModel.orderId } returns orderId
        }

        createSuccessGetP0DataResult()
        createSuccessFinishOrderResult()
        mockOrderStatusUiStateMapper(showingState = orderStatusShowingState) {
            viewModel.getP0Data(orderId, paymentId, cart)
            viewModel.finishOrder()

            assertTrue(viewModel.finishOrderResult.value is Success)
        }
    }

    @Test
    fun `finishOrder should failed when orderId is invalid and use case throw an exception`() =
        runCollectingUiState {
            val expectedErrorMessage = "Tidak dapat menyelesaikan pesanan, silahkan muat ulang dan coba lagi!"
            val expectedException = MessageErrorException(expectedErrorMessage)
            val orderStatusShowingState = mockk<OrderStatusUiState.Showing>(relaxed = true) {
                every { data.orderStatusHeaderUiModel.orderStatusId } returns "600"
                every { data.orderStatusHeaderUiModel.orderId } returns orderId
            }

            createSuccessGetP0DataResult()
            createFailedFinishOrderResult(expectedException)
            mockOrderStatusUiStateMapper(showingState = orderStatusShowingState) {
                viewModel.getP0Data(orderId, paymentId, cart)
                viewModel.finishOrder()

                val result = viewModel.finishOrderResult.value as Fail
                assertTrue(result.throwable is MessageErrorException)
                assertEquals(expectedErrorMessage, result.throwable.message)
            }
        }

    @Test
    fun `addSingleToCart should execute UseCase with expected params`() = runCollectingUiState {
        val productListShowingState = mockk<ProductListUiState.Showing>(relaxed = true) {
            every { data.productListHeaderUiModel.shopId } returns shopId
        }

        createSuccessGetP0DataResult()
        createSuccessATCResult()
        mockProductListUiStateMapper(showingState = productListShowingState) {
            viewModel.getP0Data(orderId = orderId, paymentId = paymentId, cart = cart)
            viewModel.addSingleToCart(product)

            coVerify(exactly = 1) { atcUseCase.execute(userId, "", atcExpectedParams) }
        }
    }

    @Test
    fun `addSingleToCart should success when atc use case return expected data`() =
        runCollectingUiState {
            val productListShowingState = mockk<ProductListUiState.Showing>(relaxed = true) {
                every { data.productListHeaderUiModel.shopId } returns shopId
            }

            createSuccessGetP0DataResult()
            createSuccessATCResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                viewModel.getP0Data(orderId = orderId, paymentId = paymentId, cart = cart)
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
            val productListShowingState = mockk<ProductListUiState.Showing>(relaxed = true) {
                every { data.productListHeaderUiModel.shopId } returns shopId
            }

            createSuccessGetP0DataResult()
            createFailedATCResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                viewModel.getP0Data(orderId = orderId, paymentId = paymentId, cart = cart)
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
            val productListShowingState = mockk<ProductListUiState.Showing>(relaxed = true) {
                every { data.productListHeaderUiModel.shopId } returns shopId
                every { data.productList } returns listOf(product)
            }

            createSuccessGetP0DataResult()
            createSuccessATCResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                viewModel.getP0Data(orderId = orderId, paymentId = paymentId, cart = cart)
                viewModel.addMultipleToCart()

                coVerify(exactly = 1) { atcUseCase.execute(userId, "", atcExpectedParams) }
            }
        }

    @Test
    fun `addMultipleToCart should not execute UseCase when UI state is not equals to Showing`() =
        runCollectingUiState {
            createFailedGetP0DataResult()

            viewModel.getP0Data(orderId = orderId, paymentId = paymentId, cart = cart)
            viewModel.addMultipleToCart()

            coVerify(inverse = true) { atcUseCase.execute(any(), any(), any()) }
        }

    @Test
    fun `addMultipleToCart should success when atc use case return expected data`() =
        runCollectingUiState {
            val productListShowingState = mockk<ProductListUiState.Showing>(relaxed = true) {
                every { data.productListHeaderUiModel.shopId } returns shopId
                every { data.productList } returns listOf(product)
            }

            createSuccessGetP0DataResult()
            createSuccessATCResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                viewModel.getP0Data(orderId, paymentId, cart)
                viewModel.addMultipleToCart()

                val result = viewModel.multiAtcResult.value
                assertNotNull(result)
                assertTrue(result is MultiATCState.Success)
            }
        }

    @Test
    fun `addMultipleToCart should failed when atc use case return fail`() = runCollectingUiState {
        val productListShowingState = mockk<ProductListUiState.Showing>(relaxed = true) {
            every { data.productListHeaderUiModel.shopId } returns shopId
            every { data.productList } returns listOf(product)
        }

        createSuccessGetP0DataResult()
        createSuccessATCResult(Fail(mockk(relaxed = true)))
        mockProductListUiStateMapper(showingState = productListShowingState) {
            viewModel.getP0Data(orderId, paymentId, cart)
            viewModel.addMultipleToCart()

            val result = viewModel.multiAtcResult.value
            assertNotNull(result)
            assertTrue(result is MultiATCState.Fail)
        }
    }

    @Test
    fun `addMultipleToCart should failed when atc use case throw an exception`() =
        runCollectingUiState {
            val productListShowingState = mockk<ProductListUiState.Showing>(relaxed = true) {
                every { data.productListHeaderUiModel.shopId } returns shopId
                every { data.productList } returns listOf(product)
            }

            createSuccessGetP0DataResult()
            createFailedATCResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                viewModel.getP0Data(orderId, paymentId, cart)
                viewModel.addMultipleToCart()

                val result = viewModel.multiAtcResult.value
                assertNotNull(result)
                assertTrue(result is MultiATCState.Fail)
            }
        }

    @Test
    fun `addMultipleToCart should failed when ui state is not equals to Showing`() =
        runCollectingUiState {
            createFailedGetP0DataResult()

            viewModel.getP0Data(orderId, paymentId, cart)
            viewModel.addMultipleToCart()

            val result = viewModel.multiAtcResult.value
            assertNotNull(result)
            assertTrue(result is MultiATCState.Fail)
        }

    @Test
    fun `getSecondaryActionButtons should return list of ActionButton when UI state is equals to Showing`() =
        runCollectingUiState {
            val actionButton = mockk<ActionButtonsUiModel.ActionButton>(relaxed = true)
            val actionButtonsShowingState = mockk<ActionButtonsUiState.Showing>(relaxed = true) {
                every { data.secondaryActionButtons } returns listOf(actionButton)
            }

            createSuccessGetP0DataResult()
            mockActionButtonsUiStateMapper(showingState = actionButtonsShowingState) {
                viewModel.getP0Data(orderId, paymentId, cart)

                val actionButtonList = viewModel.getSecondaryActionButtons()
                assertEquals(actionButton, actionButtonList.firstOrNull())
            }
        }

    @Test
    fun `getSecondaryActionButtons should return empty list when ui state is not equals to Showing`() =
        runCollectingUiState {
            createFailedGetP0DataResult()

            viewModel.getP0Data(orderId, paymentId, cart)

            assertTrue(viewModel.getSecondaryActionButtons().isEmpty())
        }

    @Test
    fun `getProducts should return list of products when UI state is Showing`() =
        runCollectingUiState {
            val productListShowingState = mockk<ProductListUiState.Showing>(relaxed = true) {
                every { data.productList } returns listOf(product)
            }

            createSuccessGetP0DataResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                viewModel.getP0Data(orderId, paymentId, cart)

                assertEquals(product, viewModel.getProducts().firstOrNull())
            }
        }

    @Test
    fun `getProducts should return empty products list when ui state is not equals to Showing`() =
        runCollectingUiState {
            createFailedGetP0DataResult()

            viewModel.getP0Data(orderId, paymentId, cart)

            assertTrue(viewModel.getProducts().isEmpty())
        }

    @Test
    fun `getShopId should return shop id when UI state is equals to Showing`() =
        runCollectingUiState {
            val productListShowingState = mockk<ProductListUiState.Showing>(relaxed = true) {
                every { data.productListHeaderUiModel.shopId } returns shopId
            }

            createSuccessGetP0DataResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                viewModel.getP0Data(orderId, paymentId, cart)

                assertEquals(shopId, viewModel.getShopId())
            }
        }

    @Test
    fun `getShopId should return 0 when ui state is not equals to Showing`() =
        runCollectingUiState {
            createFailedGetP0DataResult()

            viewModel.getP0Data(orderId, paymentId, cart)

            assertEquals("0", viewModel.getShopId())
        }

    @Test
    fun `getShopName should return shop name when UI state is equals to Showing`() =
        runCollectingUiState {
            val productListShowingState = mockk<ProductListUiState.Showing>(relaxed = true) {
                every { data.productListHeaderUiModel.shopName } returns shopName
            }

            createSuccessGetP0DataResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                viewModel.getP0Data(orderId, paymentId, cart)

                assertEquals(shopName, viewModel.getShopName())
            }
        }

    @Test
    fun `getShopName should return empty shop name when ui state is not equals to Showing`() =
        runCollectingUiState {
            createFailedGetP0DataResult()

            viewModel.getP0Data(orderId, paymentId, cart)

            assertEquals("", viewModel.getShopName())
        }

    @Test
    fun `getShopType should return shop type when ui state is equals to Showing`() =
        runCollectingUiState {
            val productListShowingState = mockk<ProductListUiState.Showing>(relaxed = true) {
                every { data.productListHeaderUiModel.shopType } returns shopType
            }

            createSuccessGetP0DataResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                viewModel.getP0Data(orderId, paymentId, cart)

                assertEquals(shopType, viewModel.getShopType())
            }
        }

    @Test
    fun `getShopType should return 0 shop type when ui state is not equals to Showing`() =
        runCollectingUiState {
            createFailedGetP0DataResult()

            viewModel.getP0Data(orderId, paymentId, cart)

            assertEquals(0, viewModel.getShopType())
        }

    @Test
    fun `getCategoryId should return category id when ui state is not equals to Showing`() =
        runCollectingUiState {
            val productListShowingState = mockk<ProductListUiState.Showing>(relaxed = true) {
                every { data.productList } returns listOf(product)
            }

            createSuccessGetP0DataResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                viewModel.getP0Data(orderId, paymentId, cart)

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
                productThumbnailUrl = "https://ecs7.tokopedia.net/img/cache/100-square/VqbcmM/2021/5/28/ab64b25e-a59f-4938-a08b-c49ec140eb43.jpg",
                quantity = 1,
                totalPrice = "500000",
                totalPriceText = "Rp500.000",
                isProcessing = false
            )
            val productListShowingState = mockk<ProductListUiState.Showing>(relaxed = true) {
                every { data.productList } returns listOf(
                    product,
                    product,
                    anotherProduct
                )
            }

            createSuccessGetP0DataResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                viewModel.getP0Data(orderId, paymentId, cart)

                val categoryId = viewModel.getCategoryId()
                assertEquals(2, categoryId.size)
                assertTrue(categoryId.contains(13))
                assertTrue(categoryId.contains(10))
            }
        }

    @Test
    fun `getCategoryId should return 0 shop type when ui state is not equals to Showing`() =
        runCollectingUiState {
            createFailedGetP0DataResult()

            viewModel.getP0Data(orderId, paymentId, cart)

            assertTrue(viewModel.getCategoryId().isEmpty())
        }

    @Test
    fun `getCategoryId should return category id for product bundling when ui state is equals to Showing`() =
        runCollectingUiState {
            val productBundlingItem =
                ProductListUiModel.ProductBundlingUiModel(
                    bundleName = "Bundle test",
                    bundleIconUrl = "www.icon.com",
                    totalPrice = 100.0,
                    totalPriceText = "Rp100.0",
                    bundleItemList = listOf(product)
                )
            val productListShowingState = mockk<ProductListUiState.Showing>(relaxed = true) {
                every { data.productBundlingList } returns listOf(productBundlingItem)
            }

            createSuccessGetP0DataResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                viewModel.getP0Data(orderId, paymentId, cart)

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
            val orderStatusShowingState = mockk<OrderStatusUiState.Showing>(relaxed = true) {
                every { data.orderStatusHeaderUiModel.orderStatusId } returns orderStatusId
            }

            createSuccessGetP0DataResult()
            mockOrderStatusUiStateMapper(showingState = orderStatusShowingState) {
                viewModel.getP0Data(orderId, paymentId, cart)

                assertEquals(orderStatusId, viewModel.getOrderStatusId())
            }
        }

    @Test
    fun `getOrderStatusId should 0 when ui state is not equals to Showing`() =
        runCollectingUiState {
            createFailedGetP0DataResult()

            viewModel.getP0Data(orderId, paymentId, cart)

            assertEquals("0", viewModel.getOrderStatusId())
        }

    @Test
    fun `getOrderId should not empty when ui state is equals to Showing`() =
        runCollectingUiState {
            val orderStatusShowingState = mockk<OrderStatusUiState.Showing>(relaxed = true) {
                every { data.orderStatusHeaderUiModel.orderId } returns orderId
            }

            createSuccessGetP0DataResult()
            mockOrderStatusUiStateMapper(showingState = orderStatusShowingState) {
                viewModel.getP0Data(orderId, paymentId, cart)

                assertEquals(orderId, viewModel.getOrderId())
            }
        }

    @Test
    fun `getOrderId should 0 when ui state is not equals to Showing`() =
        runCollectingUiState {
            createFailedGetP0DataResult()

            viewModel.getP0Data(orderId, paymentId, cart)

            assertEquals("0", viewModel.getOrderId())
        }

    @Test
    fun `saveUiState should save order detail and resolution result`() {
        mockkStatic("com.tokopedia.buyerorderdetail.common.extension.CacheManagerExtKt") {
            val cacheManager = mockk<CacheManager>(relaxed = true)

            viewModel.saveUiState(cacheManager)

            verify {
                cacheManager.put(
                    customId = "SAVED_GET_P0_DATA_REQUEST_STATE",
                    objectToPut = any<GetP0DataRequestState>(),
                    gson = gson
                )
            }
        }
    }

    @Test
    fun `restoreUiState should restore get P0 data request state`() = runCollectingUiState {
        mockkStatic("com.tokopedia.buyerorderdetail.common.extension.CacheManagerExtKt") {
            val savedGetP0DataRequestState = mockk<GetP0DataRequestState.Error>(relaxed = true)
            val cacheManager = mockk<CacheManager>(relaxed = true)

            every {
                cacheManager.get<GetP0DataRequestState>(
                    customId = "SAVED_GET_P0_DATA_REQUEST_STATE",
                    type = GetP0DataRequestState::class.java,
                    gson = gson
                )
            } returns savedGetP0DataRequestState

            viewModel.restoreUiState(cacheManager)

            assertTrue(viewModel.buyerOrderDetailUiState.value is BuyerOrderDetailUiState.Error)
        }
    }

    @Test
    fun `restoreUiState should return true when cache manager contain GetP0DataRequestState#Success`() =
        runCollectingUiState {
            mockkStatic("com.tokopedia.buyerorderdetail.common.extension.CacheManagerExtKt") {
                val savedGetP0DataRequestState = mockk<GetP0DataRequestState.Success>(relaxed = true)
                val cacheManager = mockk<CacheManager>(relaxed = true)

                every {
                    cacheManager.get<GetP0DataRequestState>(
                        customId = "SAVED_GET_P0_DATA_REQUEST_STATE",
                        type = GetP0DataRequestState::class.java,
                        gson = gson
                    )
                } returns savedGetP0DataRequestState

                assertTrue(viewModel.restoreUiState(cacheManager))
            }
        }

    @Test
    fun `restoreUiState should return true when cache manager contain GetP0DataRequestState#Error`() =
        runCollectingUiState {
            mockkStatic("com.tokopedia.buyerorderdetail.common.extension.CacheManagerExtKt") {
                val savedGetP0DataRequestState = mockk<GetP0DataRequestState.Error>(relaxed = true)
                val cacheManager = mockk<CacheManager>(relaxed = true)

                every {
                    cacheManager.get<GetP0DataRequestState>(
                        customId = "SAVED_GET_P0_DATA_REQUEST_STATE",
                        type = GetP0DataRequestState::class.java,
                        gson = gson
                    )
                } returns savedGetP0DataRequestState

                assertTrue(viewModel.restoreUiState(cacheManager))
            }
        }

    @Test
    fun `restoreUiState should return false when cache manager contain GetP0DataRequestState#Idle`() =
        runCollectingUiState {
            mockkStatic("com.tokopedia.buyerorderdetail.common.extension.CacheManagerExtKt") {
                val savedGetP0DataRequestState = mockk<GetP0DataRequestState.Idle>(relaxed = true)
                val cacheManager = mockk<CacheManager>(relaxed = true)

                every {
                    cacheManager.get<GetP0DataRequestState>(
                        customId = "SAVED_GET_P0_DATA_REQUEST_STATE",
                        type = GetP0DataRequestState::class.java,
                        gson = gson
                    )
                } returns savedGetP0DataRequestState

                assertFalse(viewModel.restoreUiState(cacheManager))
            }
        }

    @Test
    fun `restoreUiState should return false when cache manager contain GetP0DataRequestState#Requesting`() =
        runCollectingUiState {
            mockkStatic("com.tokopedia.buyerorderdetail.common.extension.CacheManagerExtKt") {
                val savedGetP0DataRequestState = mockk<GetP0DataRequestState.Requesting>(relaxed = true)
                val cacheManager = mockk<CacheManager>(relaxed = true)

                every {
                    cacheManager.get<GetP0DataRequestState>(
                        customId = "SAVED_GET_P0_DATA_REQUEST_STATE",
                        type = GetP0DataRequestState::class.java,
                        gson = gson
                    )
                } returns savedGetP0DataRequestState

                assertFalse(viewModel.restoreUiState(cacheManager))
            }
        }

    @Test
    fun `restoreUiState should return false when cache manager does not contain saved P0 request state`() =
        runCollectingUiState {
            mockkStatic("com.tokopedia.buyerorderdetail.common.extension.CacheManagerExtKt") {
                val cacheManager = mockk<CacheManager>(relaxed = true)

                every {
                    cacheManager.get<GetP0DataRequestState>(
                        customId = "SAVED_GET_P0_DATA_REQUEST_STATE",
                        type = GetP0DataRequestState::class.java,
                        gson = gson
                    )
                } returns null

                assertFalse(viewModel.restoreUiState(cacheManager))
            }
        }
}
