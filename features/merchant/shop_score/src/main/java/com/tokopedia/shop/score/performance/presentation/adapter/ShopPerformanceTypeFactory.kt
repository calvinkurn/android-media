package com.tokopedia.shop.score.performance.presentation.adapter

import com.tokopedia.shop.score.performance.presentation.model.*

interface ShopPerformanceTypeFactory {
    fun type(headerShopPerformanceUiModel: HeaderShopPerformanceUiModel): Int
    fun type(periodDetailPerformanceUiModel: PeriodDetailPerformanceUiModel): Int
    fun type(itemDetailPerformanceUiModel: ItemDetailPerformanceUiModel): Int
    fun type(transitionPeriodReliefUiModel: TransitionPeriodReliefUiModel): Int
    fun type(itemCurrentStatusPMUiModel: ItemCurrentStatusPMUiModel): Int
    fun type(itemPotentialPMBenefitUIModel: SectionPotentialPMBenefitUiModel): Int
    fun type(itemPotentialStatusPMUiModel: ItemPotentialStatusPMUiModel): Int
    fun type(sectionShopRecommendationUiModel: SectionShopRecommendationUiModel): Int
    fun type(timerNewSellerUiModel: ItemTimerNewSellerUiModel): Int
}