package com.tokopedia.shop.score.penalty.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.score.performance.presentation.model.*

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
                isTheSameItemStatusPMProUiModel(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList.getOrNull(oldItemPosition)
        val newItem = newList.getOrNull(newItemPosition)
        return oldItem == newItem
    }

    private fun isTheSameHeaderShopPerformanceUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is HeaderShopPerformanceUiModel && newItem is HeaderShopPerformanceUiModel &&
                oldItem.shopScore == newItem.shopScore && oldItem.shopLevel == newItem.shopLevel &&
                oldItem.showCardNewSeller == newItem.showCardNewSeller
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
}