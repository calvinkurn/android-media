package com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel

import com.tokopedia.topads.sdk.common.adapter.Item
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.adapter.factory.BannerAdsTypeFactory

class BannerProductShimmerUiModel : Item<BannerAdsTypeFactory> {
    override fun type(typeFactory: BannerAdsTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }

    override fun originalPos(): Int {
        return 0
    }
}
