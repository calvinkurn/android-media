package com.tokopedia.topads.sdk.v2.listener

import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel.BannerShopProductUiModel

interface TopAdsAddToCartClickListener {
    fun onAdToCartClicked(bannerShopProductUiModel: BannerShopProductUiModel)
}
