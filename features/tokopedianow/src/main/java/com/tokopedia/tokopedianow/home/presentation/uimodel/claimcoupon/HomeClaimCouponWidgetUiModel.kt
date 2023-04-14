package com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon

import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutUiModel

data class HomeClaimCouponWidgetUiModel(
    val id: String,
    val title: String,
    val claimCouponList: List<HomeClaimCouponWidgetItemUiModel>?,
    val isDouble: Boolean,
    val slugs: List<String>,
    val slugText: String,
    val warehouseId: String,
    @TokoNowLayoutState val state: Int
): HomeLayoutUiModel(id) {
    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}
