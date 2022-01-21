package com.tokopedia.cart.view.presenter

import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cartcommon.data.response.deletecart.Data
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import io.mockk.*
import org.junit.Test
import rx.Observable

class DeleteCartTest : BaseCartTest() {

    @Test
    fun `WHEN delete all cart item success THEN should render success`() {
        // GIVEN
        val cartItemData = CartItemHolderData(cartId = "0")

        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(RemoveFromCartData(status = "OK", data = Data(success = 1)))
        }

        coEvery { getCartRevampV3UseCase.setParams(any(), any()) } just Runs
        coEvery { getCartRevampV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartData) -> Unit>().invoke(CartData())
        }

        every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(1)

        // WHEN
        cartListPresenter.processDeleteCartItem(arrayListOf(cartItemData), arrayListOf(cartItemData), false, false, true, false)

        // THEN
        verify {
            view.onDeleteCartDataSuccess(arrayListOf("0"), true, false, false, true, false)
        }
    }

    @Test
    fun `WHEN delete some cart item success THEN should render success`() {
        // GIVEN
        val firstCartItemData = CartItemHolderData()
        val secondCartItemData = CartItemHolderData().apply {
            cartId = "1"
        }

        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(RemoveFromCartData(status = "OK", data = Data(success = 1)))
        }

        every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(1)

        // WHEN
        cartListPresenter.processDeleteCartItem(arrayListOf(firstCartItemData, secondCartItemData),
                arrayListOf(secondCartItemData), false, false)

        // THEN
        verify {
            view.onDeleteCartDataSuccess(arrayListOf("1"), false, false, false, false, false)
        }

    }

    @Test
    fun `WHEN delete some cart item success and need to validate use THEN should render success`() {
        // GIVEN
        val firstCartItemData = CartItemHolderData()
        val secondCartItemData = CartItemHolderData().apply {
            cartId = "1"
        }

        coEvery { deleteCartUseCase.setParams(any()) } just Runs
        coEvery { deleteCartUseCase.execute(any(), any()) } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(RemoveFromCartData(status = "OK", data = Data(success = 1)))
        }

        every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(1)

        every { view.checkHitValidateUseIsNeeded(any()) } returns true

        // WHEN
        cartListPresenter.processDeleteCartItem(arrayListOf(firstCartItemData, secondCartItemData),
                arrayListOf(secondCartItemData), false, false)

        // THEN
        verify {
            view.showPromoCheckoutStickyButtonLoading()
        }

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
        cartListPresenter.processDeleteCartItem(arrayListOf(cartItemData), arrayListOf(cartItemData), false, false)

        // THEN
        verify {
            view.showToastMessageRed(throwable)
        }
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
        cartListPresenter.processDeleteCartItem(arrayListOf(cartItemData), arrayListOf(cartItemData), false, true)

        // THEN
        verifyOrder {
            view.reCollapseExpandedDeletedUnavailableItems()
            view.showToastMessageRed(throwable)
        }
    }

    @Test
    fun `WHEN delete all cart item with view is detached THEN should not render view`() {
        // GIVEN
        val cartItemData = CartItemHolderData(cartId = "0")

        cartListPresenter.detachView()

        // WHEN
        cartListPresenter.processDeleteCartItem(arrayListOf(cartItemData), arrayListOf(cartItemData), false, false, true, false)

        // THEN
        verify(inverse = true) {
            view.onDeleteCartDataSuccess(arrayListOf("0"), true, false, false, true, false)
        }
    }

}