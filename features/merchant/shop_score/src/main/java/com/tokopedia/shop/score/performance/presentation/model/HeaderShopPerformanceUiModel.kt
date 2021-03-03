package com.tokopedia.shop.score.performance.presentation.model

import androidx.annotation.StringRes
import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceAdapterTypeFactory

data class HeaderShopPerformanceUiModel(var shopLevel: Int = 0,
                                        var shopScore: Int = 0,
                                        var scorePenalty: Int? = null,
                                        @StringRes var titleHeaderShopService: Int = 0,
                                        @StringRes var descHeaderShopService: Int = 0
) : BaseShopPerformance {
    override fun type(typeFactory: ShopPerformanceAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}