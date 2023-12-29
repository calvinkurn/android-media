package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.utils.DataProvider
import com.tokopedia.cart.view.CartListPresenter
import com.tokopedia.cart.view.uimodel.CartGroupHolderData
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.uimodel.CartShopGroupTickerData
import com.tokopedia.cart.view.uimodel.CartShopGroupTickerState
import com.tokopedia.cartcommon.data.response.common.OutOfService
import com.tokopedia.cartcommon.data.response.updatecart.Data
import com.tokopedia.cartcommon.data.response.updatecart.ToasterAction
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCheckout.Companion.KEY_CHECKOUT
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCheckout.Companion.KEY_PRODUCT
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import org.junit.Test

class UpdateCartTest : BaseCartTest() {

    @Test
    fun `WHEN update cart for checkout success THEN should navigate to checkout page`() {
        // GIVEN
        val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
            add(
                CartItemHolderData().apply {
                    isError = false
                    isBundlingItem = false
                    isCod = true
                    productPrice = 1000.0
                    quantity = 10
                    trackerAttribution = "t"
                    category = "c"
                }
            )
            add(
                CartItemHolderData().apply {
                    isError = false
                    isBundlingItem = true
                    isCod = true
                    productPrice = 1000.0
                    quantity = 10
                    trackerAttribution = "t"
                    category = "c"
                }
            )
            add(
                CartItemHolderData().apply {
                    isError = true
                    isBundlingItem = true
                    isCod = true
                    productPrice = 1000.0
                    quantity = 10
                    trackerAttribution = "t"
                    category = "c"
                }
            )
        }

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }
        every { view.getAllAvailableCartDataList() } answers { cartItemDataList }
        every { view.getAllSelectedCartDataList() } answers { cartItemDataList }

        // WHEN
        cartListPresenter.processUpdateCartData(false)

        // THEN
        verify {
            view.renderToShipmentFormSuccess(any(), cartItemDataList, any(), any())
        }
    }

    @Test
    fun `WHEN update cart for checkout success with eligible COD THEN should navigate to checkout page with eligible COD`() {
        // GIVEN
        val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
            add(
                CartItemHolderData().apply {
                    isCod = true
                    productPrice = 1000.0
                    quantity = 10
                }
            )
        }

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }

        every { view.getAllSelectedCartDataList() } answers { cartItemDataList }

        // WHEN
        cartListPresenter.processUpdateCartData(false)

        // THEN
        verify {
            view.renderToShipmentFormSuccess(any(), any(), true, any())
        }
    }

    @Test
    fun `WHEN update cart for checkout success with not eligible COD THEN should navigate to checkout page with not eligible COD`() {
        // GIVEN
        val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
            add(
                CartItemHolderData().apply {
                    isCod = false
                    productPrice = 1000000.0
                    quantity = 10
                }
            )
        }

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }

        every { view.getAllSelectedCartDataList() } answers { cartItemDataList }

        // WHEN
        cartListPresenter.processUpdateCartData(false)

        // THEN
        verify {
            view.renderToShipmentFormSuccess(any(), any(), false, any())
        }
    }

    @Test
    fun `WHEN update cart for checkout success with item checked all without changes THEN should navigate to checkout page with matched condition`() {
        // GIVEN
        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }
        cartListPresenter.setHasPerformChecklistChange(false)
        every { view.getAllSelectedCartDataList() } answers { listOf(CartItemHolderData()) }

        // WHEN
        cartListPresenter.processUpdateCartData(false)

        // THEN
        verify {
            view.renderToShipmentFormSuccess(any(), any(), any(), CartListPresenter.ITEM_CHECKED_ALL_WITHOUT_CHANGES)
        }
    }

    @Test
    fun `WHEN update cart for checkout success with item checked all with changes THEN should navigate to checkout page with matched condition`() {
        // GIVEN
        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }
        cartListPresenter.setHasPerformChecklistChange(true)
        every { view.getAllSelectedCartDataList() } answers { listOf(CartItemHolderData()) }

        // WHEN
        cartListPresenter.processUpdateCartData(false)

        // THEN
        verify {
            view.renderToShipmentFormSuccess(any(), any(), any(), CartListPresenter.ITEM_CHECKED_ALL_WITH_CHANGES)
        }
    }

    @Test
    fun `WHEN update cart for checkout success with item checked partial item THEN should navigate to checkout page with matched condition`() {
        // GIVEN
        val shopDataList = mutableListOf<CartGroupHolderData>().apply {
            add(
                CartGroupHolderData().apply {
                    productUiModelList = mutableListOf<CartItemHolderData>().apply {
                        add(
                            CartItemHolderData().apply {
                                isSelected = false
                            }
                        )
                    }
                }
            )
        }

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }
        every { view.getAllGroupDataList() } answers { shopDataList }
        every { view.getAllSelectedCartDataList() } answers { listOf(CartItemHolderData()) }

        // WHEN
        cartListPresenter.processUpdateCartData(false)

        // THEN
        verify {
            view.renderToShipmentFormSuccess(any(), any(), any(), CartListPresenter.ITEM_CHECKED_PARTIAL_ITEM)
        }
    }

    @Test
    fun `WHEN update cart for checkout success with item checked partial shop THEN should navigate to checkout page with matched condition`() {
        // GIVEN
        val shopDataList = mutableListOf<CartGroupHolderData>().apply {
            add(
                CartGroupHolderData().apply {
                    productUiModelList = mutableListOf<CartItemHolderData>().apply {
                        add(
                            CartItemHolderData().apply {
                                isSelected = true
                            }
                        )
                    }
                    isAllSelected = true
                }
            )
            add(
                CartGroupHolderData().apply {
                    productUiModelList = mutableListOf<CartItemHolderData>().apply {
                        add(
                            CartItemHolderData().apply {
                                isSelected = false
                            }
                        )
                    }
                    isAllSelected = false
                }
            )
        }

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }

        every { view.getAllGroupDataList() } answers { shopDataList }
        every { view.getAllSelectedCartDataList() } answers { listOf(CartItemHolderData()) }

        // WHEN
        cartListPresenter.processUpdateCartData(false)

        // THEN
        verify {
            view.renderToShipmentFormSuccess(any(), any(), any(), CartListPresenter.ITEM_CHECKED_PARTIAL_SHOP)
        }
    }

    @Test
    fun `WHEN update cart for checkout success with item checked partial shop and item THEN should navigate to checkout page with matched condition`() {
        // GIVEN
        val shopDataList = mutableListOf<CartGroupHolderData>().apply {
            add(
                CartGroupHolderData().apply {
                    productUiModelList = mutableListOf<CartItemHolderData>().apply {
                        add(
                            CartItemHolderData().apply {
                                isSelected = true
                            }
                        )
                        add(
                            CartItemHolderData().apply {
                                isSelected = false
                            }
                        )
                    }
                    isAllSelected = false
                    isPartialSelected = true
                }
            )
            add(
                CartGroupHolderData().apply {
                    productUiModelList = mutableListOf<CartItemHolderData>().apply {
                        add(
                            CartItemHolderData().apply {
                                isSelected = false
                            }
                        )
                    }
                    isAllSelected = false
                }
            )
        }

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }
        every { view.getAllGroupDataList() } answers { shopDataList }
        every { view.getAllSelectedCartDataList() } answers { listOf(CartItemHolderData()) }

        // WHEN
        cartListPresenter.processUpdateCartData(false)

        // THEN
        verify {
            view.renderToShipmentFormSuccess(any(), any(), any(), CartListPresenter.ITEM_CHECKED_PARTIAL_SHOP_AND_ITEM)
        }
    }

    @Test
    fun `WHEN update cart for checkout success with not blank and afford BO affordability THEN should send EE dimension119 value with fulfill`() {
        // GIVEN
        val tickerText = "tickerText"
        val boType = 0
        val cartItem = CartItemHolderData().apply {
            isSelected = true
            shopCartShopGroupTickerData = CartShopGroupTickerData(
                tickerText = tickerText,
                state = CartShopGroupTickerState.SUCCESS_AFFORD
            )
            shopBoMetadata = BoMetadata(
                boType = boType
            )
        }
        val shopDataList = mutableListOf<CartGroupHolderData>().apply {
            add(
                CartGroupHolderData().apply {
                    productUiModelList = mutableListOf(cartItem)
                    isAllSelected = true
                    isPartialSelected = false
                }
            )
        }

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }
        every { view.getAllGroupDataList() } answers { shopDataList }
        every { view.getAllSelectedCartDataList() } answers { listOf(cartItem) }

        // WHEN
        cartListPresenter.processUpdateCartData(false)

        // THEN
        verify {
            view.renderToShipmentFormSuccess(
                match {
                    (((it[KEY_CHECKOUT]!! as Map<String, Any>)[KEY_PRODUCT]!! as List<Any>)[0] as Map<String, Any>)["dimension119"] == "fulfill_$boType"
                },
                any(),
                any(),
                any()
            )
        }
    }

    @Test
    fun `WHEN update cart for checkout success with not blank and not afford BO affordability THEN should send EE dimension119 value with unfulfill`() {
        // GIVEN
        val tickerText = "tickerText"
        val boType = 0
        val cartItem = CartItemHolderData().apply {
            isSelected = true
            shopCartShopGroupTickerData = CartShopGroupTickerData(
                tickerText = tickerText,
                state = CartShopGroupTickerState.SUCCESS_NOT_AFFORD
            )
            shopBoMetadata = BoMetadata(
                boType = boType
            )
        }
        val shopDataList = mutableListOf<CartGroupHolderData>().apply {
            add(
                CartGroupHolderData().apply {
                    productUiModelList = mutableListOf(cartItem)
                    isAllSelected = true
                    isPartialSelected = false
                }
            )
        }

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }
        every { view.getAllGroupDataList() } answers { shopDataList }
        every { view.getAllSelectedCartDataList() } answers { listOf(cartItem) }

        // WHEN
        cartListPresenter.processUpdateCartData(false)

        // THEN
        verify {
            view.renderToShipmentFormSuccess(
                match {
                    (((it[KEY_CHECKOUT]!! as Map<String, Any>)[KEY_PRODUCT]!! as List<Any>)[0] as Map<String, Any>)["dimension119"] == "unfulfill_$boType"
                },
                any(),
                any(),
                any()
            )
        }
    }

    @Test
    fun `WHEN update cart for checkout success with blank BO affordability THEN should send EE dimension119 empty`() {
        // GIVEN
        val tickerText = ""
        val cartItem = CartItemHolderData().apply {
            isSelected = true
            shopCartShopGroupTickerData = CartShopGroupTickerData(
                tickerText = tickerText
            )
        }
        val shopDataList = mutableListOf<CartGroupHolderData>().apply {
            add(
                CartGroupHolderData().apply {
                    productUiModelList = mutableListOf(cartItem)
                    isAllSelected = true
                    isPartialSelected = false
                }
            )
        }

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }
        every { view.getAllGroupDataList() } answers { shopDataList }
        every { view.getAllSelectedCartDataList() } answers { listOf(cartItem) }

        // WHEN
        cartListPresenter.processUpdateCartData(false)

        // THEN
        verify {
            view.renderToShipmentFormSuccess(
                match {
                    (((it[KEY_CHECKOUT]!! as Map<String, Any>)[KEY_PRODUCT]!! as List<Any>)[0] as Map<String, Any>)["dimension119"] == ""
                },
                any(),
                any(),
                any()
            )
        }
    }

    @Test
    fun `WHEN update cart for checkout failed with out of service THEN should render out of service error`() {
        // GIVEN
        val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
            add(
                CartItemHolderData().apply {
                    isCod = true
                    productPrice = 1000.0
                    quantity = 10
                }
            )
        }

        val outOfService = OutOfService(id = "503")
        val mockResponse = UpdateCartV2Data(data = Data(status = false, outOfService = outOfService))
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }
        every { view.getAllAvailableCartDataList() } answers { cartItemDataList }
        every { view.getAllSelectedCartDataList() } answers { cartItemDataList }

        // WHEN
        cartListPresenter.processUpdateCartData(false)

        // THEN
        verify {
            view.renderErrorToShipmentForm(outOfService)
        }
    }

    @Test
    fun `WHEN update cart for checkout failed with other error and no need to show cta THEN should render error with no cta`() {
        // GIVEN
        val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
            add(
                CartItemHolderData().apply {
                    isCod = true
                    productPrice = 1000.0
                    quantity = 10
                }
            )
        }

        val errorMessage = "error message"
        val mockResponse = UpdateCartV2Data(data = Data(status = false, error = errorMessage, toasterAction = ToasterAction(showCta = false)))
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }
        every { view.getAllAvailableCartDataList() } answers { cartItemDataList }
        every { view.getAllSelectedCartDataList() } answers { cartItemDataList }

        // WHEN
        cartListPresenter.processUpdateCartData(false)

        // THEN
        verify {
            view.renderErrorToShipmentForm(errorMessage, "")
        }
    }

    @Test
    fun `WHEN update cart for checkout failed with other error and need to show cta THEN should render error with cta`() {
        // GIVEN
        val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
            add(
                CartItemHolderData().apply {
                    isCod = true
                    productPrice = 1000.0
                    quantity = 10
                }
            )
        }

        val errorMessage = "error message"
        val cta = "Oke"
        val mockResponse = UpdateCartV2Data(data = Data(status = false, error = errorMessage, toasterAction = ToasterAction(showCta = true, text = cta)))
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }
        every { view.getAllAvailableCartDataList() } answers { cartItemDataList }
        every { view.getAllSelectedCartDataList() } answers { cartItemDataList }

        // WHEN
        cartListPresenter.processUpdateCartData(false)

        // THEN
        verify {
            view.renderErrorToShipmentForm(errorMessage, cta)
        }
    }

    @Test
    fun `WHEN update cart failed THEN should render error`() {
        // GIVEN
        val throwable = ResponseErrorException("error")

        every { view.getAllSelectedCartDataList() } answers { listOf(CartItemHolderData()) }
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        // WHEN
        cartListPresenter.processUpdateCartData(false)

        // THEN
        verify {
            view.renderErrorToShipmentForm(throwable)
        }
    }

    @Test
    fun `WHEN update cart with empty params THEN should not call API`() {
        // GIVEN
        every { view.getAllSelectedCartDataList() } answers { emptyList() }

        // WHEN
        cartListPresenter.processUpdateCartData(false)

        // THEN
        verify(inverse = true) {
            updateCartUseCase.execute(any(), any())
        }
    }

    @Test
    fun `WHEN update cart for save state with empty params THEN should not call API`() {
        // GIVEN
        every { view.getAllAvailableCartDataList() } answers { emptyList() }

        // WHEN
        cartListPresenter.processUpdateCartData(true)

        // THEN
        verify(inverse = true) {
            updateCartUseCase.execute(any(), any())
        }
    }

    @Test
    fun `WHEN update cart for on fragment stopped THEN should call API`() {
        // GIVEN
        val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
            add(
                CartItemHolderData().apply {
                    isCod = true
                    productPrice = 1000.0
                    quantity = 10
                }
            )
        }

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }
        every { view.getAllAvailableCartDataList() } answers { cartItemDataList }
        every { view.getAllSelectedCartDataList() } answers { cartItemDataList }

        // WHEN
        cartListPresenter.processUpdateCartData(true)

        // THEN
        verify {
            updateCartUseCase.execute(any(), any())
        }
    }

    @Test
    fun `WHEN update cart for on fragment stopped THEN should not show loading`() {
        // GIVEN
        val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
            add(
                CartItemHolderData().apply {
                    isCod = true
                    productPrice = 1000.0
                    quantity = 10
                }
            )
        }

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }
        every { view.getAllAvailableCartDataList() } answers { cartItemDataList }
        every { view.getAllSelectedCartDataList() } answers { cartItemDataList }

        // WHEN
        cartListPresenter.processUpdateCartData(true)

        // THEN
        verify(inverse = true) {
            view.showProgressLoading()
        }
    }

    @Test
    fun `WHEN update cart for checkout with view is detached THEN should not render view`() {
        // GIVEN
        cartListPresenter.detachView()

        // WHEN
        cartListPresenter.processUpdateCartData(false)

        // THEN
        verify(inverse = true) {
            view.renderToShipmentFormSuccess(any(), any(), any(), any())
        }
    }
}
