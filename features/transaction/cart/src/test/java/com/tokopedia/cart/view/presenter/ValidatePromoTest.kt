package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.view.uimodel.CartShopHolderData
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import rx.Observable

class ValidatePromoTest : BaseCartTest() {

    @Test
    fun `WHEN validate promo with detached view THEN should not clear BO`() {
        // GIVEN
        val shopList = listOf(CartShopHolderData(boCode = "asdf"), CartShopHolderData())
        every { view.getAllShopDataList() } returns shopList
        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just Runs
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(ClearPromoUiModel())

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
        val shopList = listOf(CartShopHolderData(boCode = "asdf"), CartShopHolderData())
        every { view.getAllShopDataList() } returns shopList
        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just Runs
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(ClearPromoUiModel())

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
        val shopList = listOf(CartShopHolderData(boCode = "asdf", cartString = "123"), CartShopHolderData(cartString = "234"))
        every { view.getAllShopDataList() } returns shopList

        // WHEN
        cartListPresenter.validateBoPromo(ValidateUsePromoRevampUiModel(promoUiModel = PromoUiModel(
            voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(
                    shippingId = 1,
                    spId = 1,
                    code = "asdf",
                    type = "logistic",
                    uniqueId = "123",
                    messageUiModel = MessageUiModel(
                        state = "green"
                    )
                ),
            )
        )))

        // THEN
        verify(inverse = true) {
            clearCacheAutoApplyStackUseCase.setParams(any())
        }
        assertEquals("asdf", shopList[0].boCode)
    }

    @Test
    fun `WHEN validate promo with invalid BO and preapplied BO THEN should clear BO`() {
        // GIVEN
        val shopList = listOf(CartShopHolderData(boCode = "asdf", cartString = "123"), CartShopHolderData(cartString = "234"))
        every { view.getAllShopDataList() } returns shopList
        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just Runs
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(ClearPromoUiModel())

        // WHEN
        cartListPresenter.validateBoPromo(ValidateUsePromoRevampUiModel(promoUiModel = PromoUiModel(
            voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(
                    spId = 1,
                    code = "asdf",
                    type = "logistic",
                    uniqueId = "123",
                    messageUiModel = MessageUiModel(
                        state = "green"
                    )
                ),
                PromoCheckoutVoucherOrdersItemUiModel(
                    shippingId = 1,
                    code = "asdf",
                    type = "logistic",
                    uniqueId = "123",
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
                ),
            )
        )))

        // THEN
        verify(exactly = 1) {
            clearCacheAutoApplyStackUseCase.setParams(any())
        }
        assertEquals("", shopList[0].boCode)
        assertEquals(false, shopList[0].promoCodes.contains("asdf"))
    }
}
