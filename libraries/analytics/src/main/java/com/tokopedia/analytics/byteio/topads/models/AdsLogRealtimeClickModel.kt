package com.tokopedia.analytics.byteio.topads.models

import com.tokopedia.analytics.byteio.topads.AdsLogConst

data class AdsLogRealtimeClickModel(
    val refer: String,
    val adsValue: Long,
    val rit: Long,
    val adExtraData: AdExtraData
) {
    data class AdExtraData(
        val mallCardType: String = AdsLogConst.AdCardStyle.PRODUCT_CARD,
        val productId: String
    )
}
