package com.tokopedia.checkout.view.presenter

import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import org.junit.Assert.assertEquals
import org.junit.Test

class ShipmentPresenterPromoTest : BaseShipmentPresenterTest() {

    @Test
    fun `WHEN update promo checkout data THEN should set last apply data correctly`() {
        // Given
        val promoUiModel = PromoUiModel(codes = listOf("code"))

        // When
        presenter.updatePromoCheckoutData(promoUiModel)

        // Then
        assertEquals(promoUiModel.codes, presenter.lastApplyData.value.codes)
    }

    @Test
    fun `WHEN reset promo checkout data THEN should reset last apply data correctly`() {
        // Given
        val promoUiModel = PromoUiModel(codes = listOf("code"))
        presenter.updatePromoCheckoutData(promoUiModel)

        // When
        presenter.resetPromoCheckoutData()

        // Then
        assertEquals(0, presenter.lastApplyData.value.codes.size)
    }
}
