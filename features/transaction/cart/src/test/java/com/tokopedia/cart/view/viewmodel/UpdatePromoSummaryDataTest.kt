package com.tokopedia.cart.view.viewmodel

import com.tokopedia.cart.data.model.response.promo.CartPromoData
import com.tokopedia.cart.data.model.response.promo.LastApplyPromo
import com.tokopedia.cart.data.model.response.promo.LastApplyPromoData
import com.tokopedia.cart.data.model.response.promo.PromoAdditionalInfo
import com.tokopedia.cart.data.model.response.promo.VoucherOrders
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.UsageSummaries
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyAdditionalInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUsageSummariesUiModel
import org.junit.Assert.assertEquals
import org.junit.Test

class UpdatePromoSummaryDataTest : BaseCartViewModelTest() {

    @Test
    fun `WHEN update promo summary data THEN summary transaction should be updated`() {
        // GIVEN
        val lastApplyUiModel = LastApplyUiModel(
            additionalInfo = LastApplyAdditionalInfoUiModel(
                usageSummaries = listOf(
                    LastApplyUsageSummariesUiModel()
                )
            )
        )
        val cartData = CartData(
            promo = CartPromoData(
                lastApplyPromo = LastApplyPromo(
                    lastApplyPromoData = LastApplyPromoData(
                        codes = listOf("ABC"),
                        listVoucherOrders = listOf(VoucherOrders()),
                        additionalInfo = PromoAdditionalInfo(
                            usageSummaries = listOf(
                                UsageSummaries(type = "total_product_price", amount = 10000),
                                UsageSummaries(type = "", amount = 5000)
                            )
                        )
                    )
                )
            )
        )
        cartViewModel.cartModel.cartListData = cartData

        // WHEN
        cartViewModel.updatePromoSummaryData(lastApplyUiModel)

        // THEN
        assertEquals(5000, cartViewModel.cartModel.discountAmount)
    }
}
