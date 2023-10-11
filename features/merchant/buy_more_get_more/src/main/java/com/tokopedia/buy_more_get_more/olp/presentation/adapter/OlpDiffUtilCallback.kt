package com.tokopedia.buy_more_get_more.olp.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductSortingUiModel

class OlpDiffUtilCallback(
    private val oldItems: List<Visitable<*>>,
    private val newItems: List<Visitable<*>>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems.getOrNull(oldItemPosition)
        val newItem = newItems.getOrNull(newItemPosition)
        return oldItem == newItem
    }

    private inline fun <reified T> isItemMatchWithUiModel(oldItem: Visitable<*>?, newItem: Visitable<*>?): Boolean {
        return oldItem is T && newItem is T
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems.getOrNull(oldItemPosition)
        val newItem = newItems.getOrNull(newItemPosition)
        if (isItemMatchWithUiModel<OfferProductSortingUiModel>(oldItem, newItem)) {
            return false
        }
        return oldItem == newItem
    }

    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }
}
