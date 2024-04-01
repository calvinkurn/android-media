package com.tokopedia.analytics.byteio.topads.models

data class AdsLogRealtimeClickModel(
    val refer: String,
    val adsValue: String,
    val logExtra: String,
    val rit: String,
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
