package com.tokopedia.shop.score.penalty.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.score.penalty.presentation.model.ItemCardShopPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPeriodDetailPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemSortFilterPenaltyUiModel

class ShopPenaltyDiffUtilCallback(
    private val oldList: List<Visitable<*>>,
    private val newList: List<Visitable<*>>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList.getOrNull(oldItemPosition)
        val newItem = newList.getOrNull(newItemPosition)
        return isTheSameItemPenaltyUiModel(oldItem, newItem) ||
                isTheSameItemCardShopPenaltyUiModel(oldItem, newItem) ||
                isTheSameItemPeriodDetailPenaltyUiModel(oldItem, newItem) ||
                isTheSameItemSortFilterPenaltyUiModel(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList.getOrNull(oldItemPosition)
        val newItem = newList.getOrNull(newItemPosition)
        return oldItem == newItem
    }

    private fun isTheSameItemPenaltyUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is ItemPenaltyUiModel && newItem is ItemPenaltyUiModel &&
                oldItem.invoicePenalty == newItem.invoicePenalty &&
                oldItem.deductionPoint == newItem.deductionPoint &&
                oldItem.descStatusPenalty == newItem.descStatusPenalty
    }

    private fun isTheSameItemCardShopPenaltyUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is ItemCardShopPenaltyUiModel && newItem is ItemCardShopPenaltyUiModel &&
                oldItem.totalPenalty == newItem.totalPenalty &&
                oldItem.hasPenalty == newItem.hasPenalty &&
                oldItem.deductionPoints == newItem.deductionPoints
    }

    private fun isTheSameItemPeriodDetailPenaltyUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is ItemPeriodDetailPenaltyUiModel && newItem is ItemPeriodDetailPenaltyUiModel &&
                oldItem.periodDetail == newItem.periodDetail
    }

    private fun isTheSameItemSortFilterPenaltyUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is ItemSortFilterPenaltyUiModel && newItem is ItemSortFilterPenaltyUiModel &&
                oldItem.itemSortFilterWrapperList == newItem.itemSortFilterWrapperList
    }
}