package com.tokopedia.shop.score.performance.presentation.model

import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceAdapterTypeFactory

data class HeaderShopPerformanceUiModel(var shopLevel: String = "-",
                                        var shopScore: String = "-",
                                        var scorePenalty: Long? = 0,
                                        var shopAge: Long = 0,
                                        var titleHeaderShopService: String? = "",
                                        var descHeaderShopService: String? = "",
                                        var showCardNewSeller: Boolean = false
) : BaseShopPerformance {
    override fun type(typeFactory: ShopPerformanceAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}