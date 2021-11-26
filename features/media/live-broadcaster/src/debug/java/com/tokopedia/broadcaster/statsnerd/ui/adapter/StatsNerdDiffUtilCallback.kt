package com.tokopedia.broadcaster.statsnerd.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.broadcaster.uimodel.LoggerUIModel

class StatsNerdDiffUtilCallback constructor(
    private val oldData: MutableList<LoggerUIModel>,
    private val newData: MutableList<LoggerUIModel>
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