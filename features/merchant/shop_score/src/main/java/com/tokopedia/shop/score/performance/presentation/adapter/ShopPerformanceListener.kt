package com.tokopedia.shop.score.performance.presentation.adapter

interface ShopPerformanceListener {
    fun onTooltipLevelClicked(level: Int)
    fun onTooltipScoreClicked()
    fun onTickerClickedToPenaltyPage()
}

interface ItemShopPerformanceListener {
    fun onItemClickedToDetailBottomSheet(titlePerformanceDetail: String)
}

interface ItemCurrentStatusPowerMerchantListener {
    fun onItemClickedCurrentStatus()
}

interface ItemPotentialPowerMerchantListener {
    fun onItemClickedBenefitPotentialPM()
}

interface ItemRecommendationFeatureListener {
    fun onItemClickedRecommendationFeature(appLink: String)
}