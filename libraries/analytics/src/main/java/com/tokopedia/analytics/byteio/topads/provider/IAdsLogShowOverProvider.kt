package com.tokopedia.analytics.byteio.topads.provider

import com.tokopedia.analytics.byteio.topads.models.AdsLogShowOverModel

internal interface IAdsLogShowOverProvider {
    val isAds: Boolean
    val adsLogShowOverModel: AdsLogShowOverModel?
}
