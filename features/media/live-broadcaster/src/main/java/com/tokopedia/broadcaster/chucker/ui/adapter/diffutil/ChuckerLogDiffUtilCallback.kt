package com.tokopedia.broadcaster.chucker.ui.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.broadcaster.chucker.ui.uimodel.ChuckerLogUIModel

class ChuckerLogDiffUtilCallback constructor(
    private val oldData: MutableList<ChuckerLogUIModel>,
    private val newData: MutableList<ChuckerLogUIModel>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldData.size

    override fun getNewListSize() = newData.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition].id == newData[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition] == newData[newItemPosition]
    }
}