package com.tokopedia.shop.score.performance.presentation.model

import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceAdapterTypeFactory

data class ItemStatusPMUiModel(
        val titlePowerMerchant: String = "",
        val statusPowerMerchant: String = "",
        val badgePowerMerchant: String = "",
        val bgPowerMerchant: Int? = null,
        val updateDatePotential: String = "",
        val descPotentialPM: String = "",
        val isActivePM: Boolean = false
): BaseShopPerformance {
    override fun type(typeFactory: ShopPerformanceAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}