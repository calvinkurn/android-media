package com.tokopedia.cart.view.viewmodel

import androidx.lifecycle.Observer
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cart.domain.model.updatecart.UpdateAndReloadCartListData
import com.tokopedia.cartrevamp.view.helper.CartDataHelper
import com.tokopedia.cartrevamp.view.uimodel.CartGlobalEvent
import com.tokopedia.cartrevamp.view.uimodel.CartItemHolderData
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class UpdateAndReloadCartTest : BaseCartViewModelTest() {

    private lateinit var observer: Observer<CartGlobalEvent>

    override fun setUp() {
        super.setUp()
        val cartItemData = CartItemHolderData().apply {
            notes = ""
        }
        every { CartDataHelper.getAllAvailableCartItemData(any()) } returns arrayListOf(cartItemData)

        observer = mockk { every { onChanged(any()) } just Runs }
        cartViewModel.globalEvent.observeForever(observer)
    }

    @Test
    fun `WHEN update cart success and reload cart empty THEN should hide loading and no error shown`() {
        // GIVEN
        val emptyCartListData = UpdateAndReloadCartListData()

        coEvery { updateAndReloadCartUseCase(any()) } returns emptyCartListData

        // WHEN
        cartViewModel.processToUpdateAndReloadCartData("0")

        // THEN
        verify {
            observer.onChanged(CartGlobalEvent.ProgressLoading(false))
        }
        verify(inverse = true) {
            observer.onChanged(CartGlobalEvent.UpdateAndReloadCartFailed(Throwable()))
        }
    }

    @Test
    fun `WHEN update cart success and reload cart success THEN should hide loading and no error shown`() {
        // GIVEN
        val updateAndReloadCartListData = UpdateAndReloadCartListData()

        coEvery { updateAndReloadCartUseCase(any()) } returns updateAndReloadCartListData
        coEvery { getCartRevampV4UseCase(any()) } returns CartData()

        // WHEN
        cartViewModel.processToUpdateAndReloadCartData("0")

        // THEN
        verify {
            observer.onChanged(CartGlobalEvent.ProgressLoading(false))
        }
        verify(inverse = true) {
            observer.onChanged(CartGlobalEvent.UpdateAndReloadCartFailed(Throwable()))
        }
    }

    @Test
    fun `WHEN update cart and reload cart failed THEN should hide loading and show error`() {
        // GIVEN
        val exception = CartResponseErrorException("error message")

        coEvery { updateAndReloadCartUseCase(any()) } throws exception

        // WHEN
        cartViewModel.processToUpdateAndReloadCartData("0")

        // THEN
        verify { observer.onChanged(CartGlobalEvent.UpdateAndReloadCartFailed(exception)) }
    }

    override fun tearDown() {
        super.tearDown()
        cartViewModel.globalEvent.removeObserver(observer)
    }
}
