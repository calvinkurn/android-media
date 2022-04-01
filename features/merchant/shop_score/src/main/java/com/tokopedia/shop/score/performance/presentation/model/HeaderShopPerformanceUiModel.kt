package com.tokopedia.shop.score.performance.presentation.model

import androidx.annotation.StringRes
import com.tokopedia.shop.score.performance.domain.model.GoldGetPMOStatusResponse
import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceAdapterTypeFactory

data class HeaderShopPerformanceUiModel(
    var shopLevel: String = "-",
    var shopScore: String = "-",
    var scorePenalty: Long? = 0,
    var shopAge: Long = 0,
    @StringRes var titleHeaderShopService: Int? = null,
    @StringRes var descHeaderShopService: Int? = null,
    var showCard: Boolean = false,
    var powerMerchantData: GoldGetPMOStatusResponse.GoldGetPMOSStatus.Data? = null,
    var isShowPopupEndTenure: Boolean = false
) : BaseShopPerformance {
    override fun type(typeFactory: ShopPerformanceAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}