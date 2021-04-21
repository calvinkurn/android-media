package com.tokopedia.shop.score.performance.presentation.adapter

import android.view.View

interface ShopPerformanceListener {
    fun onTooltipLevelClicked(level: Int)
    fun onTooltipScoreClicked()
    fun onTickerClickedToPenaltyPage()
    fun onTickerImpressionToPenaltyPage()
}

interface ItemShopPerformanceListener {
    fun onItemClickedToDetailBottomSheet(titlePerformanceDetail: String,
                                         identifierPerformanceDetail: String)
}

interface PeriodDetailPerformanceListener {
    fun onAddCoachMarkItemPeriod(view: View)
}

interface ItemHeaderShopPerformanceListener {
    fun onViewHeaderListener(view: View)
    fun onImpressHeaderTicker()
}

interface ItemPotentialRegularMerchantListener {
    fun onItemClickedBenefitPotentialRM()
    fun onViewRegularMerchantListener(view: View)
    fun onImpressBenefitSeeAll()
}

interface ItemRecommendationFeatureListener {
    fun onItemClickedRecommendationFeature(appLink: String)
    fun onItemImpressRecommendationFeature()
}

interface ItemStatusPowerMerchantListener {
    fun onItemClickedNextUpdatePM()
    fun onItemClickedGoToPMActivation()
    fun onViewItemPowerMerchantListener(view: View)
    fun onImpressHeaderPowerMerchantSection()
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