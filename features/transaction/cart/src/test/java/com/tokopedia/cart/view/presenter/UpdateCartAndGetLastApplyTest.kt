package com.tokopedia.cart.view.presenter

import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.cart.domain.model.updatecart.UpdateAndGetLastApplyData
import com.tokopedia.cart.domain.model.updatecart.UpdateCartData
import com.tokopedia.cart.view.uimodel.CartGroupHolderData
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.Test

class UpdateCartAndGetLastApplyTest : BaseCartTest() {

    @Test
    fun `WHEN update and get last apply success THEN should render promo button`() {
        // GIVEN
        val cartItemDataList = ArrayList<CartItemHolderData>().apply {
            add(CartItemHolderData(isError = false))
        }

        val updateAndGetLastApplyData = UpdateAndGetLastApplyData().apply {
            updateCartData = UpdateCartData().apply {
                isSuccess = true
            }
            promoUiModel = PromoUiModel()
        }

        coEvery { updateCartAndGetLastApplyUseCase(any()) } returns updateAndGetLastApplyData
        every { view.getAllSelectedCartDataList() } answers { cartItemDataList }

        // WHEN
        cartListPresenter.doUpdateCartAndGetLastApply(ValidateUsePromoRequest())

        // THEN
        verify {
            view.updatePromoCheckoutStickyButton(updateAndGetLastApplyData.promoUiModel!!)
        }
    }

    @Test
    fun `WHEN update and get last apply with BO success THEN should sync group shop bo code`() {
        // GIVEN
        val cartItemDataList = ArrayList<CartItemHolderData>().apply {
            add(CartItemHolderData(isError = false))
        }
        val cartGroupHolderData = CartGroupHolderData(
            cartString = "_-0-9466960-169751269-KEY_OWOC",
            boCode = "TESTBOCODE"
        )
        val promoCheckoutVoucherOrdersItemUiModel = PromoCheckoutVoucherOrdersItemUiModel(
            shippingId = 1,
            spId = 1,
            type = "logistic",
            messageUiModel = MessageUiModel(state = "green"),
            cartStringGroup = "_-0-9466960-169751269-KEY_OWOC",
            code = "NEWBOCODE"
        )

        every { view.getAllGroupDataList() } returns listOf(cartGroupHolderData)

        val updateAndGetLastApplyData = UpdateAndGetLastApplyData().apply {
            updateCartData = UpdateCartData().apply {
                isSuccess = true
            }
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(promoCheckoutVoucherOrdersItemUiModel)
            )
        }

        coEvery { updateCartAndGetLastApplyUseCase(any()) } returns updateAndGetLastApplyData
        every { view.getAllSelectedCartDataList() } answers { cartItemDataList }

        // WHEN
        cartListPresenter.doUpdateCartAndGetLastApply(ValidateUsePromoRequest())

        // THEN
        verify {
            view.updatePromoCheckoutStickyButton(updateAndGetLastApplyData.promoUiModel!!)
        }

        assertEquals(promoCheckoutVoucherOrdersItemUiModel.code, cartGroupHolderData.boCode)
    }

    @Test
    fun `WHEN update and get last apply failed THEN should render promo button default`() {
        // GIVEN
        val exception = CartResponseErrorException("error message")
        val cartItemDataList = ArrayList<CartItemHolderData>().apply {
            add(CartItemHolderData(isError = false))
        }

        every { view.getAllSelectedCartDataList() } answers { cartItemDataList }
        coEvery { updateCartAndGetLastApplyUseCase(any()) } throws exception

        // WHEN
        cartListPresenter.doUpdateCartAndGetLastApply(ValidateUsePromoRequest())

        // THEN
        verify {
            view.renderPromoCheckoutButtonActiveDefault(emptyList())
        }
    }

    @Test
    fun `WHEN update and get last apply failed with akamai exception THEN should clear promo and show error`() {
        // GIVEN
        val exception = AkamaiErrorException("error message")
        val cartItemDataList = ArrayList<CartItemHolderData>().apply {
            add(CartItemHolderData(isError = false))
        }
        val getLastApplyPromoRequest = ValidateUsePromoRequest(
            orders = listOf(
                OrdersItem(),
                OrdersItem()
            )
        )

        every { view.getAllSelectedCartDataList() } answers { cartItemDataList }
        coEvery { updateCartAndGetLastApplyUseCase(any()) } throws exception
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()) } returns clearCacheAutoApplyStackUseCase
        coEvery { clearCacheAutoApplyStackUseCase.executeOnBackground() } returns ClearPromoUiModel()

        // WHEN
        cartListPresenter.doUpdateCartAndGetLastApply(getLastApplyPromoRequest)

        // THEN
        coVerify {
            clearCacheAutoApplyStackUseCase.executeOnBackground()
        }
        verify {
            view.showToastMessageRed(exception)
        }
    }

    @Test
    fun `WHEN update and get last apply parameter is empty data THEN should hide loading and did not call api`() {
        // GIVEN
        every { view.getAllSelectedCartDataList() } answers { emptyList() }

        // WHEN
        cartListPresenter.doUpdateCartAndGetLastApply(ValidateUsePromoRequest())

        // THEN
        coVerify(inverse = true) {
            updateCartAndGetLastApplyUseCase.invoke(any())
        }
        verify {
            view.hideProgressLoading()
        }
    }

    @Test
    fun `WHEN update and get last apply with view is detached THEN should not render view`() {
        // GIVEN
        cartListPresenter.detachView()

        // WHEN
        cartListPresenter.doUpdateCartAndGetLastApply(ValidateUsePromoRequest())

        // THEN
        verify(inverse = true) {
            view.updatePromoCheckoutStickyButton(any())
        }
    }
}
