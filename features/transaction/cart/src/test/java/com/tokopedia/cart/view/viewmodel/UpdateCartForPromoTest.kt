package com.tokopedia.cart.view.viewmodel

import com.tokopedia.cart.utils.DataProvider
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartrevamp.view.helper.CartDataHelper
import com.tokopedia.cartrevamp.view.uimodel.CartItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.UpdateCartPromoState
import com.tokopedia.network.exception.ResponseErrorException
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class UpdateCartForPromoTest : BaseCartViewModelTest() {

    override fun setUp() {
        super.setUp()
        mockkObject(CartDataHelper)
    }

    @Test
    fun `WHEN update cart for promo success THEN should navigate to promo page`() {
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

        every { CartDataHelper.getSelectedCartItemData(any()) } answers { cartItemDataList }
        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }

        // WHEN
        cartViewModel.doUpdateCartForPromo()

        // THEN
        assertEquals(
            UpdateCartPromoState.Success,
            cartViewModel.updateCartForPromoState.value
        )
    }

    @Test
    fun `WHEN update cart for promo failed with exception THEN should render error`() {
        // GIVEN
        val exception = ResponseErrorException("error message")

        val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
            add(
                CartItemHolderData().apply {
                    isCod = true
                    productPrice = 1000.0
                    quantity = 10
                }
            )
        }

        every { CartDataHelper.getSelectedCartItemData(any()) } answers { cartItemDataList }
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(exception)
        }

        // WHEN
        cartViewModel.doUpdateCartForPromo()

        // THEN
        assertEquals(
            UpdateCartPromoState.Failed(exception),
            cartViewModel.updateCartForPromoState.value
        )
    }

    @Test
    fun `WHEN update cart for promo with empty parameter THEN should not hit API`() {
        // GIVEN
        every { CartDataHelper.getSelectedCartItemData(any()) } answers { emptyList() }

        // WHEN
        cartViewModel.doUpdateCartForPromo()

        // THEN
        verify(inverse = true) {
            updateCartUseCase.execute(any(), any())
        }
    }

    override fun tearDown() {
        super.tearDown()
        unmockkObject(CartDataHelper)
    }
}
