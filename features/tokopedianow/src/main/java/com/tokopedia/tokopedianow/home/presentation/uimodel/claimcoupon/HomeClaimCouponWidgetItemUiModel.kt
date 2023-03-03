package com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon

import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutUiModel

data class HomeClaimCouponWidgetItemUiModel(
    val id: String,
    val widgetId: String,
    val smallImageUrlMobile: String,
    val imageUrlMobile: String,
    val ctaText: String,
    val isDouble: Boolean,
    val appLink: String
): HomeLayoutUiModel(id) {
    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}
