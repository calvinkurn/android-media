package com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon

import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutUiModel

data class HomeClaimCouponWidgetItemUiModel(
    val id: Int,
    val smallImageUrlMobile: String,
    val imageUrlMobile: String,
    val ctaText: String,
    val isDouble: Boolean,
    val appLink: String
): HomeLayoutUiModel(id.toString()) {
    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}
