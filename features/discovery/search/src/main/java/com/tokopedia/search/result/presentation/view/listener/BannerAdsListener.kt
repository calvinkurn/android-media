package com.tokopedia.search.result.presentation.view.listener

import com.tokopedia.topads.sdk.domain.model.CpmData

interface BannerAdsListener {

    fun onBannerAdsClicked(position: Int, applink: String?, data: CpmData?)

    fun onBannerAdsImpressionListener(position: Int, data: CpmData?)
}