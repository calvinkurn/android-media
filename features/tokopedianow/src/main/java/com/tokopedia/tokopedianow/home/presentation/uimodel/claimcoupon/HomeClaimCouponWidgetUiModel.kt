package com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon

import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutUiModel

data class HomeClaimCouponWidgetUiModel(
    val id: String,
    val claimCouponList: List<HomeClaimCouponWidgetItemUiModel>
): HomeLayoutUiModel(id) {
    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}
