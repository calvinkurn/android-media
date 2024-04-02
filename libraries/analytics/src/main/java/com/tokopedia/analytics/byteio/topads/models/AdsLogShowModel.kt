package com.tokopedia.analytics.byteio.topads.models

import com.tokopedia.analytics.byteio.topads.AdsLogConst

data class AdsLogShowModel(
    val adsValue: String,
    val rit: String,
    // todo need to confirm
    val systemTimeStartClick: String,
    val adExtraData: AdExtraData
) {
    data class AdExtraData(
        val channel: String = "",
        val enterFrom: String = "",
        val mallCardType: String = AdsLogConst.AdCardStyle.PRODUCT_CARD,
        val productId: String
    )
}
