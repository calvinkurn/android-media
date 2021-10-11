package com.tokopedia.shop.score.performance.presentation.model

import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceAdapterTypeFactory
import java.util.*

data class ItemTimerNewSellerUiModel(
    val effectiveDate: Calendar? = null,
    val effectiveDateText: String = "",
    val isTenureDate: Boolean = false,
    val shopAge: Long = 0,
    val shopScore: Int = -1
) : BaseShopPerformance {
    override fun type(typeFactory: ShopPerformanceAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}