package com.tokopedia.product.manage.feature.filter.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectViewModel

class SelectDiffUtil(private val oldlist: List<SelectViewModel>,
                     private val newList: List<SelectViewModel>)
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
        return oldlist[oldItemPosition] == newList[newItemPosition]
    }

}