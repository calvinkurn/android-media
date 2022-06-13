package com.tokopedia.shop.score.performance.presentation.adapter.diffutilscallback

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.score.performance.presentation.model.ProtectedParameterSectionUiModel
import com.tokopedia.shop.score.performance.presentation.model.tablet.ItemDetailPerformanceTabletUiModel
import com.tokopedia.shop.score.performance.presentation.model.tablet.PeriodDetailTabletUiModel
import com.tokopedia.shop.score.performance.presentation.model.tablet.ProtectedParameterTabletUiModel

class DetailPerformanceDiffUtilCallback(
    private val oldList: List<Visitable<*>>,
    private val newList: List<Visitable<*>>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList.getOrNull(oldItemPosition)
        val newItem = newList.getOrNull(newItemPosition)
        return isTheSameDetailPerformanceUiModel(oldItem, newItem) ||
                isTheSamePeriodDetailPerformanceUiModel(oldItem, newItem) ||
                isTheSameProtectedParameterSectionUiModel(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList.getOrNull(oldItemPosition)
        val newItem = newList.getOrNull(newItemPosition)
        return oldItem == newItem
    }

    private fun isTheSamePeriodDetailPerformanceUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is PeriodDetailTabletUiModel && newItem is PeriodDetailTabletUiModel &&
                oldItem.period == newItem.period &&
                oldItem.nextUpdate == newItem.nextUpdate
    }

    private fun isTheSameDetailPerformanceUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is ItemDetailPerformanceTabletUiModel && newItem is ItemDetailPerformanceTabletUiModel &&
                oldItem.shopScore == newItem.shopScore && oldItem.titleDetailPerformance == newItem.titleDetailPerformance &&
                oldItem.valueDetailPerformance == newItem.valueDetailPerformance
    }

    private fun isTheSameProtectedParameterSectionUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is ProtectedParameterTabletUiModel && newItem is ProtectedParameterTabletUiModel &&
                oldItem.descParameterRelief == newItem.descParameterRelief
    }

}