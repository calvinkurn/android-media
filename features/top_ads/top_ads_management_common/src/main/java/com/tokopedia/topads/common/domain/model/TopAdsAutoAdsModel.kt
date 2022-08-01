package com.tokopedia.topads.common.domain.model

data class TopAdsAutoAdsModel(
    val shopId: String? = "",
    val status: Int = 0,
    val statusDesc: String? = "",
    val dailyBudget: Int = 0,
    val dailyUsage: Int = 0,
    val adsInfo: TopAdsAutoAdsInfo? = TopAdsAutoAdsInfo(),
) {
    data class TopAdsAutoAdsInfo(
        val reason: String? = "",
        val message: String? = "",
    )
}