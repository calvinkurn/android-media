package com.tokopedia.shop.score.performance.presentation.adapter


interface ShopPerformanceListener :
    ItemPerformanceHeaderListener, ItemShopPerformanceListener,
    ItemRMPotentialPMProListener, ItemPMPotentialPMProListener, ItemRegularMerchantListener,
    ItemPotentialRegularMerchantListener, ItemRecommendationFeatureListener,
    ItemStatusPowerMerchantListener, ItemStatusPowerMerchantProListener, SectionFaqListener,
    ProtectedParameterListener, ItemTimerNewSellerListener, GlobalErrorListener,
    ItemReactivatedComebackListener, TickerReactivatedListener

interface ItemPerformanceHeaderListener {
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
    fun onPMToPMProPage()
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

interface ProtectedParameterListener {
    fun onProtectedParameterChevronClicked(descParameterRelief: String)
}

interface ItemTimerNewSellerListener {
    fun onBtnLearnNowToSellerEduClicked(sellerEduUrl: String)
    fun onBtnLearnNowToFaqClicked()
    fun onBtnShopPerformanceToInterruptClicked(infoPageUrl: String)
    fun onWatchVideoClicked(videoId: String)
    fun onImpressBtnLearnPerformance()
    fun onImpressWatchVideo()
}

interface ItemReactivatedComebackListener {
    fun onBtnLearnNowReactivatedClicked(sellerEduUrl: String)
    fun onWatchVideoReactivatedClicked(videoId: String)
}

interface TickerReactivatedListener {
    fun onCloseTickerClicked()
}

interface GlobalErrorListener {
    fun onBtnErrorStateClicked()
}