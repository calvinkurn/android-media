package com.tokopedia.shop.score.performance.presentation.adapter

class ShopPerformanceTypeFactoryListener(
    val shopPerformanceListener: ShopPerformanceListener,
    val itemShopPerformanceListener: ItemShopPerformanceListener,
    val itemPotentialPowerMerchantListener: ItemPotentialRegularMerchantListener,
    val itemRecommendationFeatureListener: ItemRecommendationFeatureListener,
    val itemStatusPowerMerchantListener: ItemStatusPowerMerchantListener,
    val itemTimerNewSellerListener: ItemTimerNewSellerListener,
    val sectionFaqListener: SectionFaqListener,
    val globalErrorListener: GlobalErrorListener,
    val itemRegularMerchantListener: ItemRegularMerchantListener,
    val potentialPMProListener: ItemRMPotentialPMProListener,
    val itemStatusPowerMerchantProListener: ItemStatusPowerMerchantProListener,
    val itemPMPotentialPMProListener: ItemPMPotentialPMProListener,
    val protectedParameterListener: ProtectedParameterListener
)