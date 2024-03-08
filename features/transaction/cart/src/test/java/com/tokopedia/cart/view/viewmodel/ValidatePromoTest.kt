package com.tokopedia.cart.view.viewmodel

import com.tokopedia.cart.data.model.response.promo.CartPromoData
import com.tokopedia.cart.data.model.response.promo.LastApplyPromo
import com.tokopedia.cart.data.model.response.promo.LastApplyPromoData
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cart.view.helper.CartDataHelper
import com.tokopedia.cart.view.uimodel.CartGroupHolderData
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.uimodel.CartShopHolderData
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ProductDetailsItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class ValidatePromoTest : BaseCartViewModelTest() {

    @Test
    fun `WHEN validate promo with no BO and preapplied BO THEN should clear BO`() {
        // GIVEN
        val shopList = listOf(CartGroupHolderData(boCode = "asdf"), CartGroupHolderData())
        every { CartDataHelper.getAllShopGroupDataList(any()) } returns shopList
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()) } returns clearCacheAutoApplyStackUseCase
        coEvery { clearCacheAutoApplyStackUseCase.executeOnBackground() } returns ClearPromoUiModel()

        // WHEN
        cartViewModel.validateBoPromo(ValidateUsePromoRevampUiModel())

        // THEN
        verify(exactly = 1) {
            clearCacheAutoApplyStackUseCase.setParams(any())
        }
        assertEquals("", shopList[0].boCode)
        assertEquals(false, shopList[0].promoCodes.contains("asdf"))
    }

    @Test
    fun `WHEN validate promo with BO green and preapplied BO THEN should not clear BO`() {
        // GIVEN
        val shopList = listOf(CartGroupHolderData(boCode = "asdf", cartString = "123"), CartGroupHolderData(cartString = "234"))
        every { CartDataHelper.getAllShopGroupDataList(any()) } returns shopList

        // WHEN
        cartViewModel.validateBoPromo(
            ValidateUsePromoRevampUiModel(
                promoUiModel = PromoUiModel(
                    voucherOrderUiModels = listOf(
                        PromoCheckoutVoucherOrdersItemUiModel(
                            shippingId = 1,
                            spId = 1,
                            code = "asdf",
                            type = "logistic",
                            cartStringGroup = "123",
                            messageUiModel = MessageUiModel(
                                state = "green"
                            )
                        )
                    )
                )
            )
        )

        // THEN
        verify(inverse = true) {
            clearCacheAutoApplyStackUseCase.setParams(any())
        }
        assertEquals("asdf", shopList[0].boCode)
    }

    @Test
    fun `WHEN validate promo with invalid BO and preapplied BO THEN should clear BO`() {
        // GIVEN
        val cartGroupHolderData = CartGroupHolderData(
            promoCodes = listOf("TESTBOCODE"),
            warehouseId = 1,
            boMetadata = BoMetadata(),
            isPo = false,
            cartString = "6052760-0-9466960-169751268-KEY_OWOC",
            productUiModelList = mutableListOf(
                CartItemHolderData(
                    isSelected = true,
                    cartStringOrder = "111111-KEY",
                    shopHolderData = CartShopHolderData(
                        shopId = "1",
                        poDuration = "0"
                    ),
                    productId = "1",
                    quantity = 5,
                    bundleId = "0"
                ),
                CartItemHolderData(
                    isSelected = true,
                    cartStringOrder = "111111-KEY",
                    shopHolderData = CartShopHolderData(
                        shopId = "1",
                        poDuration = "0"
                    ),
                    productId = "2",
                    quantity = 5,
                    bundleId = "0"
                ),
                CartItemHolderData(
                    isSelected = true,
                    cartStringOrder = "222222-KEY",
                    shopHolderData = CartShopHolderData(
                        shopId = "2",
                        poDuration = "0"
                    ),
                    productId = "3",
                    quantity = 5,
                    bundleId = "0"
                )
            )
        )
        val secondCartGroupHolderData = CartGroupHolderData(
            promoCodes = listOf("TESTBOCODE"),
            warehouseId = 1,
            boMetadata = BoMetadata(),
            isPo = false,
            boCode = "asdf",
            cartString = "6052760-0-9466960-169751269-KEY_OWOC",
            productUiModelList = mutableListOf(
                CartItemHolderData(
                    isSelected = true,
                    cartStringOrder = "123",
                    shopHolderData = CartShopHolderData(
                        shopId = "1",
                        poDuration = "0"
                    ),
                    productId = "1",
                    quantity = 5,
                    bundleId = "0"
                ),
                CartItemHolderData(
                    isSelected = true,
                    cartStringOrder = "123",
                    shopHolderData = CartShopHolderData(
                        shopId = "1",
                        poDuration = "0"
                    ),
                    productId = "2",
                    quantity = 5,
                    bundleId = "0"
                ),
                CartItemHolderData(
                    isSelected = true,
                    cartStringOrder = "456",
                    shopHolderData = CartShopHolderData(
                        shopId = "2",
                        poDuration = "0"
                    ),
                    productId = "3",
                    quantity = 5,
                    bundleId = "0"
                )
            )
        )
        val shopList = listOf(cartGroupHolderData, secondCartGroupHolderData)
        every { CartDataHelper.getAllShopGroupDataList(any()) } returns shopList
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()) } returns clearCacheAutoApplyStackUseCase
        coEvery { clearCacheAutoApplyStackUseCase.executeOnBackground() } returns ClearPromoUiModel()

        // WHEN
        cartViewModel.validateBoPromo(
            ValidateUsePromoRevampUiModel(
                promoUiModel = PromoUiModel(
                    voucherOrderUiModels = listOf(
                        PromoCheckoutVoucherOrdersItemUiModel(
                            spId = 1,
                            code = "asdf",
                            type = "logistic",
                            uniqueId = "123",
                            cartStringGroup = "6052760-0-9466960-169751269-KEY_OWOC",
                            messageUiModel = MessageUiModel(
                                state = "green"
                            )
                        ),
                        PromoCheckoutVoucherOrdersItemUiModel(
                            shippingId = 1,
                            code = "asdf",
                            type = "logistic",
                            uniqueId = "456",
                            cartStringGroup = "6052760-0-9466960-169751269-KEY_OWOC",
                            messageUiModel = MessageUiModel(
                                state = "green"
                            )
                        ),
                        PromoCheckoutVoucherOrdersItemUiModel(
                            shippingId = 1,
                            spId = 1,
                            code = "asdf",
                            uniqueId = "123",
                            messageUiModel = MessageUiModel(
                                state = "green"
                            )
                        ),
                        PromoCheckoutVoucherOrdersItemUiModel(
                            shippingId = 1,
                            spId = 1,
                            code = "asdf",
                            type = "logistic",
                            uniqueId = "123",
                            messageUiModel = MessageUiModel(
                                state = "red"
                            )
                        ),
                        PromoCheckoutVoucherOrdersItemUiModel(
                            shippingId = 1,
                            spId = 1,
                            code = "asdf",
                            type = "logistic",
                            uniqueId = "321",
                            messageUiModel = MessageUiModel(
                                state = "green"
                            )
                        )
                    )
                )
            )
        )

        // THEN
        verify(exactly = 1) {
            clearCacheAutoApplyStackUseCase.setParams(any())
        }
        assertEquals("", shopList[0].boCode)
        assertEquals(false, shopList[0].promoCodes.contains("asdf"))
    }

    @Test
    fun `WHEN generate param last apply last apply response valid THEN generate validate use from last apply`() {
        // GIVEN
        val lastApplyPromo = LastApplyPromo(
            lastApplyPromoData = LastApplyPromoData(
                codes = listOf("code1", "code2")
            )
        )
        val cartData = CartData(
            promo = CartPromoData(
                lastApplyPromo = lastApplyPromo
            )
        )
        cartViewModel.cartModel.cartListData = cartData
        cartViewModel.cartModel.lastValidateUseResponse = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                codes = listOf("code3", "code4")
            )
        )
        cartViewModel.cartModel.isLastApplyResponseStillValid = true

        // WHEN
        val validateUsePromoRequest = cartViewModel.generateParamGetLastApplyPromo()

        // THEN
        val resultValidateUsePromoRequest = ValidateUsePromoRequest(
            codes = arrayListOf("code1", "code2"),
            state = CartConstant.PARAM_CART,
            skipApply = 0,
            cartType = CartConstant.PARAM_DEFAULT,
            isCartCheckoutRevamp = true
        )
        assertEquals(resultValidateUsePromoRequest, validateUsePromoRequest)
    }

    @Test
    fun `WHEN generate param last apply last validate response not null THEN generate validate use from last validate use response`() {
        // GIVEN
        val lastApplyPromo = LastApplyPromo(
            lastApplyPromoData = LastApplyPromoData(
                codes = listOf("code1", "code2")
            )
        )
        val cartData = CartData(
            promo = CartPromoData(
                lastApplyPromo = lastApplyPromo
            )
        )
        cartViewModel.cartModel.cartListData = cartData
        cartViewModel.cartModel.lastValidateUseResponse = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                codes = listOf("code3", "code4")
            )
        )
        cartViewModel.cartModel.isLastApplyResponseStillValid = false

        // WHEN
        val validateUsePromoRequest = cartViewModel.generateParamGetLastApplyPromo()

        // THEN
        val resultValidateUsePromoRequest = ValidateUsePromoRequest(
            codes = arrayListOf("code3", "code4"),
            state = CartConstant.PARAM_CART,
            skipApply = 0,
            cartType = CartConstant.PARAM_DEFAULT,
            isCartCheckoutRevamp = true
        )
        assertEquals(resultValidateUsePromoRequest, validateUsePromoRequest)
    }

    @Test
    fun `WHEN generate param last apply THEN generate validate use from current selected item`() {
        // GIVEN
        cartViewModel.cartModel.cartListData = CartData()
        cartViewModel.cartModel.lastValidateUseResponse = null
        cartViewModel.cartModel.isLastApplyResponseStillValid = false
        val itemCartOne = CartItemHolderData(
            productId = "1",
            quantity = 1,
            isSelected = true
        )
        val itemCartTwo = CartItemHolderData(
            productId = "2",
            quantity = 1,
            isSelected = true
        )
        val cartGroupHolderData = CartGroupHolderData(
            isAllSelected = true,
            isError = false,
            isCollapsed = true,
            productUiModelList = mutableListOf(
                itemCartOne,
                itemCartTwo
            )
        )
        cartViewModel.cartDataList.value = arrayListOf(cartGroupHolderData)

        // WHEN
        val validateUsePromoRequest = cartViewModel.generateParamGetLastApplyPromo()

        // THEN
        val resultValidateUsePromoRequest = ValidateUsePromoRequest(
            orders = listOf(
                OrdersItem(
                    productDetails = listOf(
                        ProductDetailsItem(
                            productId = 1,
                            quantity = 1
                        ),
                        ProductDetailsItem(
                            productId = 2,
                            quantity = 1
                        )
                    )
                )
            ),
            state = CartConstant.PARAM_CART,
            skipApply = 0,
            cartType = CartConstant.PARAM_DEFAULT,
            isCartCheckoutRevamp = true
        )
        assertEquals(resultValidateUsePromoRequest, validateUsePromoRequest)
    }
}
