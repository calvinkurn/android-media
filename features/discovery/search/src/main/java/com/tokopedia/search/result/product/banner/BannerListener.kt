package com.tokopedia.search.result.product.banner

interface BannerListener {
    fun onBannerClicked(bannerDataView: BannerDataView)
    fun onBannerImpressed(bannerDataView: BannerDataView)
}
