package com.tokopedia.cart.view.presenter

import com.tokopedia.promocheckout.common.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import org.junit.Test
import rx.Observable

class ClearPromoTest : BaseCartTest() {

    @Test
    fun `WHEN clear promo success THEN should go to checkout page`() {
        // GIVEN
        val clearPromoModel = ClearPromoUiModel()

        every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just Runs
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(clearPromoModel)

        // WHEN
        cartListPresenter?.doClearRedPromosBeforeGoToCheckout(ArrayList())

        // THEN
        verify {
            view.hideProgressLoading()
            view.onSuccessClearRedPromosThenGoToCheckout()
        }
    }

    @Test
    fun `WHEN clear promo failed THEN should go to checkout page`() {
        // GIVEN
        val exception = CartResponseErrorException("error message")

        every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just Runs
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.error(exception)

        // WHEN
        cartListPresenter?.doClearRedPromosBeforeGoToCheckout(ArrayList())

        // THEN
        verify {
            view.hideProgressLoading()
            view.onSuccessClearRedPromosThenGoToCheckout()
        }
    }
}