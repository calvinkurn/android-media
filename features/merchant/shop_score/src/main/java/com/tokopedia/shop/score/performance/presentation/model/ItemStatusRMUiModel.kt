package com.tokopedia.shop.score.performance.presentation.model

import androidx.annotation.StringRes
import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceAdapterTypeFactory

data class ItemStatusRMUiModel(val statusGradePM: String = "",
                               val updateDatePotential: String = "",
                               val badgeGradePM: String = "",
                               @StringRes val bgGradePM: Int? = null
) : BaseShopPerformance {
    override fun type(typeFactory: ShopPerformanceAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}