package com.tokopedia.shop.score.performance.presentation.adapter

interface ShopPerformanceListener {
    fun onTooltipLevelClicked(level: Int)
    fun onTooltipScoreClicked()
    fun onTickerClickedToPenaltyPage()
}

interface ItemShopPerformanceListener {
    fun onItemClickedToDetailBottomSheet(titlePerformanceDetail: String)
}

interface ItemPotentialRegularMerchantListener {
    fun onItemClickedBenefitPotentialRM()
}

interface ItemRecommendationFeatureListener {
    fun onItemClickedRecommendationFeature(appLink: String)
}

interface ItemStatusPowerMerchantListener {
    fun onItemClickedNextUpdatePM()
    fun onItemClickedGoToPMActivation()
}

interface ItemFaqListener {
    fun onArrowClicked(position: Int)
}