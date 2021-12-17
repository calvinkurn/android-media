package com.tokopedia.shop.score.performance.presentation.adapter

import com.tokopedia.shop.score.performance.presentation.model.SectionFaqUiModel
import com.tokopedia.shop.score.performance.presentation.model.*
import com.tokopedia.shop.score.performance.presentation.model.tablet.ItemHeaderParameterDetailUiModel

interface ShopPerformanceTypeFactory {
    fun type(headerShopPerformanceUiModel: HeaderShopPerformanceUiModel): Int
    fun type(periodDetailPerformanceUiModel: PeriodDetailPerformanceUiModel): Int
    fun type(itemDetailPerformanceUiModel: ItemDetailPerformanceUiModel): Int
    fun type(itemStatusRMUiModel: ItemStatusRMUiModel): Int
    fun type(itemRMPotentialPMBenefitUIModel: SectionRMPotentialPMBenefitUiModel): Int
    fun type(itemStatusPMUiModel: ItemStatusPMUiModel): Int
    fun type(sectionShopRecommendationUiModel: SectionShopRecommendationUiModel): Int
    fun type(timerNewSellerUiModel: ItemTimerNewSellerUiModel): Int
    fun type(sectionFaqUiModel: SectionFaqUiModel): Int
    fun type(itemShopPerformanceErrorUiModel: ItemShopPerformanceErrorUiModel): Int
    fun type(itemLevelScoreProjectUiModel: ItemLevelScoreProjectUiModel): Int
    fun type(sectionRMPMProBenefitUIModel: SectionRMPotentialPMProUiModel): Int
    fun type(sectionPMPotentialPMProUiModel: SectionPMPotentialPMProUiModel): Int
    fun type(itemStatusPMProUiModel: ItemStatusPMProUiModel): Int
    fun type(protectedParameterSectionUiModel: ProtectedParameterSectionUiModel): Int
    fun type(itemReactivatedComebackUiModel: ItemReactivatedComebackUiModel): Int
    fun type(tickerReactivatedUiModel: TickerReactivatedUiModel): Int
    fun type(itemHeaderParameterDetailUiModel: ItemHeaderParameterDetailUiModel): Int
}