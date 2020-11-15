package com.tokopedia.sellerorder.filter.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerorder.filter.presentation.model.BaseSomFilter
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterChipsUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterDateUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListOrderUiModel

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
}

class SomFilterDiffUtil(private val oldList: List<BaseSomFilter>, private val newList: List<BaseSomFilter>): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition] is SomFilterUiModel && newList[newItemPosition] is SomFilterUiModel -> {
                val oldSomFilter = (oldList[oldItemPosition] as SomFilterUiModel)
                val newSomFilter = (newList[newItemPosition] as SomFilterUiModel)
                oldSomFilter.nameFilter == newSomFilter.nameFilter
            }
            oldList[oldItemPosition] is SomFilterDateUiModel && newList[oldItemPosition] is SomFilterDateUiModel-> {
                val oldSomFilterDate = (oldList[oldItemPosition] as SomFilterDateUiModel)
                val newSomFilterDate = (newList[newItemPosition] as SomFilterDateUiModel)
                oldSomFilterDate.nameFilter == newSomFilterDate.nameFilter
            }
            else -> false
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition] is SomFilterUiModel && newList[newItemPosition] is SomFilterUiModel -> {
                val oldSomFilter = (oldList[oldItemPosition] as SomFilterUiModel)
                val newSomFilter = (newList[newItemPosition] as SomFilterUiModel)
                oldSomFilter.isDividerVisible == newSomFilter.isDividerVisible && oldSomFilter.canSelectMany == newSomFilter.canSelectMany
            }
            oldList[oldItemPosition] is SomFilterDateUiModel && newList[oldItemPosition] is SomFilterDateUiModel-> {
                val oldSomFilterDate = (oldList[oldItemPosition] as SomFilterDateUiModel)
                val newSomFilterDate = (newList[newItemPosition] as SomFilterDateUiModel)
                oldSomFilterDate.date == newSomFilterDate.date && oldSomFilterDate.nameFilter == newSomFilterDate.nameFilter
            }
            else -> false
        }
    }
}