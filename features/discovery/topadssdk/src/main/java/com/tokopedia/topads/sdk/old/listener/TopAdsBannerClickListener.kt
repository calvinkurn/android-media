package com.tokopedia.topads.sdk.old.listener

import com.tokopedia.topads.sdk.old.domain.model.CpmData

interface TopAdsBannerClickListener {
    fun onBannerAdsClicked(position: Int, applink: String?, data: CpmData?)
}
