package com.tokopedia.shop.score.performance.presentation.adapter

interface ShopPerformanceListener {
    fun onTooltipLevelClicked(level: Long)
    fun onTooltipScoreClicked()
    fun onTickerClickedToPenaltyPage()
    fun onTickerImpressionToPenaltyPage()
}

interface ItemShopPerformanceListener {
    fun onItemClickedToDetailBottomSheet(
        titlePerformanceDetail: String,
        identifierPerformanceDetail: String
    )

    fun onItemClickedToFaqClicked()
}

interface ItemRMPotentialPMProListener {
    fun onGotoPMProPage()
}

interface ItemPMPotentialPMProListener {
    fun onGotoBenefitPMPro()
}

interface ItemRegularMerchantListener {
    fun onRMSectionToPMPage()
}

interface ItemPotentialRegularMerchantListener {
    fun onItemClickedBenefitPotentialRM()
    fun onImpressBenefitSeeAll()
}

interface ItemRecommendationFeatureListener {
    fun onItemClickedRecommendationFeature(appLink: String, identifier: String)
    fun onItemImpressRecommendationFeature(identifier: String)
}

interface ItemStatusPowerMerchantListener {
    fun onItemClickedGotoPMPro()
    fun onItemClickedGoToPMActivation()
    fun onImpressHeaderPowerMerchantSection()
}

interface ItemStatusPowerMerchantProListener {
    fun onItemClickedGoToPMProActivation()
}

interface SectionFaqListener {
    fun onHelpCenterClicked()
    fun onImpressHelpCenter()
}

interface ItemFaqListener {
    fun onArrowClicked(position: Int)
}

interface ItemTimerNewSellerListener {
    fun onBtnShopPerformanceToFaqClicked()
    fun onBtnShopPerformanceToInterruptClicked(infoPageUrl: String)
    fun onWatchVideoClicked(videoId: String)
    fun onImpressBtnLearnPerformance()
    fun onImpressWatchVideo()
}

interface GlobalErrorListener {
    fun onBtnErrorStateClicked()
}