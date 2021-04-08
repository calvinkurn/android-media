package com.tokopedia.shop.score.performance.presentation.adapter

import android.view.View

interface ShopPerformanceListener {
    fun onTooltipLevelClicked(level: Int)
    fun onTooltipScoreClicked()
    fun onTickerClickedToPenaltyPage()
}

interface ItemShopPerformanceListener {
    fun onItemClickedToDetailBottomSheet(titlePerformanceDetail: String,
                                         identifierPerformanceDetail: String)
    fun onViewItemDetailPerformanceListener(view: View)
}

interface ItemHeaderShopPerformanceListener {
    fun onViewHeaderListener(view: View)
}

interface ItemPotentialRegularMerchantListener {
    fun onItemClickedBenefitPotentialRM()
    fun onViewRegularMerchantListener(view: View)
}

interface ItemRecommendationFeatureListener {
    fun onItemClickedRecommendationFeature(appLink: String)
}

interface ItemStatusPowerMerchantListener {
    fun onItemClickedNextUpdatePM()
    fun onItemClickedGoToPMActivation()
    fun onViewItemPowerMerchantListener(view: View)
}

interface SectionFaqListener {
    fun onHelpCenterClicked()
}

interface ItemFaqListener {
    fun onArrowClicked(position: Int)
}

interface ItemTimerNewSellerListener {
    fun onBtnShopPerformanceToFaqClicked()
    fun onBtnShopPerformanceToInterruptClicked(infoPageUrl: String)
    fun onWatchVideoClicked(videoId: String)
}