package com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.ServerLoggerPriorityUiModel
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.ServerLoggerUiModel

class ServerLoggerDiffUtilCallback(
    private val oldList: List<Visitable<*>>,
    private val newList: List<Visitable<*>>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList.getOrNull(oldItemPosition)
        val newItem = newList.getOrNull(newItemPosition)
        return isTheSameItemLoggerUiModel(oldItem, newItem) || isTheSameItemPriorityLoggerUiModel(
            oldItem, newItem
        )
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList.getOrNull(oldItemPosition)
        val newItem = newList.getOrNull(newItemPosition)
        return oldItem == newItem
    }

    private fun isTheSameItemLoggerUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is ServerLoggerUiModel && newItem is ServerLoggerUiModel &&
                oldItem.tag == newItem.tag &&
                oldItem.priority == newItem.priority &&
                oldItem.previewMessage == newItem.previewMessage &&
                oldItem.serverChannel == newItem.serverChannel &&
                oldItem.message == newItem.message &&
                oldItem.dateTime == newItem.dateTime
    }

    private fun isTheSameItemPriorityLoggerUiModel(
        oldItem: Visitable<*>?,
        newItem: Visitable<*>?
    ): Boolean {
        return oldItem is ServerLoggerPriorityUiModel && newItem is ServerLoggerPriorityUiModel &&
                oldItem.priority == newItem.priority
    }
}