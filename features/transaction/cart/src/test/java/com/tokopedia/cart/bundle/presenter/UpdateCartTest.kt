package com.tokopedia.cart.bundle.presenter

import com.tokopedia.cart.bundle.utils.DataProvider
import com.tokopedia.cart.bundle.view.CartListPresenter
import com.tokopedia.cart.bundle.view.uimodel.CartItemHolderData
import com.tokopedia.cart.bundle.view.uimodel.CartShopHolderData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.network.exception.ResponseErrorException
import io.mockk.*
import org.junit.Test

class UpdateCartTest : BaseCartTest() {

    @Test
    fun `WHEN update cart for checkout success THEN should navigate to checkout page`() {
        // GIVEN
        val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
            add(CartItemHolderData().apply {
                isCod = true
                productPrice = 1000
                quantity = 10
            })
        }

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }
        every { view.getAllAvailableCartDataList() } answers { cartItemDataList }
        every { view.getAllSelectedCartDataList() } answers { cartItemDataList }

        // WHEN
        cartListPresenter?.processUpdateCartData(false)

        // THEN
        verify {
            view.renderToShipmentFormSuccess(any(), cartItemDataList, any(), any())
        }
    }

    @Test
    fun `WHEN update cart for checkout success with eligible COD THEN should navigate to checkout page with eligible COD`() {
        // GIVEN
        val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
            add(CartItemHolderData().apply {
                isCod = true
                productPrice = 1000
                quantity = 10
            })
        }

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }

        every { view.getAllSelectedCartDataList() } answers { cartItemDataList }

        // WHEN
        cartListPresenter?.processUpdateCartData(false)

        // THEN
        verify {
            view.renderToShipmentFormSuccess(any(), any(), true, any())
        }
    }

    @Test
    fun `WHEN update cart for checkout success with not eligible COD THEN should navigate to checkout page with not eligible COD`() {
        // GIVEN
        val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
            add(CartItemHolderData().apply {
                isCod = false
                productPrice = 1000000
                quantity = 10
            })
        }

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }

        every { view.getAllSelectedCartDataList() } answers { cartItemDataList }

        // WHEN
        cartListPresenter?.processUpdateCartData(false)

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
        cartListPresenter?.setHasPerformChecklistChange(false)
        every { view.getAllSelectedCartDataList() } answers { listOf(CartItemHolderData()) }

        // WHEN
        cartListPresenter?.processUpdateCartData(false)

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
        cartListPresenter?.setHasPerformChecklistChange(true)
        every { view.getAllSelectedCartDataList() } answers { listOf(CartItemHolderData()) }

        // WHEN
        cartListPresenter?.processUpdateCartData(false)

        // THEN
        verify {
            view.renderToShipmentFormSuccess(any(), any(), any(), CartListPresenter.ITEM_CHECKED_ALL_WITH_CHANGES)
        }
    }

    @Test
    fun `WHEN update cart for checkout success with item checked partial item THEN should navigate to checkout page with matched condition`() {
        // GIVEN
        val shopDataList = mutableListOf<CartShopHolderData>().apply {
            add(CartShopHolderData().apply {
                productUiModelList = mutableListOf<CartItemHolderData>().apply {
                    add(CartItemHolderData().apply {
                        isSelected = false
                    })
                }
            })
        }

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }
        every { view.getAllShopDataList() } answers { shopDataList }
        every { view.getAllSelectedCartDataList() } answers { listOf(CartItemHolderData()) }

        // WHEN
        cartListPresenter?.processUpdateCartData(false)

        // THEN
        verify {
            view.renderToShipmentFormSuccess(any(), any(), any(), CartListPresenter.ITEM_CHECKED_PARTIAL_ITEM)
        }
    }

    @Test
    fun `WHEN update cart for checkout success with item checked partial shop THEN should navigate to checkout page with matched condition`() {
        // GIVEN
        val shopDataList = mutableListOf<CartShopHolderData>().apply {
            add(CartShopHolderData().apply {
                productUiModelList = mutableListOf<CartItemHolderData>().apply {
                    add(CartItemHolderData().apply {
                        isSelected = true
                    })
                }
                isAllSelected = true
            })
            add(CartShopHolderData().apply {
                productUiModelList = mutableListOf<CartItemHolderData>().apply {
                    add(CartItemHolderData().apply {
                        isSelected = false
                    })
                }
                isAllSelected = false
            })
        }

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }

        every { view.getAllShopDataList() } answers { shopDataList }
        every { view.getAllSelectedCartDataList() } answers { listOf(CartItemHolderData()) }

        // WHEN
        cartListPresenter?.processUpdateCartData(false)

        // THEN
        verify {
            view.renderToShipmentFormSuccess(any(), any(), any(), CartListPresenter.ITEM_CHECKED_PARTIAL_SHOP)
        }
    }

    @Test
    fun `WHEN update cart for checkout success with item checked partial shop and item THEN should navigate to checkout page with matched condition`() {
        // GIVEN
        val shopDataList = mutableListOf<CartShopHolderData>().apply {
            add(CartShopHolderData().apply {
                productUiModelList = mutableListOf<CartItemHolderData>().apply {
                    add(CartItemHolderData().apply {
                        isSelected = true
                    })
                    add(CartItemHolderData().apply {
                        isSelected = false
                    })
                }
                isAllSelected = false
                isPartialSelected = true
            })
            add(CartShopHolderData().apply {
                productUiModelList = mutableListOf<CartItemHolderData>().apply {
                    add(CartItemHolderData().apply {
                        isSelected = false
                    })
                }
                isAllSelected = false
            })
        }

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }
        every { view.getAllShopDataList() } answers { shopDataList }
        every { view.getAllSelectedCartDataList() } answers { listOf(CartItemHolderData()) }

        // WHEN
        cartListPresenter?.processUpdateCartData(false)

        // THEN
        verify {
            view.renderToShipmentFormSuccess(any(), any(), any(), CartListPresenter.ITEM_CHECKED_PARTIAL_SHOP_AND_ITEM)
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
        cartListPresenter?.processUpdateCartData(false)

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
        cartListPresenter?.processUpdateCartData(false)

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
        cartListPresenter?.processUpdateCartData(true)

        // THEN
        verify(inverse = true) {
            updateCartUseCase.execute(any(), any())
        }
    }

}