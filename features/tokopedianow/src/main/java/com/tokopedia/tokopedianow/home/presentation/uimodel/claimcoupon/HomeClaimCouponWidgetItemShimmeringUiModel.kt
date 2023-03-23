package com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon

import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutUiModel

class HomeClaimCouponWidgetItemShimmeringUiModel(
    val id: String,
    val isDouble: Boolean
): HomeLayoutUiModel(id) {
    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}
