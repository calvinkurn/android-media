package com.tokopedia.cart.view.presenter

import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import io.mockk.coEvery
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Test

class GetLastApplyTest : BaseCartTest() {

    @Test
    fun `WHEN get last apply success THEN should update promo button state`() {
        // GIVEN
        val lastApplyModel = ValidateUsePromoRevampUiModel().apply {
            promoUiModel = PromoUiModel()
        }

        coEvery { getLastApplyPromoUseCase(any()) } returns lastApplyModel

        // WHEN
        cartListPresenter.doGetLastApply(ValidateUsePromoRequest())

        // THEN
        verify {
            view.updatePromoCheckoutStickyButton(lastApplyModel.promoUiModel)
        }
    }

    @Test
    fun `WHEN get last apply failed THEN should update promo button state to inactive`() {
        // GIVEN
        val exception = CartResponseErrorException("error message")

        coEvery { getLastApplyPromoUseCase(any()) } throws exception

        // WHEN
        cartListPresenter.doGetLastApply(ValidateUsePromoRequest())

        // THEN
        verify {
            view.showPromoCheckoutStickyButtonInactive()
        }
    }

    @Test
    fun `WHEN get last apply failed THEN should show error and update promo button state to inactive`() {
        // GIVEN
        val exception = AkamaiErrorException("error message")

        coEvery { getLastApplyPromoUseCase(any()) } throws exception

        // WHEN
        cartListPresenter.doGetLastApply(ValidateUsePromoRequest())

        // THEN
        verifyOrder {
            view.showToastMessageRed(exception)
            view.showPromoCheckoutStickyButtonInactive()
        }
    }
}
