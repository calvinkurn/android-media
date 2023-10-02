package com.tokopedia.cart.view.viewmodel

import com.tokopedia.cart.data.model.response.promo.CartPromoData
import com.tokopedia.cart.data.model.response.promo.LastApplyPromo
import com.tokopedia.cart.data.model.response.promo.LastApplyPromoData
import com.tokopedia.cart.data.model.response.promo.VoucherOrders
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cartrevamp.view.uimodel.CartState
import com.tokopedia.network.exception.ResponseErrorException
import io.mockk.coEvery
import io.mockk.every
import org.junit.Assert.assertEquals
import org.junit.Test
import rx.Observable

class GetCartListTest : BaseCartViewModelTest() {

    @Test
    fun `WHEN initial load cart list success THEN should render success`() {
        // GIVEN
        val cartData = CartData()

        coEvery { getCartRevampV4UseCase(any()) } returns cartData

        every { updateCartCounterUseCase.createObservable(any()) } answers { Observable.just(1) }

        // WHEN
        cartViewModel.processInitialGetCartData(
            cartId = "",
            initialLoad = true,
            isLoadingTypeRefresh = false
        )

        // THEN
        assertEquals(CartState.Success(cartData), cartViewModel.loadCartState.value)
    }

    @Test
    fun `WHEN initial load cart list failed THEN should render error`() {
        // GIVEN
        val exception =
            ResponseErrorException("Terjadi kesalahan pada server. Ulangi beberapa saat lagi")

        coEvery { getCartRevampV4UseCase(any()) } throws exception

        every { updateCartCounterUseCase.createObservable(any()) } answers { Observable.just(1) }

        // WHEN
        cartViewModel.processInitialGetCartData(
            cartId = "",
            initialLoad = true,
            isLoadingTypeRefresh = true
        )

        // THEN
        assertEquals(CartState.Failed(exception), cartViewModel.loadCartState.value)
    }

    @Test
    fun `WHEN reload cart list success THEN should render success`() {
        // GIVEN
        val cartData = CartData()

        coEvery { getCartRevampV4UseCase(any()) } returns cartData

        every { updateCartCounterUseCase.createObservable(any()) } answers { Observable.just(1) }

        // WHEN
        cartViewModel.processInitialGetCartData(
            cartId = "",
            initialLoad = false,
            isLoadingTypeRefresh = false
        )

        // THEN
        assertEquals(CartState.Success(cartData), cartViewModel.loadCartState.value)
    }

    @Test
    fun `WHEN reload cart list failed THEN should render error`() {
        // GIVEN
        val exception =
            ResponseErrorException("Terjadi kesalahan pada server. Ulangi beberapa saat lagi")

        coEvery { getCartRevampV4UseCase(any()) } throws exception

        every { updateCartCounterUseCase.createObservable(any()) } answers { Observable.just(1) }

        // WHEN
        cartViewModel.processInitialGetCartData(
            cartId = "",
            initialLoad = false,
            isLoadingTypeRefresh = true
        )

        // THEN
        assertEquals(CartState.Failed(exception), cartViewModel.loadCartState.value)
    }

    @Test
    fun `WHEN initial load cart list success with promo last apply data THEN should render success`() {
        // GIVEN
        val cartData = CartData(
            promo = CartPromoData(
                lastApplyPromo = LastApplyPromo(
                    lastApplyPromoData = LastApplyPromoData(
                        codes = listOf("ABC"),
                        listVoucherOrders = listOf(VoucherOrders())
                    )
                )
            )
        )

        coEvery { getCartRevampV4UseCase(any()) } returns cartData

        every { updateCartCounterUseCase.createObservable(any()) } answers { Observable.just(1) }

        // WHEN
        cartViewModel.processInitialGetCartData(
            cartId = "",
            initialLoad = true,
            isLoadingTypeRefresh = false
        )

        // THEN
        assertEquals(CartState.Success(cartData), cartViewModel.loadCartState.value)
    }

    @Test
    fun `WHEN initial load cart list success with promo last apply data THEN is last apply valid flag should be true`() {
        // GIVEN
        val cartData = CartData(
            promo = CartPromoData(
                lastApplyPromo = LastApplyPromo(
                    lastApplyPromoData = LastApplyPromoData(
                        codes = listOf("ABC"),
                        listVoucherOrders = listOf(VoucherOrders())
                    )
                )
            )
        )

        coEvery { getCartRevampV4UseCase(any()) } returns cartData

        every { updateCartCounterUseCase.createObservable(any()) } answers { Observable.just(1) }

        // WHEN
        cartViewModel.processInitialGetCartData(
            cartId = "",
            initialLoad = true,
            isLoadingTypeRefresh = false
        )

        // THEN
        assert(cartViewModel.cartModel.isLastApplyResponseStillValid)
    }
}
