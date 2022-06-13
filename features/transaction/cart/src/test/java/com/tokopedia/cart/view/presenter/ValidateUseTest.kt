package com.tokopedia.cart.view.presenter

import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import io.mockk.every
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Test
import rx.Observable

class ValidateUseTest : BaseCartTest() {

    @Test
    fun `WHEN validate use success THEN should update promo button state`() {
        // GIVEN
        val validateUseModel = ValidateUsePromoRevampUiModel().apply {
            promoUiModel = PromoUiModel()
        }

        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(validateUseModel)

        // WHEN
        cartListPresenter.doValidateUse(ValidateUsePromoRequest())

        // THEN
        verify {
            view.updatePromoCheckoutStickyButton(validateUseModel.promoUiModel)
        }
    }

    @Test
    fun `WHEN validate use failed THEN should update promo button state to inactive`() {
        // GIVEN
        val exception = CartResponseErrorException("error message")

        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.error(exception)

        // WHEN
        cartListPresenter.doValidateUse(ValidateUsePromoRequest())

        // THEN
        verify {
            view.showPromoCheckoutStickyButtonInactive()
        }
    }

    @Test
    fun `WHEN validate use failed THEN should show error and update promo button state to inactive`() {
        // GIVEN
        val exception = AkamaiErrorException("error message")

        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.error(exception)

        // WHEN
        cartListPresenter.doValidateUse(ValidateUsePromoRequest())

        // THEN
        verifyOrder {
            view.showToastMessageRed(exception)
            view.showPromoCheckoutStickyButtonInactive()
        }
    }
}