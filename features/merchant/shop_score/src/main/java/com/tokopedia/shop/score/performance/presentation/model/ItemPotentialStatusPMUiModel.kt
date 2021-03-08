package com.tokopedia.shop.score.performance.presentation.model

import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceAdapterTypeFactory

data class ItemPotentialStatusPMUiModel(
        val statusPotential: String = "",
        val datePotentialStatus: String = "",
        val iconPotentialPM: String = "",
        val upToPotentialPM: String = ""
): BaseShopPerformance {
    override fun type(typeFactory: ShopPerformanceAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}