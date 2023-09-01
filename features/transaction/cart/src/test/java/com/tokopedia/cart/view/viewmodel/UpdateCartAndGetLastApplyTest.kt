package com.tokopedia.cart.view.viewmodel

import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.cart.domain.model.updatecart.UpdateAndGetLastApplyData
import com.tokopedia.cart.domain.model.updatecart.UpdateCartData
import com.tokopedia.cartrevamp.view.helper.CartDataHelper
import com.tokopedia.cartrevamp.view.uimodel.CartGroupHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.UpdateCartAndGetLastApplyEvent
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import junit.framework.TestCase.assertEquals
import org.junit.Test

class UpdateCartAndGetLastApplyTest : BaseCartViewModelTest() {

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

        every { CartDataHelper.getSelectedCartItemData(any()) } answers { cartItemDataList }
        coEvery { updateCartAndGetLastApplyUseCase(any()) } returns updateAndGetLastApplyData

        // WHEN
        cartViewModel.doUpdateCartAndGetLastApply(ValidateUsePromoRequest())

        // THEN
        assertEquals(
            UpdateCartAndGetLastApplyEvent.Success(updateAndGetLastApplyData.promoUiModel!!),
            cartViewModel.updateCartAndGetLastApplyEvent.value
        )
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

        every { CartDataHelper.getAllShopGroupDataList(any()) } returns listOf(cartGroupHolderData)

        val updateAndGetLastApplyData = UpdateAndGetLastApplyData().apply {
            updateCartData = UpdateCartData().apply {
                isSuccess = true
            }
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(promoCheckoutVoucherOrdersItemUiModel)
            )
        }

        coEvery { updateCartAndGetLastApplyUseCase(any()) } returns updateAndGetLastApplyData
        every { CartDataHelper.getSelectedCartItemData(any()) } answers { cartItemDataList }

        // WHEN
        cartViewModel.doUpdateCartAndGetLastApply(ValidateUsePromoRequest())

        // THEN
        assertEquals(
            UpdateCartAndGetLastApplyEvent.Success(updateAndGetLastApplyData.promoUiModel!!),
            cartViewModel.updateCartAndGetLastApplyEvent.value
        )
        assertEquals(promoCheckoutVoucherOrdersItemUiModel.code, cartGroupHolderData.boCode)
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

        every { CartDataHelper.getSelectedCartItemData(any()) } answers { cartItemDataList }
        coEvery { updateCartAndGetLastApplyUseCase(any()) } throws exception
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()) } returns clearCacheAutoApplyStackUseCase
        coEvery { clearCacheAutoApplyStackUseCase.executeOnBackground() } returns ClearPromoUiModel()

        // WHEN
        cartViewModel.doUpdateCartAndGetLastApply(getLastApplyPromoRequest)

        // THEN
        assertEquals(
            UpdateCartAndGetLastApplyEvent.Failed(exception),
            cartViewModel.updateCartAndGetLastApplyEvent.value
        )
    }

    @Test
    fun `WHEN update and get last apply parameter is empty data THEN should hide loading and did not call api`() {
        // GIVEN
        every { CartDataHelper.getSelectedCartItemData(any()) } answers { emptyList() }

        // WHEN
        cartViewModel.doUpdateCartAndGetLastApply(ValidateUsePromoRequest())

        // THEN
        coVerify(inverse = true) {
            updateCartAndGetLastApplyUseCase.invoke(any())
        }
    }
}
