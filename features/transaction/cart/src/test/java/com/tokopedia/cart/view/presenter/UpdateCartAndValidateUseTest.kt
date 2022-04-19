package com.tokopedia.cart.view.presenter

import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.cart.domain.model.updatecart.UpdateAndValidateUseData
import com.tokopedia.cart.domain.model.updatecart.UpdateCartData
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import org.junit.Test
import rx.Observable

class UpdateCartAndValidateUseTest : BaseCartTest() {

    @Test
    fun `WHEN update and validate use success THEN should render promo button`() {
        // GIVEN
        val cartItemDataList = ArrayList<CartItemHolderData>().apply {
            add(CartItemHolderData(isError = false))
        }

        val updateAndValidateUseData = UpdateAndValidateUseData().apply {
            updateCartData = UpdateCartData().apply {
                isSuccess = true
            }
            promoUiModel = PromoUiModel()
        }

        every { updateCartAndValidateUseUseCase.createObservable(any()) } returns Observable.just(updateAndValidateUseData)
        every { view.getAllSelectedCartDataList() } answers { cartItemDataList }

        // WHEN
        cartListPresenter.doUpdateCartAndValidateUse(ValidateUsePromoRequest())

        // THEN
        verify {
            view.updatePromoCheckoutStickyButton(updateAndValidateUseData.promoUiModel!!)
        }
    }

    @Test
    fun `WHEN update and validate use failed THEN should render promo button default`() {
        // GIVEN
        val exception = CartResponseErrorException("error message")
        val cartItemDataList = ArrayList<CartItemHolderData>().apply {
            add(CartItemHolderData(isError = false))
        }

        every { view.getAllSelectedCartDataList() } answers { cartItemDataList }
        every { updateCartAndValidateUseUseCase.createObservable(any()) } returns Observable.error(exception)

        // WHEN
        cartListPresenter.doUpdateCartAndValidateUse(ValidateUsePromoRequest())

        // THEN
        verify {
            view.renderPromoCheckoutButtonActiveDefault(emptyList())
        }
    }

    @Test
    fun `WHEN update and validate use failed with akamai exception THEN should clear promo and show error`() {
        // GIVEN
        val exception = AkamaiErrorException("error message")
        val cartItemDataList = ArrayList<CartItemHolderData>().apply {
            add(CartItemHolderData(isError = false))
        }

        every { view.getAllSelectedCartDataList() } answers { cartItemDataList }
        every { updateCartAndValidateUseUseCase.createObservable(any()) } returns Observable.error(exception)
        every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just Runs
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(ClearPromoUiModel())

        // WHEN
        cartListPresenter.doUpdateCartAndValidateUse(ValidateUsePromoRequest())

        // THEN
        verify {
            clearCacheAutoApplyStackUseCase.createObservable(any())
            view.showToastMessageRed(exception)
        }
    }

    @Test
    fun `WHEN update and validate use parameter is empty data THEN should hide loading and did not call api`() {
        // GIVEN
        every { view.getAllSelectedCartDataList() } answers { emptyList() }

        // WHEN
        cartListPresenter.doUpdateCartAndValidateUse(ValidateUsePromoRequest())

        // THEN
        verify(inverse = true) {
            updateCartAndValidateUseUseCase.createObservable(any())
        }
        verify {
            view.hideProgressLoading()
        }
    }

    @Test
    fun `WHEN update and validate with view is detached THEN should not render view`() {
        // GIVEN
        cartListPresenter.detachView()

        // WHEN
        cartListPresenter.doUpdateCartAndValidateUse(ValidateUsePromoRequest())

        // THEN
        verify(inverse = true) {
            view.updatePromoCheckoutStickyButton(any())
        }
    }

}