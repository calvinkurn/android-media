package com.tokopedia.shop.score.performance.presentation.model

import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceAdapterTypeFactory

data class ItemCurrentStatusPMUiModel(val statusPowerMerchant: String = "",
                                      val updateDatePotential: String = "",
                                      val statusPotentialPM: String = "",
                                      val gradePotentialPM: String = ""
): BaseShopPerformance {
    override fun type(typeFactory: ShopPerformanceAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}