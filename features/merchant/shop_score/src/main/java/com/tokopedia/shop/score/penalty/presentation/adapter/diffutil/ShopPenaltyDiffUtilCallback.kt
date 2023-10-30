package com.tokopedia.shop.score.penalty.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyInfoNotificationUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyPointCardUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltySubsectionUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyTickerUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel
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
            isTheSameItemSortFilterPenaltyUiModel(oldItem, newItem) ||
            isTheSameItemPenaltyTickerUiModel(oldItem, newItem) ||
            isTheSameItemPenaltyInfoNotificationUiModel(oldItem, newItem) ||
            isTheSameItemPenaltySubsectionUiModel(oldItem, newItem) ||
            isTheSameItemPenaltyPointCardUiModel(oldItem, newItem)
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

    private fun isTheSameItemSortFilterPenaltyUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is ItemSortFilterPenaltyUiModel && newItem is ItemSortFilterPenaltyUiModel &&
                oldItem.itemSortFilterWrapperList == newItem.itemSortFilterWrapperList
    }

    private fun isTheSameItemPenaltyTickerUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is ItemPenaltyTickerUiModel && newItem is ItemPenaltyTickerUiModel &&
            oldItem.title == newItem.title &&
            oldItem.tickerMessage == newItem.tickerMessage &&
            oldItem.webUrl == newItem.webUrl &&
            oldItem.actionText == newItem.actionText
    }

    private fun isTheSameItemPenaltyInfoNotificationUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is ItemPenaltyInfoNotificationUiModel && newItem is ItemPenaltyInfoNotificationUiModel &&
            oldItem.shouldShowDot == newItem.shouldShowDot &&
            oldItem.latestOngoingId == newItem.latestOngoingId
    }

    private fun isTheSameItemPenaltySubsectionUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is ItemPenaltySubsectionUiModel && newItem is ItemPenaltySubsectionUiModel &&
            oldItem.date == newItem.date
    }

    private fun isTheSameItemPenaltyPointCardUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is ItemPenaltyPointCardUiModel && newItem is ItemPenaltyPointCardUiModel &&
            oldItem.date == newItem.date &&
            oldItem.result.penaltyDynamic == newItem.result.penaltyDynamic
    }

}
