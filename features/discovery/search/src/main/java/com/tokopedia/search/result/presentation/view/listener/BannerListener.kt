package com.tokopedia.search.result.presentation.view.listener

import com.tokopedia.search.result.presentation.model.BannerDataView

interface BannerListener {
    fun onBannerClicked(bannerDataView: BannerDataView)
}
