package com.tokopedia.shop.score.performance.presentation.model

import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceAdapterTypeFactory

data class HeaderShopPerformanceUiModel(var shopLevel: String = "-",
                                        var shopScore: String = "-",
                                        var scorePenalty: Int? = 0,
                                        var titleHeaderShopService: String? = "",
                                        var descHeaderShopService: String? = "",
                                        var isNewSeller: Boolean = false,

) : BaseShopPerformance {
    override fun type(typeFactory: ShopPerformanceAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}