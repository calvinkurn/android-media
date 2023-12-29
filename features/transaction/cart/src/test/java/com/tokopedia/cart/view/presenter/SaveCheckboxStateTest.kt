package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.view.uimodel.CartItemHolderData
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Test

class SaveCheckboxStateTest : BaseCartTest() {

    @Test
    fun `WHEN trigger save checkbox state THEN use case should be called`() {
        // GIVEN
        val cartItemDataList = ArrayList<CartItemHolderData>().apply {
            add(
                CartItemHolderData(
                    isSelected = true,
                    cartId = "123"
                )
            )
        }

        coEvery { setCartlistCheckboxStateUseCase(any()) } returns true

        // WHEN
        cartListPresenter.saveCheckboxState(cartItemDataList)

        // THEN
        coVerify {
            setCartlistCheckboxStateUseCase(any())
        }
    }
}
