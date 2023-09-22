package com.tokopedia.cart.view.viewmodel

import com.tokopedia.cart.data.model.response.promo.CartPromoData
import com.tokopedia.cart.data.model.response.promo.LastApplyPromo
import com.tokopedia.cart.data.model.response.promo.LastApplyPromoData
import com.tokopedia.cart.data.model.response.promo.PromoAdditionalInfo
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.AdditionalInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import org.junit.Assert.assertEquals
import org.junit.Test

class GetPromoFlagTest : BaseCartViewModelTest() {

    @Test
    fun `WHEN get promo flag with last apply valid THEN should return last apply poml auto applied flag`() {
        // GIVEN
        val pomlAutoApplied = true
        cartViewModel.cartModel.apply {
            cartListData = CartData(
                promo = CartPromoData(
                    lastApplyPromo = LastApplyPromo(
                        lastApplyPromoData = LastApplyPromoData(
                            additionalInfo = PromoAdditionalInfo(
                                pomlAutoApplied = pomlAutoApplied
                            )
                        )
                    )
                )
            )
            isLastApplyResponseStillValid = true
        }

        // WHEN
        val promoFlag = cartViewModel.getPromoFlag()

        // THEN
        assertEquals(pomlAutoApplied, promoFlag)
    }

    @Test
    fun `WHEN get promo flag with last apply valid and null cart data THEN should return false`() {
        // GIVEN
        cartViewModel.cartModel.isLastApplyResponseStillValid = true

        // WHEN
        val promoFlag = cartViewModel.getPromoFlag()

        // THEN
        assertEquals(false, promoFlag)
    }

    @Test
    fun `WHEN get promo flag with last apply invalid THEN should return last validate use response poml auto applied flag`() {
        // GIVEN
        val pomlAutoApplied = true
        cartViewModel.cartModel.apply {
            lastValidateUseResponse = ValidateUsePromoRevampUiModel(
                promoUiModel = PromoUiModel(
                    additionalInfoUiModel = AdditionalInfoUiModel(
                        pomlAutoApplied = pomlAutoApplied
                    )
                )
            )
            isLastApplyResponseStillValid = false
        }

        // WHEN
        val promoFlag = cartViewModel.getPromoFlag()

        // THEN
        assertEquals(pomlAutoApplied, promoFlag)
    }

    @Test
    fun `WHEN get promo flag with last apply invalid and last validate use response null THEN should return false`() {
        // GIVEN
        cartViewModel.cartModel.isLastApplyResponseStillValid = false

        // WHEN
        val promoFlag = cartViewModel.getPromoFlag()

        // THEN
        assertEquals(false, promoFlag)
    }
}
