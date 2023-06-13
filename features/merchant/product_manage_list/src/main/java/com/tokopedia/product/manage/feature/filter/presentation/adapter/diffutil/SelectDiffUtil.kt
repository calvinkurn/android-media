package com.tokopedia.product.manage.feature.filter.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.ChecklistUiModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectUiModel

class SelectDiffUtil(private val oldList: List<SelectUiModel>, private val newList: List<SelectUiModel>)
    : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].id == newList[newItemPosition].id)
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].isSelected == newList[newItemPosition].isSelected)
    }
}
