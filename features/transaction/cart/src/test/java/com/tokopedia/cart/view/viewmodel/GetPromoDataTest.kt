package com.tokopedia.cart.view.viewmodel

import com.tokopedia.cart.data.model.response.promo.CartPromoData
import com.tokopedia.cart.data.model.response.promo.CartPromoTicker
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import io.mockk.coEvery
import io.mockk.every
import org.junit.Assert.assertEquals
import org.junit.Test
import rx.Observable

class GetPromoDataTest : BaseCartViewModelTest() {

    @Test
    fun `WHEN load cart with cart promo ticker THEN should set ticker promo data`() {
        // GIVEN
        val promoTicker = CartPromoTicker(
            enable = true,
            text = "text",
            iconUrl = "url"
        )
        val cartData = CartData(
            promo = CartPromoData(
                ticker = promoTicker
            )
        )

        coEvery { getCartRevampV4UseCase(any()) } returns cartData

        every { updateCartCounterUseCase.createObservable(any()) } answers { Observable.just(1) }

        // WHEN
        cartViewModel.processInitialGetCartData("", true, false)

        // THEN
        assertEquals(promoTicker, cartViewModel.cartModel.promoTicker)
    }

    @Test
    fun `WHEN load cart THEN should set flag show choose promo widget`() {
        // GIVEN
        val showChoosePromoWidget = true
        val cartData = CartData(
            promo = CartPromoData(
                showChoosePromoWidget = showChoosePromoWidget
            )
        )

        coEvery { getCartRevampV4UseCase(any()) } returns cartData

        every { updateCartCounterUseCase.createObservable(any()) } answers { Observable.just(1) }

        // WHEN
        cartViewModel.processInitialGetCartData("", true, false)

        // THEN
        assertEquals(showChoosePromoWidget, cartViewModel.cartModel.showChoosePromoWidget)
    }

    @Test
    fun `WHEN set last validate use request THEN should return the same last validate use request`() {
        // GIVEN
        val validateUseRequest = ValidateUsePromoRequest(
            cartType = "testing",
            state = "test",
            codes = arrayListOf(""),
            orders = listOf(OrdersItem())
        )

        // WHEN
        cartViewModel.cartModel.lastValidateUseRequest = validateUseRequest

        // THEN
        assertEquals(validateUseRequest, cartViewModel.cartModel.lastValidateUseRequest)
    }
}
