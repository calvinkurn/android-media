package com.tokopedia.analytics.byteio.topads.models

import com.tokopedia.analytics.byteio.topads.AdsLogConst

data class AdsLogShowModel(
    val adsValue: Long,
    val logExtra: String,
    val adExtraData: AdExtraData
) {
    data class AdExtraData(
        val mallCardType: String = AdsLogConst.AdCardStyle.PRODUCT_CARD,
        val productId: String,
        val productName: String,
    )
}
