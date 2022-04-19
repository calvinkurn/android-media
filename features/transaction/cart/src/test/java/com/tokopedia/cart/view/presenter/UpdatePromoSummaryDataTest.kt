package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.data.model.response.promo.CartPromoData
import com.tokopedia.cart.data.model.response.promo.LastApplyPromo
import com.tokopedia.cart.data.model.response.promo.LastApplyPromoData
import com.tokopedia.cart.data.model.response.promo.VoucherOrders
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyAdditionalInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUsageSummariesUiModel
import org.junit.Test

class UpdatePromoSummaryDataTest : BaseCartTest() {

    @Test
    fun `WHEN update promo summary data THEN summary transaction should be updated`() {
        // GIVEN
        val lastApplyUiModel = LastApplyUiModel(additionalInfo = LastApplyAdditionalInfoUiModel(usageSummaries = listOf(
                LastApplyUsageSummariesUiModel()
        )))
        val cartData = CartData(promo = CartPromoData(lastApplyPromo = LastApplyPromo(lastApplyPromoData = LastApplyPromoData(codes = listOf("ABC"), listVoucherOrders = listOf(VoucherOrders())))))
        cartListPresenter.setCartListData(cartData)

        // WHEN
        cartListPresenter.updatePromoSummaryData(lastApplyUiModel)

        // THEN
        assert(cartListPresenter.getPromoSummaryUiModel() != null)
    }

}