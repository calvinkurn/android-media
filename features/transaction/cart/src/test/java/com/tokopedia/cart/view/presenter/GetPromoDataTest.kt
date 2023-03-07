package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.data.model.response.promo.CartPromoData
import com.tokopedia.cart.data.model.response.promo.CartPromoTicker
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import org.junit.Assert.assertEquals
import org.junit.Test
import rx.Observable

class GetPromoDataTest : BaseCartTest() {

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

        coEvery { getCartRevampV3UseCase.setParams(any(), any()) } just Runs
        coEvery { getCartRevampV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartData) -> Unit>().invoke(cartData)
        }

        every { updateCartCounterUseCase.createObservable(any()) } answers { Observable.just(1) }

        // WHEN
        cartListPresenter.processInitialGetCartData("", true, false)

        // THEN
        assertEquals(promoTicker, cartListPresenter.getTickerPromoData())
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

        coEvery { getCartRevampV3UseCase.setParams(any(), any()) } just Runs
        coEvery { getCartRevampV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartData) -> Unit>().invoke(cartData)
        }

        every { updateCartCounterUseCase.createObservable(any()) } answers { Observable.just(1) }

        // WHEN
        cartListPresenter.processInitialGetCartData("", true, false)

        // THEN
        assertEquals(showChoosePromoWidget, cartListPresenter.getShowChoosePromoWidget())
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
        cartListPresenter.setLastValidateUseRequest(validateUseRequest)

        // THEN
        assertEquals(validateUseRequest, cartListPresenter.getLastValidateUseRequest())
    }
}
