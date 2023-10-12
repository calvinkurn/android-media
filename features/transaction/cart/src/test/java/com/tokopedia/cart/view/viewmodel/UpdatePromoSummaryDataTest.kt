package com.tokopedia.cart.view.viewmodel

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
                    LastApplyUsageSummariesUiModel(type = "total_product_price", amount = 10000),
                    LastApplyUsageSummariesUiModel(type = "", amount = 5000)
                )
            )
        )

        // WHEN
        cartViewModel.updatePromoSummaryData(lastApplyUiModel)

        // THEN
        assertEquals(10000, cartViewModel.cartModel.discountAmount)
    }
}
