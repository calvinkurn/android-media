package com.tokopedia.shop.score.performance.presentation.model

import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceAdapterTypeFactory

class ItemLevelScoreProjectUiModel: BaseShopPerformance {
    override fun type(typeFactory: ShopPerformanceAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}