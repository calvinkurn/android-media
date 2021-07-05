package com.tokopedia.sellerorder.filter.presentation.adapter

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerorder.filter.presentation.adapter.SomFilterItemChipsAdapter.Companion.KEY_IS_SELECTED_CHIPS
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterChipsUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterDateUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterEmptyUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel


class SomFilterDiffUtilCallback(
        private val oldItems: List<Visitable<*>>,
        private val newItems: List<Visitable<*>>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems.getOrNull(oldItemPosition)
        val newItem = newItems.getOrNull(newItemPosition)
        return isTheSameFilterUiModel(oldItem, newItem) || isTheSameFilterDateUiModel(oldItem, newItem) || isTheSameEmptyState(oldItem, newItem)
    }

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems.getOrNull(oldItemPosition)
        val newItem = newItems.getOrNull(newItemPosition)
        return oldItem == newItem
    }

    private fun isTheSameFilterUiModel(oldItem: Visitable<*>?, newItem: Visitable<*>?): Boolean {
        return oldItem is SomFilterUiModel && newItem is SomFilterUiModel &&
                oldItem.nameFilter == newItem.nameFilter
    }

    private fun isTheSameFilterDateUiModel(oldItem: Visitable<*>?, newItem: Visitable<*>?): Boolean {
        return oldItem is SomFilterDateUiModel && newItem is SomFilterDateUiModel &&
                oldItem.nameFilter == newItem.nameFilter
    }

    private fun isTheSameEmptyState(oldItem: Visitable<*>?, newItem: Visitable<*>?): Boolean {
        return oldItem is SomFilterEmptyUiModel && newItem is SomFilterEmptyUiModel
    }
}

class SomSubFilterDiffUtil(private val oldList: List<SomFilterChipsUiModel>, private val newList: List<SomFilterChipsUiModel>): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].key == newList[oldItemPosition].key
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].idFilter == newList[newItemPosition].idFilter &&
                oldList[oldItemPosition].isSelected == newList[newItemPosition].isSelected
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val newItem = newList[newItemPosition]
        val oldItem = oldList[oldItemPosition]

        val diff = Bundle()

        if (newItem.isSelected != oldItem.isSelected) {
            diff.putBoolean(KEY_IS_SELECTED_CHIPS, newItem.isSelected)
        }

        return if (diff.size() == 0) {
            null
        } else diff
    }
}

class SomSubChildFilterDiffUtil(private val oldList: List<SomFilterChipsUiModel.ChildStatusUiModel>, private val newList: List<SomFilterChipsUiModel.ChildStatusUiModel>): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].key == newList[oldItemPosition].key
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].text == newList[newItemPosition].text &&
                oldList[oldItemPosition].childId == newList[newItemPosition].childId
    }
}