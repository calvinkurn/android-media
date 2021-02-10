package com.tokopedia.seller.search.feature.suggestion.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.*

class SuggestionSearchDiffUtil(private val oldList: List<Visitable<*>>, private val newList: List<Visitable<*>>): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList.getOrNull(oldItemPosition)
        val newItem = newList.getOrNull(newItemPosition)
        return isTheSameTitleHeaderSellerSearchUiModel(oldItem, newItem) || isTheSameOrderSearchSellerUiModel(oldItem, newItem) ||
                isTheSameProductSellerSearchUiModel(oldItem, newItem) || isTheSameNavigationSearchUiModel(oldItem, newItem) ||
                isTheSameTitleHasMoreSearchUiModel(oldItem, newItem)

    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList.getOrNull(oldItemPosition)
        val newItem = newList.getOrNull(newItemPosition)
        return oldItem == newItem
    }

    private fun isTheSameTitleHeaderSellerSearchUiModel(oldItem: Visitable<*>?, newItem: Visitable<*>?): Boolean {
        return oldItem is TitleHeaderSellerSearchUiModel && newItem is TitleHeaderSellerSearchUiModel &&
                oldItem.title == newItem.title
    }

    private fun isTheSameOrderSearchSellerUiModel(oldItem: Visitable<*>?, newItem: Visitable<*>?): Boolean {
        return oldItem is OrderSellerSearchUiModel && newItem is OrderSellerSearchUiModel &&
                oldItem.id == newItem.id && oldItem.title == newItem.title
    }

    private fun isTheSameProductSellerSearchUiModel(oldItem: Visitable<*>?, newItem: Visitable<*>?): Boolean {
        return oldItem is ProductSellerSearchUiModel && newItem is ProductSellerSearchUiModel &&
                oldItem.id == newItem.id && oldItem.title == newItem.title
    }

    private fun isTheSameNavigationSearchUiModel(oldItem: Visitable<*>?, newItem: Visitable<*>?): Boolean {
        return oldItem is NavigationSellerSearchUiModel && newItem is NavigationSellerSearchUiModel &&
                oldItem.id == newItem.id && oldItem.title == newItem.title
    }

    private fun isTheSameTitleHasMoreSearchUiModel(oldItem: Visitable<*>?, newItem: Visitable<*>?): Boolean {
        return oldItem is TitleHeaderSellerSearchUiModel && newItem is TitleHeaderSellerSearchUiModel &&
                oldItem.title == newItem.title
    }
}