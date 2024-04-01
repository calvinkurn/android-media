package com.tokopedia.analytics.byteio.topads.models

data class AdsLogShowModel(
    val nt: String,
    val adsValue: String,
    val logExtra: String,
    val rit: String,
    // todo need to confirm
    val systemTimeStartClick: String,
    val adExtraData: AdExtraData
) {
    data class AdExtraData(
        val channel: String,
        val enterFrom: String,
        val mallCardType: String,
        val productId: String
    )
}
