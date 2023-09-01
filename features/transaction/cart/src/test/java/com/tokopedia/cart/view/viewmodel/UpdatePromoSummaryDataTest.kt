package com.tokopedia.cart.view.viewmodel

import com.tokopedia.cart.data.model.response.promo.CartPromoData
import com.tokopedia.cart.data.model.response.promo.LastApplyPromo
import com.tokopedia.cart.data.model.response.promo.LastApplyPromoData
import com.tokopedia.cart.data.model.response.promo.VoucherOrders
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cartrevamp.view.mapper.CartUiModelMapper
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyAdditionalInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUsageSummariesUiModel
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
        val cartData = CartData(promo = CartPromoData(lastApplyPromo = LastApplyPromo(lastApplyPromoData = LastApplyPromoData(codes = listOf("ABC"), listVoucherOrders = listOf(VoucherOrders())))))
        cartViewModel.cartModel.apply {
            cartListData = cartData
            promoSummaryUiModel = CartUiModelMapper.mapPromoSummaryUiModel(cartData.promoSummary)
        }

        // WHEN
        cartViewModel.updatePromoSummaryData(lastApplyUiModel)

        // THEN
        assert(cartViewModel.cartModel.promoSummaryUiModel != null)
    }
}
