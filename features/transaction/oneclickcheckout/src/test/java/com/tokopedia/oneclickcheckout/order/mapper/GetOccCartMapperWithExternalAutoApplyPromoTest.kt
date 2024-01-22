package com.tokopedia.oneclickcheckout.order.mapper

import com.tokopedia.oneclickcheckout.order.view.BaseOrderSummaryPageViewModelTest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.PromoExternalAutoApply
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class GetOccCartMapperWithExternalAutoApplyPromoTest : BaseOrderSummaryPageViewModelTest() {

    @Test
    fun `Check GetOccCartMapper With External Auto Apply Promo Voucher`() {
        // When
        val result = getOccCartMapper.mapGetOccCartDataToOrderData(
            data = helper.getOccCartDataWithLastApply.data,
            listPromoExternalAutoApplyCode = arrayListOf(
                PromoExternalAutoApply(
                    code = "DDN30WA2HCZ36A1M2PDR",
                    type = "mv"
                )
            )
        )

        // Then
        assertEquals(0, result.promo.lastApply.codes.size)
        assertEquals(1, result.promo.lastApply.voucherOrders.size)
        assertEquals("DDN30WA2HCZ36A1M2PDR", result.promo.lastApply.voucherOrders.first().code)
    }

    @Test
    fun `Check GetOccCartMapper With External Auto Apply Promo Global`() {
        // When
        val result = getOccCartMapper.mapGetOccCartDataToOrderData(
            data = helper.getOccCartDataWithLastApply.data,
            listPromoExternalAutoApplyCode = arrayListOf(
                PromoExternalAutoApply(
                    code = "DDN30WA2HCZ36A1M2PDR",
                    type = "gl"
                )
            )
        )

        // Then
        assertEquals(1, result.promo.lastApply.codes.size)
        assertEquals(0, result.promo.lastApply.voucherOrders.size)
        assertEquals("DDN30WA2HCZ36A1M2PDR", result.promo.lastApply.codes.first())
    }

    @Test
    fun `Check GetOccCartMapper With Empty External Auto Apply Promo`() {
        // When
        val result = getOccCartMapper.mapGetOccCartDataToOrderData(
            data = helper.getOccCartDataWithLastApply.data,
            listPromoExternalAutoApplyCode = arrayListOf()
        )

        // Then
        assertEquals(1, result.promo.lastApply.codes.size)
        assertEquals(1, result.promo.lastApply.voucherOrders.size)
        assertEquals("BOCODE", result.promo.lastApply.codes.first())
        assertEquals("MVCODE", result.promo.lastApply.voucherOrders.first().code)
    }
}
