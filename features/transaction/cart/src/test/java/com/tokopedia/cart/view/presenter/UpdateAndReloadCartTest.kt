package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cart.domain.model.updatecart.UpdateAndReloadCartListData
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import io.mockk.coEvery
import io.mockk.every
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Test

class UpdateAndReloadCartTest : BaseCartTest() {

    @Test
    fun `WHEN update cart success and reload cart empty THEN should hide loading and no error shown`() {
        // GIVEN
        val emptyCartListData = UpdateAndReloadCartListData()

        coEvery { updateAndReloadCartUseCase(any()) } returns emptyCartListData

        // WHEN
        cartListPresenter.processToUpdateAndReloadCartData("0")

        // THEN
        verify {
            view.hideProgressLoading()
        }
        verify(inverse = true) {
            view.showToastMessageRed(Throwable())
        }
    }

    @Test
    fun `WHEN update cart success and reload cart success THEN should hide loading and no error shown`() {
        // GIVEN
        val cartItemData = CartItemHolderData().apply {
            notes = ""
        }
        val updateAndReloadCartListData = UpdateAndReloadCartListData()

        coEvery { updateAndReloadCartUseCase(any()) } returns updateAndReloadCartListData

        every { view.getAllAvailableCartDataList() } returns arrayListOf(cartItemData)
        coEvery { getCartRevampV4UseCase(any()) } returns CartData()

        // WHEN
        cartListPresenter.processToUpdateAndReloadCartData("0")

        // THEN
        verify {
            view.hideProgressLoading()
        }
        verify(inverse = true) {
            view.showToastMessageRed(Throwable())
        }
    }

    @Test
    fun `WHEN update cart and reload cart failed THEN should hide loading and show error`() {
        // GIVEN
        val exception = CartResponseErrorException("error message")
        val cartItemData = CartItemHolderData().apply {
            notes = ""
        }

        coEvery { updateAndReloadCartUseCase(any()) } throws exception
        every { view.getAllAvailableCartDataList() } returns arrayListOf(cartItemData)

        // WHEN
        cartListPresenter.processToUpdateAndReloadCartData("0")

        // THEN
        verifyOrder {
            view.hideProgressLoading()
            view.showToastMessageRed(exception)
        }
    }

    @Test
    fun `WHEN update cart and reload with view is detched THEN should not render view`() {
        // GIVEN
        cartListPresenter.detachView()

        // WHEN
        cartListPresenter.processToUpdateAndReloadCartData("0")

        // THEN
        verify(inverse = true) {
            view.hideProgressLoading()
        }
    }
}
