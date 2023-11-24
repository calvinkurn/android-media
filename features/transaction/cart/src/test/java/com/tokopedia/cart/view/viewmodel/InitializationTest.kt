package com.tokopedia.cart.view.viewmodel

import com.tokopedia.cart.data.model.response.promo.CartPromoData
import com.tokopedia.cart.data.model.response.promo.CartPromoTicker
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cart.domain.model.updatecart.UpdateAndGetLastApplyData
import com.tokopedia.cart.domain.model.updatecart.UpdateCartData
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import org.junit.Assert.assertEquals
import org.junit.Test

class InitializationTest : BaseCartViewModelTest() {

    @Test
    fun `WHEN cart list data is updated THEN current value whould be updated accordingly`() {
        // Given
        val cartListData = CartData(
            promo = CartPromoData(
                ticker = CartPromoTicker(
                    enable = true
                )
            )
        )

        // When
        cartViewModel.cartModel.cartListData = cartListData

        // Then
        assertEquals(cartViewModel.cartModel.cartListData, cartListData)
    }

    @Test
    fun `WHEN last validate use response is updated THEN current value whould be updated accordingly`() {
        // Given
        val lastValidateUseResponse = ValidateUsePromoRevampUiModel(
            code = "123",
            promoUiModel = PromoUiModel(codes = arrayListOf("234", "345"))
        )

        // When
        cartViewModel.cartModel.lastValidateUseResponse = lastValidateUseResponse

        // Then
        assertEquals(cartViewModel.cartModel.lastValidateUseResponse, lastValidateUseResponse)
    }

    @Test
    fun `WHEN last update cart and validate use response is updated THEN current value should be updated accordingly`() {
        // Given
        val lastUpdateAndValidateUseData = UpdateAndGetLastApplyData(
            updateCartData = UpdateCartData(isSuccess = true),
            promoUiModel = PromoUiModel(codes = arrayListOf("123", "456"))
        )

        // When
        cartViewModel.cartModel.lastUpdateCartAndGetLastApplyResponse = lastUpdateAndValidateUseData

        // Then
        assertEquals(
            cartViewModel.cartModel.lastUpdateCartAndGetLastApplyResponse,
            lastUpdateAndValidateUseData
        )
    }
}
