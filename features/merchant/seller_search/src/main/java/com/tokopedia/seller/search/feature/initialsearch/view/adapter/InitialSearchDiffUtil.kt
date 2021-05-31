package com.tokopedia.seller.search.feature.initialsearch.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.*

class InitialSearchDiffUtil(private val oldList: List<Visitable<*>>, private val newList: List<Visitable<*>>): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList.getOrNull(oldItemPosition)
        val newItem = newList.getOrNull(newItemPosition)
        return isTheSameItemTitleInitialSearchUiModel(oldItem, newItem) || isTheSameItemInitialSearchUiModel(oldItem, newItem) ||
                isTheSameItemTitleHighlightSearchUiModel(oldItem, newItem) || isTheSameHighlightSearchUiModel(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList.getOrNull(oldItemPosition)
        val newItem = newList.getOrNull(newItemPosition)
        return oldItem == newItem
    }

    private fun isTheSameItemTitleInitialSearchUiModel(oldItem: Visitable<*>?, newItem: Visitable<*>?): Boolean {
        return oldItem is ItemTitleInitialSearchUiModel && newItem is ItemTitleInitialSearchUiModel
    }

    private fun isTheSameItemInitialSearchUiModel(oldItem: Visitable<*>?, newItem: Visitable<*>?): Boolean {
        return oldItem is ItemInitialSearchUiModel && newItem is ItemInitialSearchUiModel &&
                oldItem.id == newItem.id
    }

    private fun isTheSameItemTitleHighlightSearchUiModel(oldItem: Visitable<*>?, newItem: Visitable<*>?): Boolean {
        return oldItem is ItemTitleHighlightInitialSearchUiModel && newItem is ItemTitleHighlightInitialSearchUiModel
    }

    private fun isTheSameHighlightSearchUiModel(oldItem: Visitable<*>?, newItem: Visitable<*>?): Boolean {
        return oldItem is HighlightInitialSearchUiModel && newItem is HighlightInitialSearchUiModel &&
                oldItem.highlightInitialList == newItem.highlightInitialList
    }
}