package com.tokopedia.shop.score.performance.presentation.model

import androidx.annotation.StringRes
import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceAdapterTypeFactory

data class ItemStatusPMUiModel(
        val statusPowerMerchant: String = "",
        val badgePowerMerchant: String = "",
        @StringRes val bgPowerMerchant: Int = 0,
        val updateDatePotential: String = "",
        val descPotentialPM: String = "",
        val isActivePM: Boolean = false
): BaseShopPerformance {
    override fun type(typeFactory: ShopPerformanceAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}