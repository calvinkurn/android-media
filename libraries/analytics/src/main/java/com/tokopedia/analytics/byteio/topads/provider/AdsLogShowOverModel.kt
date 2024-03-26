package com.tokopedia.analytics.byteio.topads.provider

import com.tokopedia.analytics.byteio.topads.AdsLogConst

data class AdsLogShowOverModel(
    val rit: String,
    val productId: String,
    val cardType: String = AdsLogConst.AdCardStyle.PRODUCT_CARD,
    val sessionInfo: SessionInfo = SessionInfo("")
) {
    var percentageVisible: String = ""
}

data class SessionInfo(
    val sessionInd: String
)
