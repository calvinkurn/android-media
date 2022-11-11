package com.tokopedia.cart.view.presenter

import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoOrderData
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
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

        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just Runs
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(clearPromoModel)

        // WHEN
        cartListPresenter.doClearRedPromosBeforeGoToCheckout(ClearPromoRequest())

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

        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just Runs
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.error(exception)

        // WHEN
        cartListPresenter.doClearRedPromosBeforeGoToCheckout(ClearPromoRequest())

        // THEN
        verify {
            view.hideProgressLoading()
            view.onSuccessClearRedPromosThenGoToCheckout()
        }
    }

    @Test
    fun `WHEN clear promo with view is detached THEN should not render view`() {
        // GIVEN
        val clearPromoModel = ClearPromoUiModel()

        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just Runs
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(clearPromoModel)

        cartListPresenter.detachView()

        // WHEN
        cartListPresenter.doClearRedPromosBeforeGoToCheckout(ClearPromoRequest())

        // THEN
        verify(inverse = true) {
            view.showItemLoading()
        }
    }

    @Test
    fun `WHEN clear all bo THEN should hit clear`() {
        // GIVEN
        val clearPromoModel = ClearPromoUiModel()

        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just Runs
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(clearPromoModel)

        // WHEN
        cartListPresenter.clearAllBo(ClearPromoOrderData())

        // THEN
        verify {
            clearCacheAutoApplyStackUseCase.setParams(any())
        }
    }
}
