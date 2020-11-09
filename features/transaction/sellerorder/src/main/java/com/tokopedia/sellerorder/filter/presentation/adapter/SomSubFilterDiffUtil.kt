package com.tokopedia.sellerorder.filter.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterChipsUiModel

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