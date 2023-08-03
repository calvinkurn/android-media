package com.tokopedia.cart.view.presenter

import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoOrderData
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import io.mockk.coEvery
import io.mockk.verify
import org.junit.Test

class ClearPromoTest : BaseCartTest() {

    @Test
    fun `WHEN clear promo success THEN should go to checkout page`() {
        // GIVEN
        val clearPromoModel = ClearPromoUiModel()

        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()) } returns clearCacheAutoApplyStackUseCase
        coEvery { clearCacheAutoApplyStackUseCase.executeOnBackground() } returns clearPromoModel

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

        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()) } returns clearCacheAutoApplyStackUseCase
        coEvery { clearCacheAutoApplyStackUseCase.executeOnBackground() } throws exception

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

        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()) } returns clearCacheAutoApplyStackUseCase
        coEvery { clearCacheAutoApplyStackUseCase.executeOnBackground() } returns clearPromoModel

        cartListPresenter.detachView()

        // WHEN
        cartListPresenter.doClearRedPromosBeforeGoToCheckout(ClearPromoRequest())

        // THEN
        verify(inverse = true) {
            view.showItemLoading()
        }
    }

    @Test
    fun `WHEN clear red promo before go to promo success THEN should go to promo page`() {
        // GIVEN
        val clearPromoModel = ClearPromoUiModel()

        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()) } returns clearCacheAutoApplyStackUseCase
        coEvery { clearCacheAutoApplyStackUseCase.executeOnBackground() } returns clearPromoModel

        // WHEN
        cartListPresenter.doClearRedPromosBeforeGoToPromo(ClearPromoRequest())

        // THEN
        verify {
            view.hideProgressLoading()
            view.onSuccessClearRedPromosThenGoToPromo()
        }
    }

    @Test
    fun `WHEN clear red promo before go to promo failed THEN should go to promo page`() {
        // GIVEN
        val exception = CartResponseErrorException("error message")

        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()) } returns clearCacheAutoApplyStackUseCase
        coEvery { clearCacheAutoApplyStackUseCase.executeOnBackground() } throws exception

        // WHEN
        cartListPresenter.doClearRedPromosBeforeGoToPromo(ClearPromoRequest())

        // THEN
        verify {
            view.hideProgressLoading()
            view.onSuccessClearRedPromosThenGoToPromo()
        }
    }

    @Test
    fun `WHEN clear red promo before go to promo with view is detached THEN should not render view`() {
        // GIVEN
        val clearPromoModel = ClearPromoUiModel()

        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()) } returns clearCacheAutoApplyStackUseCase
        coEvery { clearCacheAutoApplyStackUseCase.executeOnBackground() } returns clearPromoModel

        cartListPresenter.detachView()

        // WHEN
        cartListPresenter.doClearRedPromosBeforeGoToPromo(ClearPromoRequest())

        // THEN
        verify(inverse = true) {
            view.showItemLoading()
        }
    }

    @Test
    fun `WHEN clear all bo THEN should hit clear`() {
        // GIVEN
        val clearPromoModel = ClearPromoUiModel()

        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()) } returns clearCacheAutoApplyStackUseCase
        coEvery { clearCacheAutoApplyStackUseCase.executeOnBackground() } returns clearPromoModel

        // WHEN
        cartListPresenter.clearAllBo(ClearPromoOrderData())

        // THEN
        verify {
            clearCacheAutoApplyStackUseCase.setParams(any())
        }
    }
}
