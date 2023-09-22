package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.data.model.response.promo.LastApplyPromo
import com.tokopedia.cart.view.viewmodel.BaseCartViewModelTest
import com.tokopedia.cartrevamp.view.mapper.PromoRequestMapper
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GetLastApplyParamTest : BaseCartViewModelTest() {

    override fun setUp() {
        super.setUp()
        mockkObject(PromoRequestMapper)
    }

    @Test
    fun `WHEN lastApply is valid THEN generateParamGetLastApplyPromo should use lastApply`() {
        // GIVEN
        val argumentSlot = slot<Any>()
        cartViewModel.cartModel.isLastApplyResponseStillValid = true
        cartViewModel.cartModel.cartListData?.promo?.lastApplyPromo = LastApplyPromo()
        every {
            PromoRequestMapper.generateGetLastApplyRequestParams(
                capture(argumentSlot),
                any(),
                any()
            )
        } answers { callOriginal() }

        // WHEN
        cartViewModel.generateParamGetLastApplyPromo()

        // THEN
        assertTrue(argumentSlot.captured is LastApplyPromo)
    }

    @Test
    fun `WHEN lastApply not valid and lastValidateUseResponse not null THEN generateParamGetLastApplyPromo should use PromoUiModel`() {
        // GIVEN
        val argumentSlot = slot<Any>()
        cartViewModel.cartModel.isLastApplyResponseStillValid = false
        cartViewModel.cartModel.lastValidateUseResponse = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel()
        )
        every {
            PromoRequestMapper.generateGetLastApplyRequestParams(
                capture(argumentSlot),
                any(),
                any()
            )
        } answers { callOriginal() }

        // WHEN
        cartViewModel.generateParamGetLastApplyPromo()

        // THEN
        assertTrue(argumentSlot.captured is PromoUiModel)
    }

    @Test
    fun `WHEN lastApply not valid and lastValidateUseResponse is null THEN generateParamGetLastApplyPromo should null`() {
        // GIVEN
        val nullableArgumentSlot = mutableListOf<Any?>()
        cartViewModel.cartModel.isLastApplyResponseStillValid = false
        cartViewModel.cartModel.lastValidateUseResponse = null
        every {
            PromoRequestMapper.generateGetLastApplyRequestParams(
                captureNullable(nullableArgumentSlot),
                any(),
                any()
            )
        } answers { callOriginal() }

        // WHEN
        cartViewModel.generateParamGetLastApplyPromo()

        // THEN
        assertEquals(1, nullableArgumentSlot.size)
        assertNull(nullableArgumentSlot[0])
    }

    override fun tearDown() {
        super.tearDown()
        unmockkObject(PromoRequestMapper)
    }
}
