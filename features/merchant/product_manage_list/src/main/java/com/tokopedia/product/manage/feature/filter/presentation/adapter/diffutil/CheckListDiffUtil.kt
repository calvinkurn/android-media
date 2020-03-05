package com.tokopedia.product.manage.feature.filter.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.ChecklistViewModel

class CheckListDiffUtil(private val oldlist: List<ChecklistViewModel>,
                        private val newList: List<ChecklistViewModel>)
    : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldlist[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun getOldListSize(): Int {
        return oldlist.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldlist[oldItemPosition].isSelected == newList[newItemPosition].isSelected
                && oldlist[oldItemPosition].name == newList[newItemPosition].name
                && oldlist[oldItemPosition].value == newList[newItemPosition].value
                && oldlist[oldItemPosition].id == newList[newItemPosition].id
    }

}