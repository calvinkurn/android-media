package com.tokopedia.topads.sdk.listener

import com.tokopedia.topads.sdk.domain.model.CpmData

interface TopAdsBannerClickListener {
    fun onBannerAdsClicked(position: Int, applink: String?, data: CpmData?)
}
