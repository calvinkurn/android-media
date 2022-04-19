package com.tokopedia.topads.sdk.listener

import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopProductViewModel

interface TopAdsAddToCartClickListener {
    fun onAdToCartClicked(bannerShopProductViewModel: BannerShopProductViewModel)
}