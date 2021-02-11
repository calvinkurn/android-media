package com.tokopedia.topads.sdk.view.adapter.viewmodel.banner

import com.tokopedia.topads.sdk.base.adapter.Item
import com.tokopedia.topads.sdk.view.adapter.factory.BannerAdsTypeFactory

class BannerProductShimmerViewModel : Item<BannerAdsTypeFactory> {
    override fun type(typeFactory: BannerAdsTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }

    override fun originalPos(): Int {
        return 0
    }
}