package com.tokopedia.product.manage.feature.filter.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterUiModel

class FilterDiffUtil(private val oldList: List<FilterUiModel>, private val newList: List<FilterUiModel>)
    : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].title == newList[newItemPosition].title)
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].data == newList[newItemPosition].data)
    }
}