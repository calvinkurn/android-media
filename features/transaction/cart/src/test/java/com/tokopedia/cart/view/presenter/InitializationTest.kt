package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.data.model.response.promo.CartPromoData
import com.tokopedia.cart.data.model.response.promo.CartPromoTicker
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cart.domain.model.updatecart.UpdateAndGetLastApplyData
import com.tokopedia.cart.domain.model.updatecart.UpdateCartData
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import org.junit.Assert
import org.junit.Test

class InitializationTest : BaseCartTest() {

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
        cartListPresenter.setCartListData(cartListData)

        // Then
        Assert.assertEquals(cartListPresenter.getCartListData(), cartListData)
    }

    @Test
    fun `WHEN last validate use response is updated THEN current value whould be updated accordingly`() {
        // Given
        val lastValidateUseResponse = ValidateUsePromoRevampUiModel(
            code = "123",
            promoUiModel = PromoUiModel(codes = arrayListOf("234", "345"))
        )

        // When
        cartListPresenter.setValidateUseLastResponse(lastValidateUseResponse)

        // Then
        Assert.assertEquals(cartListPresenter.getValidateUseLastResponse(), lastValidateUseResponse)
    }

    @Test
    fun `WHEN last update cart and validate use response is updated THEN current value should be updated accordingly`() {
        // Given
        val lastUpdateAndValidateUseData = UpdateAndGetLastApplyData(
            updateCartData = UpdateCartData(isSuccess = true),
            promoUiModel = PromoUiModel(codes = arrayListOf("123", "456"))
        )

        // When
        cartListPresenter.setUpdateCartAndGetLastApplyLastResponse(lastUpdateAndValidateUseData)

        // Then
        Assert.assertEquals(
            cartListPresenter.getUpdateCartAndGetLastApplyLastResponse(),
            lastUpdateAndValidateUseData
        )
    }
}
