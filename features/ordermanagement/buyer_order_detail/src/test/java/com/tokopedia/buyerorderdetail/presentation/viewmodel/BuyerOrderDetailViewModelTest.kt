package com.tokopedia.buyerorderdetail.presentation.viewmodel

import com.tokopedia.buyerorderdetail.domain.models.FinishOrderParams
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataParams
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.presentation.mapper.EpharmacyInfoUiStateMapper
import com.tokopedia.buyerorderdetail.presentation.mapper.ScpRewardsMedalTouchPointWidgetMapper
import com.tokopedia.buyerorderdetail.presentation.model.MultiATCState
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.ActionButtonsUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.BuyerOrderDetailUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.OrderStatusUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.ProductListUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.ScpRewardsMedalTouchPointWidgetUiState
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.order_management_common.presentation.uimodel.ActionButtonsUiModel
import com.tokopedia.order_management_common.presentation.uimodel.ProductBmgmSectionUiModel
import com.tokopedia.scp_rewards_touchpoints.touchpoints.data.response.ScpRewardsMedalTouchPointResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
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
            createSuccessGetBuyerOrderDetailDataResult()

            getBuyerOrderDetailData()

            coVerify(exactly = 1) { getBuyerOrderDetailDataUseCase(expectedParams) }
        }
    }

    @Test
    fun `UI state should equals to Showing when getP0DataRequestState is Success`() =
        runCollectingUiState { uiStates ->
            createSuccessGetBuyerOrderDetailDataResult()

            assertTrue(uiStates.last() is BuyerOrderDetailUiState.FullscreenLoading)

            getBuyerOrderDetailData()

            assertTrue(uiStates.last() is BuyerOrderDetailUiState.HasData.Showing)
        }

    @Test
    fun `UI state should equals to Error when getP0DataRequestState is Error`() =
        runCollectingUiState { uiStates ->
            createFailedGetBuyerOrderDetailDataResult()

            assertTrue(uiStates.last() is BuyerOrderDetailUiState.FullscreenLoading)

            getBuyerOrderDetailData()

            assertTrue(uiStates.last() is BuyerOrderDetailUiState.Error)
        }

    @Test
    fun `UI state should equals to PullRefreshLoading when reloading P0 data`() =
        runCollectingUiState { uiStates ->
            var uiStateBeforeSuccessReloading: BuyerOrderDetailUiState? = null
            createSuccessGetBuyerOrderDetailDataResult()

            // assert first initial bom page opened
            assertTrue(uiStates.last() is BuyerOrderDetailUiState.FullscreenLoading)

            getBuyerOrderDetailData()

            // assert data showing after initial first data is completed
            assertTrue(uiStates.last() is BuyerOrderDetailUiState.HasData.Showing)

            // reload
            createSuccessGetBuyerOrderDetailDataResult {
                advanceUntilIdle()
                uiStateBeforeSuccessReloading = uiStates.last()
            }

            getBuyerOrderDetailData()

            // assert data is pull refresh state after swipe refresh and data not complete yet
            assertTrue(uiStateBeforeSuccessReloading is BuyerOrderDetailUiState.HasData.PullRefreshLoading)
            // assert last state should showing after success pull refresh
            assertTrue(uiStates.last() is BuyerOrderDetailUiState.HasData.Showing)
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
            getBuyerOrderDetailData()
            viewModel.finishOrder()
            advanceUntilIdle()

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
        createSuccessFinishOrderResult()
        mockOrderStatusUiStateMapper(showingState = orderStatusShowingState) {
            getBuyerOrderDetailData()
            viewModel.finishOrder()
            advanceUntilIdle()

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
            getBuyerOrderDetailData()
            viewModel.finishOrder()
            advanceUntilIdle()

            assertTrue(viewModel.finishOrderResult.value is Success)
        }
    }

    @Test
    fun `finishOrder should failed when orderId is invalid and use case throw an exception`() =
        runCollectingUiState {
            val expectedErrorMessage =
                "Tidak dapat menyelesaikan pesanan, silahkan muat ulang dan coba lagi!"
            val expectedException = MessageErrorException(expectedErrorMessage)
            val orderStatusShowingState =
                mockk<OrderStatusUiState.HasData.Showing>(relaxed = true) {
                    every { data.orderStatusHeaderUiModel.orderStatusId } returns "600"
                    every { data.orderStatusHeaderUiModel.orderId } returns orderId
                }

            createSuccessGetBuyerOrderDetailDataResult()
            createFailedFinishOrderResult(expectedException)
            mockOrderStatusUiStateMapper(showingState = orderStatusShowingState) {
                getBuyerOrderDetailData()
                viewModel.finishOrder()
                advanceUntilIdle()

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
            getBuyerOrderDetailData()
            viewModel.addSingleToCart(product)

            coVerify(exactly = 1) { atcUseCase.execute(userId, "", atcExpectedParams) }
        }
    }

    @Test
    fun `addSingleToCart should success when atc use case return expected data`() =
        runCollectingUiState {
            val productListShowingState =
                mockk<ProductListUiState.HasData.Showing>(relaxed = true) {
                    every { data.productListHeaderUiModel.shopId } returns shopId
                }

            createSuccessGetBuyerOrderDetailDataResult()
            createSuccessATCResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                getBuyerOrderDetailData()
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
            val productListShowingState =
                mockk<ProductListUiState.HasData.Showing>(relaxed = true) {
                    every { data.productListHeaderUiModel.shopId } returns shopId
                }

            createSuccessGetBuyerOrderDetailDataResult()
            createFailedATCResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                getBuyerOrderDetailData()
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
            val productListShowingState =
                mockk<ProductListUiState.HasData.Showing>(relaxed = true) {
                    every { data.productListHeaderUiModel.shopId } returns shopId
                    every { data.getAllProduct() } returns listOf(product)
                }
            val mockGetBuyerOrderDetailResult =
                mockk<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail>(relaxed = true) {
                    every { details?.nonBundles } returns listOf(
                        GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details.NonBundle(
                            productId = product.productId,
                            productName = product.productName,
                            price = product.price,
                            quantity = product.quantity,
                            notes = product.productNote
                        )
                    )
                }

            createSuccessGetBuyerOrderDetailDataResult(
                getBuyerOrderDetailResult = mockGetBuyerOrderDetailResult
            )
            createSuccessATCResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                getBuyerOrderDetailData()
                viewModel.addMultipleToCart()

                coVerify(exactly = 1) { atcUseCase.execute(userId, "", atcExpectedParams) }
            }
        }

    @Test
    fun `addMultipleToCart should success when atc use case return expected data`() =
        runCollectingUiState {
            val productListShowingState =
                mockk<ProductListUiState.HasData.Showing>(relaxed = true) {
                    every { data.productListHeaderUiModel.shopId } returns shopId
                    every { data.productList } returns listOf(product)
                }
            val mockGetBuyerOrderDetailResult =
                mockk<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail>(relaxed = true) {
                    every { details?.nonBundles } returns listOf(
                        GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details.NonBundle(
                            productId = product.productId,
                            productName = product.productName,
                            price = product.price,
                            quantity = product.quantity,
                            notes = product.productNote
                        )
                    )
                }

            createSuccessGetBuyerOrderDetailDataResult(
                getBuyerOrderDetailResult = mockGetBuyerOrderDetailResult
            )
            createSuccessATCResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                getBuyerOrderDetailData()
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
        val mockGetBuyerOrderDetailResult =
            mockk<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail>(relaxed = true) {
                every { details?.nonBundles } returns listOf(
                    GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details.NonBundle(
                        productId = product.productId,
                        productName = product.productName,
                        price = product.price,
                        quantity = product.quantity,
                        notes = product.productNote
                    )
                )
            }

        createSuccessGetBuyerOrderDetailDataResult(
            getBuyerOrderDetailResult = mockGetBuyerOrderDetailResult
        )
        createSuccessATCResult(Fail(mockk(relaxed = true)))
        mockProductListUiStateMapper(showingState = productListShowingState) {
            getBuyerOrderDetailData()
            viewModel.addMultipleToCart()

            val result = viewModel.multiAtcResult.value
            assertNotNull(result)
            assertTrue(result is MultiATCState.Fail)
        }
    }

    @Test
    fun `addMultipleToCart should failed when atc use case throw an exception`() =
        runCollectingUiState {
            val productListShowingState =
                mockk<ProductListUiState.HasData.Showing>(relaxed = true) {
                    every { data.productListHeaderUiModel.shopId } returns shopId
                    every { data.productList } returns listOf(product)
                }
            val mockGetBuyerOrderDetailResult =
                mockk<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail>(relaxed = true) {
                    every { details?.nonBundles } returns listOf(
                        GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details.NonBundle(
                            productId = product.productId,
                            productName = product.productName,
                            price = product.price,
                            quantity = product.quantity,
                            notes = product.productNote
                        )
                    )
                }

            createSuccessGetBuyerOrderDetailDataResult(
                getBuyerOrderDetailResult = mockGetBuyerOrderDetailResult
            )
            createFailedATCResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                getBuyerOrderDetailData()
                viewModel.addMultipleToCart()

                val result = viewModel.multiAtcResult.value
                assertNotNull(result)
                assertTrue(result is MultiATCState.Fail)
            }
        }

    @Test
    fun `addMultipleToCart should failed when params is empty`() =
        runCollectingUiState {
            createFailedGetBuyerOrderDetailDataResult()

            getBuyerOrderDetailData()
            viewModel.addMultipleToCart()

            val result = viewModel.multiAtcResult.value
            assertNotNull(result)
            assertTrue(result is MultiATCState.Fail)
        }

    @Test
    fun `collapseProductList should collapse product list`() =
        runCollectingUiState {
            val productListShowingState =
                mockk<ProductListUiState.HasData.Showing>(relaxed = true) {
                    every { data.productList } returns listOf(
                        mockk(relaxed = true),
                        mockk(relaxed = true)
                    )
                }
            createSuccessGetBuyerOrderDetailDataResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                getBuyerOrderDetailData()
                viewModel.expandProductList()
                advanceTimeBy(1000L)
                assertTrue(isProductListExpanded())
                viewModel.collapseProductList()
                advanceTimeBy(1000L)
                assertTrue(isProductListCollapsed())
            }
        }

    @Test
    fun `expandProductList should expand product list`() =
        runCollectingUiState {
            val productListShowingState =
                mockk<ProductListUiState.HasData.Showing>(relaxed = true) {
                    every { data.productList } returns listOf(
                        mockk(relaxed = true),
                        mockk(relaxed = true)
                    )
                }
            createSuccessGetBuyerOrderDetailDataResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                getBuyerOrderDetailData()
                viewModel.expandProductList()
                advanceTimeBy(1000L)
                assertTrue(isProductListExpanded())
            }
        }

    @Test
    fun `getSecondaryActionButtons should return list of ActionButton when UI state is equals to Showing`() =
        runCollectingUiState {
            val actionButton = mockk<ActionButtonsUiModel.ActionButton>(relaxed = true)
            val actionButtonsShowingState =
                mockk<ActionButtonsUiState.HasData.Showing>(relaxed = true) {
                    every { data.secondaryActionButtons } returns listOf(actionButton)
                }

            createSuccessGetBuyerOrderDetailDataResult()
            mockActionButtonsUiStateMapper(showingState = actionButtonsShowingState) {
                getBuyerOrderDetailData()

                val actionButtonList = viewModel.getSecondaryActionButtons()
                assertEquals(actionButton, actionButtonList.firstOrNull())
            }
        }

    @Test
    fun `getSecondaryActionButtons should return empty list when ui state is not equals to Showing`() =
        runCollectingUiState {
            createFailedGetBuyerOrderDetailDataResult()

            getBuyerOrderDetailData()

            assertTrue(viewModel.getSecondaryActionButtons().isEmpty())
        }

    @Test
    fun `EpharmacyInfoUiState should catch error when EpharmacyInfoUiStateMapper throwing crash`() =
        runCollectingUiState {
            createSuccessGetBuyerOrderDetailDataResult()

            every { EpharmacyInfoUiStateMapper.map(any(), any()) } throws Throwable("Error")

            getBuyerOrderDetailData()

            // if error happen in ephar mapper, return empty data so the section not showing
            assertTrue(it.last() is BuyerOrderDetailUiState.HasData.Showing)
            assertTrue(
                (it.last() as BuyerOrderDetailUiState.HasData.Showing)
                    .epharmacyInfoUiState.data
                    .isEmptyData()
            )
        }

    @Test
    fun `getProducts should return list of products when UI state is Showing`() =
        runCollectingUiState {
            val productListShowingState =
                mockk<ProductListUiState.HasData.Showing>(relaxed = true) {
                    every { data.productList } returns listOf(product)
                }

            createSuccessGetBuyerOrderDetailDataResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                getBuyerOrderDetailData()

                assertEquals(product, viewModel.getProducts().firstOrNull())
            }
        }

    @Test
    fun `given bmgmResponse, when getProducts then should return list of products when UI state is Showing`() =
        runCollectingUiState { buyerDetailUiState ->

            val bmgmDetailsResponse =
                ProductBmgmSectionUiModel(
                    bmgmId = "1:3:0",
                    bmgmName = "offers - Beli2DiskonDiskon30%",
                    totalPrice = 400000.00,
                    totalPriceText = "Rp400.000",
                    totalPriceReductionInfoText = "Rp100.000",
                    bmgmIconUrl = "https://images.tokopedia.net/img/cache/100-square/VqbcmM/2023/2/8/60274de2-2dbc-48b4-b0cb-4f626792df2A.jpg",
                    bmgmItemList = listOf(
                        ProductBmgmSectionUiModel.ProductUiModel(
                            orderId = "556574",
                            orderDetailId = "2150865420",
                            productName = "Power Bank Original - Pink",
                            thumbnailUrl = "https://images.tokopedia.net/img/cache/100-square/VqbcmM/2023/2/8/60274de2-2dbc-48b4-b0cb-4f626792df2b.jpg",
                            price = 75000.00,
                            productPriceText = "Rp 75.000",
                            quantity = 2,
                            productNote = "ukurannya 43 ya"
                        ),
                        ProductBmgmSectionUiModel.ProductUiModel(
                            orderId = "556575",
                            orderDetailId = "2150865421",
                            productName = "Power Bank Original - Blue",
                            thumbnailUrl = "https://images.tokopedia.net/img/cache/100-square/VqbcmM/2023/2/8/60274de2-2dbc-48b4-b0cb-4f626792df2b.jpg",
                            price = 85000.00,
                            productPriceText = "Rp 85.000",
                            quantity = 2,
                            productNote = "ukurannya 44 ya"
                        )
                    )
                )

            val productListShowingState =
                mockk<ProductListUiState.HasData.Showing>(relaxed = true) {
                    every { data.productBmgmList } returns listOf(bmgmDetailsResponse)
                }

            createSuccessGetBuyerOrderDetailDataResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                getBuyerOrderDetailData()

                val actualBmgmUiModel = buyerDetailUiState.filterIsInstance(BuyerOrderDetailUiState.HasData.Showing::class.java)
                    .last().productListUiState.data.productBmgmList.first()
                assertTrue(bmgmDetailsResponse.totalPrice == actualBmgmUiModel.totalPrice)
                assertEquals(bmgmDetailsResponse.bmgmId, actualBmgmUiModel.bmgmId)
                assertEquals(bmgmDetailsResponse.bmgmName, actualBmgmUiModel.bmgmName)
                assertEquals(bmgmDetailsResponse.bmgmIconUrl, actualBmgmUiModel.bmgmIconUrl)
                assertEquals(bmgmDetailsResponse.totalPriceReductionInfoText, actualBmgmUiModel.totalPriceReductionInfoText)
                bmgmDetailsResponse.bmgmItemList.forEachIndexed { index, productUiModel ->
                    assertEquals(productUiModel.orderId, actualBmgmUiModel.bmgmItemList[index].orderId)
                    assertEquals(productUiModel.orderDetailId, actualBmgmUiModel.bmgmItemList[index].orderDetailId)
                    assertEquals(productUiModel.productName, actualBmgmUiModel.bmgmItemList[index].productName)
                    assertEquals(productUiModel.thumbnailUrl, actualBmgmUiModel.bmgmItemList[index].thumbnailUrl)
                    assertTrue(productUiModel.price == actualBmgmUiModel.bmgmItemList[index].price)
                    assertEquals(productUiModel.productPriceText, actualBmgmUiModel.bmgmItemList[index].productPriceText)
                    assertEquals(productUiModel.quantity, actualBmgmUiModel.bmgmItemList[index].quantity)
                    assertEquals(productUiModel.productNote, actualBmgmUiModel.bmgmItemList[index].productNote)
                }
            }
        }

    @Test
    fun `given bmgmList empty, when getProducts then should return empty list of products when UI state is Showing`() =
        runCollectingUiState { buyerDetailUiState ->

            val productListShowingState =
                mockk<ProductListUiState.HasData.Showing>(relaxed = true) {
                    every { data.productBmgmList } returns emptyList()
                }

            createSuccessGetBuyerOrderDetailDataResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                getBuyerOrderDetailData()

                val actualBmgmUiModel = buyerDetailUiState.filterIsInstance(BuyerOrderDetailUiState.HasData.Showing::class.java)
                    .last().productListUiState.data.productBmgmList
                assertEquals(emptyList<ProductBmgmSectionUiModel>(), actualBmgmUiModel)
            }
        }

    @Test
    fun `getProducts should return empty products list when ui state is not equals to Showing`() =
        runCollectingUiState {
            createFailedGetBuyerOrderDetailDataResult()

            getBuyerOrderDetailData()

            assertTrue(viewModel.getProducts().isEmpty())
        }

    @Test
    fun `getShopId should return shop id when UI state is equals to Showing`() =
        runCollectingUiState {
            val productListShowingState =
                mockk<ProductListUiState.HasData.Showing>(relaxed = true) {
                    every { data.productListHeaderUiModel.shopId } returns shopId
                }

            createSuccessGetBuyerOrderDetailDataResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                getBuyerOrderDetailData()

                assertEquals(shopId, viewModel.getShopId())
            }
        }

    @Test
    fun `getShopId should return 0 when ui state is not equals to Showing`() =
        runCollectingUiState {
            createFailedGetBuyerOrderDetailDataResult()

            getBuyerOrderDetailData()

            assertEquals("0", viewModel.getShopId())
        }

    @Test
    fun `getShopName should return shop name when UI state is equals to Showing`() =
        runCollectingUiState {
            val productListShowingState =
                mockk<ProductListUiState.HasData.Showing>(relaxed = true) {
                    every { data.productListHeaderUiModel.shopName } returns shopName
                }

            createSuccessGetBuyerOrderDetailDataResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                getBuyerOrderDetailData()

                assertEquals(shopName, viewModel.getShopName())
            }
        }

    @Test
    fun `getShopName should return empty shop name when ui state is not equals to Showing`() =
        runCollectingUiState {
            createFailedGetBuyerOrderDetailDataResult()

            getBuyerOrderDetailData()

            assertEquals("", viewModel.getShopName())
        }

    @Test
    fun `getShopType should return shop type when ui state is equals to Showing`() =
        runCollectingUiState {
            val productListShowingState =
                mockk<ProductListUiState.HasData.Showing>(relaxed = true) {
                    every { data.productListHeaderUiModel.shopType } returns shopType
                }

            createSuccessGetBuyerOrderDetailDataResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                getBuyerOrderDetailData()

                assertEquals(shopType, viewModel.getShopType())
            }
        }

    @Test
    fun `getShopType should return 0 shop type when ui state is not equals to Showing`() =
        runCollectingUiState {
            createFailedGetBuyerOrderDetailDataResult()

            getBuyerOrderDetailData()

            assertEquals(0, viewModel.getShopType())
        }

    @Test
    fun `getCategoryId should return category id when ui state is not equals to Showing`() =
        runCollectingUiState {
            val productListShowingState =
                mockk<ProductListUiState.HasData.Showing>(relaxed = true) {
                    every { data.productList } returns listOf(product)
                }

            createSuccessGetBuyerOrderDetailDataResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                getBuyerOrderDetailData()

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
                isProcessing = false,
                productUrl = ""
            )
            val productListShowingState =
                mockk<ProductListUiState.HasData.Showing>(relaxed = true) {
                    every { data.productList } returns listOf(
                        product,
                        product,
                        anotherProduct
                    )
                }

            createSuccessGetBuyerOrderDetailDataResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                getBuyerOrderDetailData()

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

            getBuyerOrderDetailData()

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
            val productListShowingState =
                mockk<ProductListUiState.HasData.Showing>(relaxed = true) {
                    every { data.productBundlingList } returns listOf(productBundlingItem)
                }

            createSuccessGetBuyerOrderDetailDataResult()
            mockProductListUiStateMapper(showingState = productListShowingState) {
                getBuyerOrderDetailData()

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
            val orderStatusShowingState =
                mockk<OrderStatusUiState.HasData.Showing>(relaxed = true) {
                    every { data.orderStatusHeaderUiModel.orderStatusId } returns orderStatusId
                }

            createSuccessGetBuyerOrderDetailDataResult()
            mockOrderStatusUiStateMapper(showingState = orderStatusShowingState) {
                getBuyerOrderDetailData()

                assertEquals(orderStatusId, viewModel.getOrderStatusId())
            }
        }

    @Test
    fun `getOrderStatusId should 0 when ui state is not equals to Showing`() =
        runCollectingUiState {
            createFailedGetBuyerOrderDetailDataResult()

            getBuyerOrderDetailData()

            assertEquals("0", viewModel.getOrderStatusId())
        }

    @Test
    fun `getOrderId should not empty when ui state is equals to Showing`() =
        runCollectingUiState {
            val orderStatusShowingState =
                mockk<OrderStatusUiState.HasData.Showing>(relaxed = true) {
                    every { data.orderStatusHeaderUiModel.orderId } returns orderId
                }

            createSuccessGetBuyerOrderDetailDataResult()
            mockOrderStatusUiStateMapper(showingState = orderStatusShowingState) {
                getBuyerOrderDetailData()

                assertEquals(orderId, viewModel.getOrderId())
            }
        }

    @Test
    fun `getOrderId should 0 when ui state is not equals to Showing`() =
        runCollectingUiState {
            createFailedGetBuyerOrderDetailDataResult()

            getBuyerOrderDetailData()

            assertEquals("0", viewModel.getOrderId())
        }

    @Test
    fun `after success with finishing order then update medal touch point state`() {
        runCollectingUiState { buyerOrderDetailUiStateList ->
            val orderStatusShowingState =
                mockk<OrderStatusUiState.HasData.Showing>(relaxed = true) {
                    every { data.orderStatusHeaderUiModel.orderStatusId } returns "600"
                    every { data.orderStatusHeaderUiModel.orderId } returns orderId
                }
            val medalTouchPointData =
                ScpRewardsMedalTouchPointResponse.ScpRewardsMedaliTouchpointOrder.MedaliTouchpointOrder(
                    medaliID = 123312,
                    medaliIconImageURL = "http://tokopedia.com/medaliIconImage",
                    medaliIconImageURLWidget = "http://tokopedia.com/medaliIconImageURLWidget",
                    medaliSunburstImageURL = "http://tokopedia.com/medaliSunburstImageURL",
                    cta = ScpRewardsMedalTouchPointResponse.ScpRewardsMedaliTouchpointOrder.MedaliTouchpointOrder.CtaItem(
                        appLink = "tokopedia://medali",
                        isShown = true
                    )
                )
            val marginLeft = 16
            val marginTop = 22
            val marginRight = 16

            createSuccessGetBuyerOrderDetailDataResult()
            createSuccessFinishOrderResult()

            mockOrderStatusUiStateMapper(showingState = orderStatusShowingState) {
                getBuyerOrderDetailData()
                viewModel.finishOrder()
                advanceUntilIdle()

                viewModel.updateScpRewardsMedalTouchPointWidgetState(
                    data = medalTouchPointData,
                    marginLeft = marginLeft,
                    marginTop = marginTop,
                    marginRight = marginRight
                )

                assertTrue(viewModel.finishOrderResult.value is Success)
                assertEquals(
                    ScpRewardsMedalTouchPointWidgetMapper.map(
                        data = medalTouchPointData,
                        marginLeft = marginLeft,
                        marginTop = marginTop,
                        marginRight = marginRight
                    ),
                    buyerOrderDetailUiStateList.filterIsInstance(BuyerOrderDetailUiState.HasData.Showing::class.java)
                        .last().scpRewardsMedalTouchPointWidgetUiState
                )
            }
        }
    }

    @Test
    fun `after failure with finishing order then dont update medal touch point state`() {
        runCollectingUiState { buyerOrderDetailUiStateList ->
            val orderStatusShowingState =
                mockk<OrderStatusUiState.HasData.Showing>(relaxed = true) {
                    every { data.orderStatusHeaderUiModel.orderStatusId } returns "600"
                    every { data.orderStatusHeaderUiModel.orderId } returns orderId
                }
            createSuccessGetBuyerOrderDetailDataResult()
            createFailedFinishOrderResult()

            mockOrderStatusUiStateMapper(showingState = orderStatusShowingState) {
                getBuyerOrderDetailData()
                viewModel.finishOrder()
                advanceUntilIdle()
                assertTrue(viewModel.finishOrderResult.value is Fail)
                assertEquals(
                    ScpRewardsMedalTouchPointWidgetUiState.HasData.Hidden,
                    buyerOrderDetailUiStateList.filterIsInstance(BuyerOrderDetailUiState.HasData.Showing::class.java)
                        .last().scpRewardsMedalTouchPointWidgetUiState
                )
            }
        }
    }

    @Test
    fun `after success with finishing order but medal touch point state not updated`() {
        runCollectingUiState { buyerOrderDetailUiStateList ->
            val orderStatusShowingState =
                mockk<OrderStatusUiState.HasData.Showing>(relaxed = true) {
                    every { data.orderStatusHeaderUiModel.orderStatusId } returns "600"
                    every { data.orderStatusHeaderUiModel.orderId } returns orderId
                }

            createSuccessGetBuyerOrderDetailDataResult()
            createSuccessFinishOrderResult()

            mockOrderStatusUiStateMapper(showingState = orderStatusShowingState) {
                getBuyerOrderDetailData()
                viewModel.finishOrder()
                advanceUntilIdle()

                assertTrue(viewModel.finishOrderResult.value is Success)
                assertEquals(
                    ScpRewardsMedalTouchPointWidgetUiState.HasData.Hidden,
                    buyerOrderDetailUiStateList.filterIsInstance(BuyerOrderDetailUiState.HasData.Showing::class.java)
                        .last().scpRewardsMedalTouchPointWidgetUiState
                )
            }
        }
    }

    @Test
    fun `hide medal touch point widget after successful finish order`() {
        runCollectingUiState { buyerOrderDetailUiStateList ->
            val orderStatusShowingState =
                mockk<OrderStatusUiState.HasData.Showing>(relaxed = true) {
                    every { data.orderStatusHeaderUiModel.orderStatusId } returns "600"
                    every { data.orderStatusHeaderUiModel.orderId } returns orderId
                }
            val medalTouchPointData =
                ScpRewardsMedalTouchPointResponse.ScpRewardsMedaliTouchpointOrder.MedaliTouchpointOrder(
                    medaliID = 123312,
                    medaliIconImageURL = "http://tokopedia.com/medaliIconImage",
                    medaliIconImageURLWidget = "http://tokopedia.com/medaliIconImageURLWidget",
                    medaliSunburstImageURL = "http://tokopedia.com/medaliSunburstImageURL",
                    cta = ScpRewardsMedalTouchPointResponse.ScpRewardsMedaliTouchpointOrder.MedaliTouchpointOrder.CtaItem(
                        appLink = "tokopedia://medali",
                        isShown = true
                    )
                )
            val marginLeft = 16
            val marginTop = 22
            val marginRight = 16

            createSuccessGetBuyerOrderDetailDataResult()
            createSuccessFinishOrderResult()

            mockOrderStatusUiStateMapper(showingState = orderStatusShowingState) {
                getBuyerOrderDetailData()
                viewModel.finishOrder()
                advanceUntilIdle()

                viewModel.updateScpRewardsMedalTouchPointWidgetState(
                    data = medalTouchPointData,
                    marginLeft = marginLeft,
                    marginTop = marginTop,
                    marginRight = marginRight
                )

                assertTrue(viewModel.finishOrderResult.value is Success)
                assertEquals(
                    ScpRewardsMedalTouchPointWidgetMapper.map(
                        data = medalTouchPointData,
                        marginLeft = marginLeft,
                        marginTop = marginTop,
                        marginRight = marginRight
                    ),
                    buyerOrderDetailUiStateList.filterIsInstance(BuyerOrderDetailUiState.HasData.Showing::class.java)
                        .last().scpRewardsMedalTouchPointWidgetUiState
                )
                viewModel.hideScpRewardsMedalTouchPointWidget()
                assertEquals(
                    ScpRewardsMedalTouchPointWidgetUiState.HasData.Hidden,
                    buyerOrderDetailUiStateList.filterIsInstance(BuyerOrderDetailUiState.HasData.Showing::class.java)
                        .last().scpRewardsMedalTouchPointWidgetUiState
                )
            }
        }
    }
}
