package com.tokopedia.analyticsdebugger.websocket.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogUiModel

/**
 * Created By : Jonathan Darwin on December 03, 2021
 */
class WebSocketLogDiffCallback(
    private val oldList: List<WebSocketLogUiModel>,
    private val newList: List<WebSocketLogUiModel>,
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