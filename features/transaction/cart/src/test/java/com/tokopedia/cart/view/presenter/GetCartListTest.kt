package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.data.model.response.promo.CartPromoData
import com.tokopedia.cart.data.model.response.promo.LastApplyPromo
import com.tokopedia.cart.data.model.response.promo.LastApplyPromoData
import com.tokopedia.cart.data.model.response.promo.VoucherOrders
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.network.exception.ResponseErrorException
import io.mockk.*
import org.junit.Test
import rx.Observable

class GetCartListTest : BaseCartTest() {

    @Test
    fun `WHEN initial load cart list success THEN should render success`() {
        // GIVEN
        val cartData = CartData()

        coEvery { getCartRevampV3UseCase.setParams(any(), any()) } just Runs
        coEvery { getCartRevampV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartData) -> Unit>().invoke(cartData)
        }

        every { updateCartCounterUseCase.createObservable(any()) } answers { Observable.just(1) }

        // WHEN
        cartListPresenter.processInitialGetCartData("", true, false)

        // THEN
        verifyOrder {
            view.renderLoadGetCartDataFinish()
            view.renderInitialGetCartListDataSuccess(cartData)
            view.stopCartPerformanceTrace()
        }
    }

    @Test
    fun `WHEN initial load cart list failed THEN should render error`() {
        // GIVEN
        val exception = ResponseErrorException("Terjadi kesalahan pada server. Ulangi beberapa saat lagi")

        coEvery { getCartRevampV3UseCase.setParams(any(), any()) } just Runs
        coEvery { getCartRevampV3UseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(exception)
        }

        every { updateCartCounterUseCase.createObservable(any()) } answers { Observable.just(1) }

        // WHEN
        cartListPresenter.processInitialGetCartData("", true, true)

        // THEN
        verify {
            view.renderErrorInitialGetCartListData(exception)
            view.stopCartPerformanceTrace()
        }
    }

    @Test
    fun `WHEN reload cart list success THEN should render success`() {
        // GIVEN
        val cartData = CartData()

        coEvery { getCartRevampV3UseCase.setParams(any(), any()) } just Runs
        coEvery { getCartRevampV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartData) -> Unit>().invoke(cartData)
        }

        every { updateCartCounterUseCase.createObservable(any()) } answers { Observable.just(1) }

        // WHEN
        cartListPresenter.processInitialGetCartData("", false, false)

        // THEN
        verify {
            view.renderLoadGetCartDataFinish()
            view.renderInitialGetCartListDataSuccess(cartData)
            view.stopCartPerformanceTrace()
        }
    }

    @Test
    fun `WHEN reload cart list failed THEN should render error`() {
        // GIVEN
        val exception = ResponseErrorException("Terjadi kesalahan pada server. Ulangi beberapa saat lagi")

        coEvery { getCartRevampV3UseCase.setParams(any(), any()) } just Runs
        coEvery { getCartRevampV3UseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(exception)
        }

        every { updateCartCounterUseCase.createObservable(any()) } answers { Observable.just(1) }

        // WHEN
        cartListPresenter.processInitialGetCartData("", false, true)

        // THEN
        verify {
            view.renderErrorInitialGetCartListData(exception)
            view.stopCartPerformanceTrace()
        }
    }

    @Test
    fun `WHEN initial load cart list success with promo last apply data THEN should render success`() {
        // GIVEN
        val cartData = CartData(promo = CartPromoData(lastApplyPromo = LastApplyPromo(lastApplyPromoData = LastApplyPromoData(codes = listOf("ABC"), listVoucherOrders = listOf(VoucherOrders())))))

        coEvery { getCartRevampV3UseCase.setParams(any(), any()) } just Runs
        coEvery { getCartRevampV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartData) -> Unit>().invoke(cartData)
        }

        every { updateCartCounterUseCase.createObservable(any()) } answers { Observable.just(1) }

        // WHEN
        cartListPresenter.processInitialGetCartData("", true, false)

        // THEN
        verifyOrder {
            view.renderLoadGetCartDataFinish()
            view.renderInitialGetCartListDataSuccess(cartData)
            view.stopCartPerformanceTrace()
        }
    }

    @Test
    fun `WHEN initial load cart list success with promo last apply data THEN is last apply valid flag should be true`() {
        // GIVEN
        val cartData = CartData(promo = CartPromoData(lastApplyPromo = LastApplyPromo(lastApplyPromoData = LastApplyPromoData(codes = listOf("ABC"), listVoucherOrders = listOf(VoucherOrders())))))

        coEvery { getCartRevampV3UseCase.setParams(any(), any()) } just Runs
        coEvery { getCartRevampV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartData) -> Unit>().invoke(cartData)
        }

        every { updateCartCounterUseCase.createObservable(any()) } answers { Observable.just(1) }

        // WHEN
        cartListPresenter.processInitialGetCartData("", true, false)

        // THEN
        assert(cartListPresenter.isLastApplyValid() == true)
    }

    @Test
    fun `WHEN initial load cart list with view is not attached THEN should not render view`() {
        // GIVEN
        val cartData = CartData()

        cartListPresenter.detachView()

        // WHEN
        cartListPresenter.processInitialGetCartData("", true, false)

        // THEN
        verify(inverse = true) {
            view.renderInitialGetCartListDataSuccess(cartData)
        }
    }

}