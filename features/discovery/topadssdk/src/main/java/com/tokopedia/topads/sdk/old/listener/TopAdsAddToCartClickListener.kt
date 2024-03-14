package com.tokopedia.topads.sdk.old.listener

import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopProductUiModel

interface TopAdsAddToCartClickListener {
    fun onAdToCartClicked(bannerShopProductUiModel: BannerShopProductUiModel)
}
