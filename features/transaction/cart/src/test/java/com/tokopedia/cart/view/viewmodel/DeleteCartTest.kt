package com.tokopedia.cart.view.viewmodel

import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cartcommon.data.response.deletecart.Data
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartrevamp.view.uimodel.CartGroupHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.DeleteCartEvent
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import rx.Observable

class DeleteCartTest : BaseCartViewModelTest() {

    @Test
    fun `WHEN delete all cart item success THEN should render success`() {
        // GIVEN
        val cartItemData = CartItemHolderData(cartId = "0")
        val cartGroupHolderData = CartGroupHolderData().apply {
            productUiModelList.add(cartItemData)
        }

        cartViewModel.addItem(cartGroupHolderData)

        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(
                RemoveFromCartData(
                    status = "OK",
                    data = Data(success = 1)
                )
            )
        }

        coEvery { getCartRevampV4UseCase(any()) } returns CartData()
        every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(1)

        // WHEN
        cartViewModel.processDeleteCartItem(
            arrayListOf(cartItemData),
            addWishList = false,
            forceExpandCollapsedUnavailableItems = false,
            isFromGlobalCheckbox = true,
            isFromEditBundle = false
        )

        // THEN
        assertEquals(
            DeleteCartEvent.Success(
                arrayListOf("0"),
                removeAllItems = true,
                forceExpandCollapsedUnavailableItems = false,
                isFromGlobalCheckbox = true,
                isFromEditBundle = false,
                addWishList = false
            ),
            cartViewModel.deleteCartEvent.value
        )
    }

    @Test
    fun `WHEN delete some cart item success THEN should render success`() {
        // GIVEN
        val firstCartItemData = CartItemHolderData()
        val secondCartItemData = CartItemHolderData().apply {
            cartId = "1"
        }

        cartViewModel.addItem(firstCartItemData)
        cartViewModel.addItem(secondCartItemData)

        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(
                RemoveFromCartData(
                    status = "OK",
                    data = Data(success = 1)
                )
            )
        }

        every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(1)

        // WHEN
        cartViewModel.processDeleteCartItem(
            arrayListOf(secondCartItemData),
            addWishList = false,
            forceExpandCollapsedUnavailableItems = false
        )

        // THEN
        assertEquals(
            DeleteCartEvent.Success(
                arrayListOf("1"),
                removeAllItems = false,
                forceExpandCollapsedUnavailableItems = false,
                isFromGlobalCheckbox = false,
                isFromEditBundle = false,
                addWishList = false
            ),
            cartViewModel.deleteCartEvent.value
        )
    }

    @Test
    fun `WHEN delete some cart item success and need to validate use THEN should render success`() {
        // GIVEN
        val firstCartItemData = CartItemHolderData()
        val secondCartItemData = CartItemHolderData().apply {
            cartId = "1"
        }

        cartViewModel.addItem(firstCartItemData)
        cartViewModel.addItem(secondCartItemData)

        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(
                RemoveFromCartData(
                    status = "OK",
                    data = Data(success = 1)
                )
            )
        }

        every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(1)
        every { view.checkHitValidateUseIsNeeded(any()) } returns true

        // WHEN
        cartViewModel.processDeleteCartItem(
            arrayListOf(secondCartItemData),
            addWishList = false,
            forceExpandCollapsedUnavailableItems = false
        )

        // THEN
        assertEquals(
            DeleteCartEvent.Success(
                arrayListOf("1"),
                removeAllItems = false,
                forceExpandCollapsedUnavailableItems = false,
                isFromGlobalCheckbox = false,
                isFromEditBundle = false,
                addWishList = false
            ),
            cartViewModel.deleteCartEvent.value
        )
    }

    @Test
    fun `WHEN delete cart item failed THEN should render error`() {
        // GIVEN
        val throwable = ResponseErrorException("fail testing delete")
        val cartItemData = CartItemHolderData()

        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(1)

        // WHEN
        cartViewModel.processDeleteCartItem(
            arrayListOf(cartItemData),
            addWishList = false,
            forceExpandCollapsedUnavailableItems = false
        )

        // THEN
        assertEquals(
            DeleteCartEvent.Failed(false, throwable),
            cartViewModel.deleteCartEvent.value
        )
    }

    @Test
    fun `WHEN delete cart item failed and has flag forceExpandCollapsedUnavailableItems THEN should render error and re collapse expanded unavailable items`() {
        // GIVEN
        val throwable = ResponseErrorException("fail testing delete")
        val cartItemData = CartItemHolderData()

        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(1)

        // WHEN
        cartViewModel.processDeleteCartItem(
            arrayListOf(cartItemData),
            addWishList = false,
            forceExpandCollapsedUnavailableItems = true
        )

        // THEN
        assertEquals(
            DeleteCartEvent.Failed(true, throwable),
            cartViewModel.deleteCartEvent.value
        )
    }

    @After
    fun tearDown() {
    }
}
