package com.tokopedia.shop.score.performance.presentation.adapter

import com.tokopedia.shop.score.performance.presentation.model.SectionFaqUiModel
import com.tokopedia.shop.score.performance.presentation.model.*

interface ShopPerformanceTypeFactory {
    fun type(headerShopPerformanceUiModel: HeaderShopPerformanceUiModel): Int
    fun type(periodDetailPerformanceUiModel: PeriodDetailPerformanceUiModel): Int
    fun type(itemDetailPerformanceUiModel: ItemDetailPerformanceUiModel): Int
    fun type(transitionPeriodReliefUiModel: TransitionPeriodReliefUiModel): Int
    fun type(itemStatusRMUiModel: ItemStatusRMUiModel): Int
    fun type(itemPotentialPMBenefitUIModel: SectionPotentialPMBenefitUiModel): Int
    fun type(itemStatusPMUiModel: ItemStatusPMUiModel): Int
    fun type(sectionShopRecommendationUiModel: SectionShopRecommendationUiModel): Int
    fun type(timerNewSellerUiModel: ItemTimerNewSellerUiModel): Int
    fun type(sectionFaqUiModel: SectionFaqUiModel): Int
    fun type(itemShopPerformanceErrorUiModel: ItemShopPerformanceErrorUiModel): Int
    fun type(itemLevelScoreProjectUiModel: ItemLevelScoreProjectUiModel): Int
    fun type(sectionPMProBenefitUIModel: SectionPotentialPMProUiModel): Int
}