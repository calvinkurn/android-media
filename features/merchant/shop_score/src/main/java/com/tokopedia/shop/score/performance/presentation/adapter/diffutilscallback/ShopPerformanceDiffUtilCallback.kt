package com.tokopedia.shop.score.performance.presentation.adapter.diffutilscallback

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.score.performance.presentation.model.*
import com.tokopedia.shop.score.performance.presentation.model.tablet.ItemHeaderParameterDetailUiModel

class ShopPerformanceDiffUtilCallback(
    private val oldList: List<Visitable<*>>,
    private val newList: List<Visitable<*>>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList.getOrNull(oldItemPosition)
        val newItem = newList.getOrNull(newItemPosition)
        return isTheSameHeaderShopPerformanceUiModel(oldItem, newItem) ||
                isTheSameDetailPerformanceUiModel(oldItem, newItem) ||
                isTheSameLevelScoreProjectUiModel(oldItem, newItem) ||
                isTheSameItemStatusPMProUiModel(oldItem, newItem) ||
                isTheSameItemStatusPMUiModel(oldItem, newItem) ||
                isTheSameItemStatusRMUiModel(oldItem, newItem) ||
                isTheSameItemTimerNewSellerUiModel(oldItem, newItem) ||
                isTheSamePeriodDetailPerformanceUiModel(oldItem, newItem) ||
                isTheSameProtectedParameterSectionUiModel(oldItem, newItem) ||
                isTheSameSectionFaqUiModel(oldItem, newItem) ||
                isTheSameSectionPMPotentialPMProUiModel(oldItem, newItem) ||
                isTheSameSectionRMPotentialPMBenefitUiModel(oldItem, newItem) ||
                isTheSameSectionRMPotentialPMProUiModel(oldItem, newItem) ||
                isTheSameSectionShopRecommendationUiModel(oldItem, newItem) ||
                isTheSameTickerReactivatedUiModel(oldItem, newItem) ||
                isTheSameReactivatedComebackUiModel(oldItem, newItem) ||
                isTheSameItemHeaderParameterDetailUiModel(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList.getOrNull(oldItemPosition)
        val newItem = newList.getOrNull(newItemPosition)
        return oldItem == newItem
    }

    private fun isTheSameItemHeaderParameterDetailUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is ItemHeaderParameterDetailUiModel && newItem is ItemHeaderParameterDetailUiModel &&
                oldItem.headerShopPerformanceUiModel == newItem.headerShopPerformanceUiModel &&
                oldItem.detailParameterList == newItem.detailParameterList
    }

    private fun isTheSameTickerReactivatedUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is TickerReactivatedUiModel && newItem is TickerReactivatedUiModel
    }

    private fun isTheSameReactivatedComebackUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is ItemReactivatedComebackUiModel && newItem is ItemReactivatedComebackUiModel
    }

    private fun isTheSameHeaderShopPerformanceUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is HeaderShopPerformanceUiModel && newItem is HeaderShopPerformanceUiModel &&
                oldItem.shopScore == newItem.shopScore && oldItem.shopLevel == newItem.shopLevel &&
                oldItem.showCard == newItem.showCard
    }

    private fun isTheSameDetailPerformanceUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is ItemDetailPerformanceUiModel && newItem is ItemDetailPerformanceUiModel &&
                oldItem.shopScore == newItem.shopScore && oldItem.titleDetailPerformance == newItem.titleDetailPerformance &&
                oldItem.valueDetailPerformance == newItem.valueDetailPerformance
    }

    private fun isTheSameLevelScoreProjectUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is ItemLevelScoreProjectUiModel && newItem is ItemLevelScoreProjectUiModel
    }

    private fun isTheSameItemStatusPMProUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is ItemStatusPMProUiModel && newItem is ItemStatusPMProUiModel
    }

    private fun isTheSameItemStatusPMUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is ItemStatusPMUiModel && newItem is ItemStatusPMUiModel &&
                oldItem.descPM == newItem.descPM
    }

    private fun isTheSameItemStatusRMUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is ItemStatusRMUiModel && newItem is ItemStatusRMUiModel &&
                oldItem.titleRMEligible == newItem.titleRMEligible &&
                oldItem.descRMEligible == newItem.descRMEligible
    }

    private fun isTheSameItemTimerNewSellerUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is ItemTimerNewSellerUiModel && newItem is ItemTimerNewSellerUiModel &&
                oldItem.shopAge == newItem.shopAge &&
                oldItem.shopScore == newItem.shopScore &&
                oldItem.isTenureDate == newItem.isTenureDate
    }

    private fun isTheSamePeriodDetailPerformanceUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is PeriodDetailPerformanceUiModel && newItem is PeriodDetailPerformanceUiModel &&
                oldItem.period == newItem.period &&
                oldItem.nextUpdate == newItem.nextUpdate
    }

    private fun isTheSameProtectedParameterSectionUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is ProtectedParameterSectionUiModel && newItem is ProtectedParameterSectionUiModel &&
                oldItem.descParameterRelief == newItem.descParameterRelief
    }

    private fun isTheSameSectionFaqUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is SectionFaqUiModel && newItem is SectionFaqUiModel &&
                oldItem.itemFaqUiModelList == newItem.itemFaqUiModelList
    }

    private fun isTheSameSectionPMPotentialPMProUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is SectionPMPotentialPMProUiModel && newItem is SectionPMPotentialPMProUiModel
                && oldItem.potentialPMProPMBenefitList == newItem.potentialPMProPMBenefitList
    }

    private fun isTheSameSectionRMPotentialPMBenefitUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is SectionRMPotentialPMBenefitUiModel && newItem is SectionRMPotentialPMBenefitUiModel
                && oldItem.potentialPMBenefitList == newItem.potentialPMBenefitList
    }

    private fun isTheSameSectionRMPotentialPMProUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is SectionRMPotentialPMProUiModel && newItem is SectionRMPotentialPMProUiModel
                && oldItem.potentialPMProPMBenefitList == newItem.potentialPMProPMBenefitList
    }

    private fun isTheSameSectionShopRecommendationUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is SectionShopRecommendationUiModel && newItem is SectionShopRecommendationUiModel
                && oldItem.recommendationShopList == newItem.recommendationShopList
    }
}