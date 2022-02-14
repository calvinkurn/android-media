package com.tokopedia.shop.score.performance.presentation.model

import androidx.annotation.StringRes
import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceAdapterTypeFactory

data class ItemStatusRMUiModel(
    @StringRes val titleRMEligible: Int? = null,
    @StringRes val descRMEligible: Int? = null
) : BaseShopPerformance {
    override fun type(typeFactory: ShopPerformanceAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}