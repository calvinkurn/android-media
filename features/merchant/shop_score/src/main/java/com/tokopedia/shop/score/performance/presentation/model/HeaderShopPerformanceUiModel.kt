package com.tokopedia.shop.score.performance.presentation.model

import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceAdapterTypeFactory

data class HeaderShopPerformanceUiModel(var shopLevel: String = "-",
                                        var shopScore: String = "-",
                                        var scorePenalty: Int? = 0,
                                        var shopAge: Int = 0,
                                        var titleHeaderShopService: String? = "",
                                        var descHeaderShopService: String? = "",
                                        var showCardNewSeller: Boolean = false
) : BaseShopPerformance {
    override fun type(typeFactory: ShopPerformanceAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}