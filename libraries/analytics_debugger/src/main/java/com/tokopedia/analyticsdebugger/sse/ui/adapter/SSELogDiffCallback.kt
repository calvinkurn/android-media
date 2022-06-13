package com.tokopedia.analyticsdebugger.sse.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.analyticsdebugger.sse.ui.uimodel.SSELogUiModel

/**
 * Created By : Jonathan Darwin on November 12, 2021
 */
class SSELogDiffCallback(
    private val oldList: List<SSELogUiModel>,
    private val newList: List<SSELogUiModel>,
): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }
}