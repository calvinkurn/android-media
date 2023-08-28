package com.tokopedia.cart.view.viewmodel

import com.tokopedia.cartrevamp.view.uimodel.CartGlobalEvent
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import io.mockk.coEvery
import org.junit.Assert.assertEquals
import org.junit.Test

class ClearPromoTest : BaseCartViewModelTest() {

    @Test
    fun `WHEN clear promo success THEN should go to checkout page`() {
        // GIVEN
        val clearPromoModel = ClearPromoUiModel()

        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()) } returns clearCacheAutoApplyStackUseCase
        coEvery { clearCacheAutoApplyStackUseCase.executeOnBackground() } returns clearPromoModel

        // WHEN
        cartViewModel.doClearRedPromosBeforeGoToCheckout(ClearPromoRequest())

        // THEN
        assertEquals(
            CartGlobalEvent.SuccessClearRedPromosThenGoToCheckout,
            cartViewModel.globalEvent.value
        )
    }

    @Test
    fun `WHEN clear promo failed THEN should go to checkout page`() {
        // GIVEN
        val exception = CartResponseErrorException("error message")

        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()) } returns clearCacheAutoApplyStackUseCase
        coEvery { clearCacheAutoApplyStackUseCase.executeOnBackground() } throws exception

        // WHEN
        cartViewModel.doClearRedPromosBeforeGoToCheckout(ClearPromoRequest())

        // THEN
        assertEquals(
            CartGlobalEvent.SuccessClearRedPromosThenGoToCheckout,
            cartViewModel.globalEvent.value
        )
    }

    @Test
    fun `WHEN clear red promo before go to promo success THEN should go to promo page`() {
        // GIVEN
        val clearPromoModel = ClearPromoUiModel()

        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()) } returns clearCacheAutoApplyStackUseCase
        coEvery { clearCacheAutoApplyStackUseCase.executeOnBackground() } returns clearPromoModel

        // WHEN
        cartViewModel.doClearRedPromosBeforeGoToPromo(ClearPromoRequest())

        // THEN
        assertEquals(
            CartGlobalEvent.SuccessClearRedPromosThenGoToPromo,
            cartViewModel.globalEvent.value
        )
    }

    @Test
    fun `WHEN clear red promo before go to promo failed THEN should go to promo page`() {
        // GIVEN
        val exception = CartResponseErrorException("error message")

        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()) } returns clearCacheAutoApplyStackUseCase
        coEvery { clearCacheAutoApplyStackUseCase.executeOnBackground() } throws exception

        // WHEN
        cartViewModel.doClearRedPromosBeforeGoToPromo(ClearPromoRequest())

        // THEN
        assertEquals(
            CartGlobalEvent.SuccessClearRedPromosThenGoToPromo,
            cartViewModel.globalEvent.value
        )
    }
}
