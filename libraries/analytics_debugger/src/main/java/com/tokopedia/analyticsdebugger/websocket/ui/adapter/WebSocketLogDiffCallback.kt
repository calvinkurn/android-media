package com.tokopedia.analyticsdebugger.websocket.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLog
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogUiModel

/**
 * Created By : Jonathan Darwin on December 03, 2021
 */
class WebSocketLogDiffCallback(
    private val oldList: List<WebSocketLog>,
    private val newList: List<WebSocketLog>,
): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return if(oldList[oldItemPosition] is WebSocketLogUiModel && newList[newItemPosition] is WebSocketLogUiModel) {
            (oldList[oldItemPosition] as WebSocketLogUiModel).id == (newList[newItemPosition] as WebSocketLogUiModel).id
        }
        else oldList[oldItemPosition] == newList[newItemPosition]
    }
}