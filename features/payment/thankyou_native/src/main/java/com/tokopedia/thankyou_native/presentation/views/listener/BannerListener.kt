package com.tokopedia.thankyou_native.presentation.views.listener

import com.tokopedia.thankyou_native.presentation.adapter.model.BannerItem

interface BannerListener {
    fun onBannerClick(bannerItem: BannerItem, position: Int)

    fun onBannerImpressed(bannerItem: BannerItem, position: Int)
}
