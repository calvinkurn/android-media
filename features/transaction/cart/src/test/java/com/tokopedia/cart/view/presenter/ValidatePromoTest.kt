package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.view.uimodel.CartGroupHolderData
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.uimodel.CartShopHolderData
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata
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

class ValidatePromoTest : BaseCartTest() {

    @Test
    fun `WHEN validate promo with detached view THEN should not clear BO`() {
        // GIVEN
        val shopList = listOf(CartGroupHolderData(boCode = "asdf"), CartGroupHolderData())
        every { view.getAllGroupDataList() } returns shopList
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()) } returns clearCacheAutoApplyStackUseCase
        coEvery { clearCacheAutoApplyStackUseCase.executeOnBackground() } returns ClearPromoUiModel()

        // WHEN
        cartListPresenter.detachView()
        cartListPresenter.validateBoPromo(ValidateUsePromoRevampUiModel())

        // THEN
        verify(inverse = true) {
            clearCacheAutoApplyStackUseCase.setParams(any())
        }
        assertEquals("asdf", shopList[0].boCode)
    }

    @Test
    fun `WHEN validate promo with no BO and preapplied BO THEN should clear BO`() {
        // GIVEN
        val shopList = listOf(CartGroupHolderData(boCode = "asdf"), CartGroupHolderData())
        every { view.getAllGroupDataList() } returns shopList
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()) } returns clearCacheAutoApplyStackUseCase
        coEvery { clearCacheAutoApplyStackUseCase.executeOnBackground() } returns ClearPromoUiModel()

        // WHEN
        cartListPresenter.validateBoPromo(ValidateUsePromoRevampUiModel())

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
        every { view.getAllGroupDataList() } returns shopList

        // WHEN
        cartListPresenter.validateBoPromo(
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
        every { view.getAllGroupDataList() } returns shopList
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()) } returns clearCacheAutoApplyStackUseCase
        coEvery { clearCacheAutoApplyStackUseCase.executeOnBackground() } returns ClearPromoUiModel()

        // WHEN
        cartListPresenter.validateBoPromo(
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
}
