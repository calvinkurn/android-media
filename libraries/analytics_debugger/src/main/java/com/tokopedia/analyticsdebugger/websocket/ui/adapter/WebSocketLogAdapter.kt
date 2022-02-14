package com.tokopedia.analyticsdebugger.websocket.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLog
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogUiModel
import com.tokopedia.logger.LogManager

/**
 * Created By : Jonathan Darwin on December 03, 2021
 */
class WebSocketLogAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data = mutableListOf<WebSocketLog>()
    private var listener: ((WebSocketLogUiModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            LOG -> WebSocketLogViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_websocket_log_list, parent, false))
            else -> object: RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_websocket_log_placeholder, parent, false)) {}
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is WebSocketLogViewHolder)
            holder.bind((data[position] as WebSocketLogUiModel), listener)
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int {
        return if(data[position] is WebSocketLogUiModel) return LOG
        else PLACEHOLDER
    }

    fun submitList(newList: List<WebSocketLog>) {
        val diffCallback = WebSocketLogDiffCallback(data, newList)
        val dispatch = DiffUtil.calculateDiff(diffCallback)
        dispatch.dispatchUpdatesTo(this)

        data.clear()
        data.addAll(newList)
    }

    fun setOnClickListener(listener: (WebSocketLogUiModel) -> Unit) {
        this.listener = listener
    }

    private companion object {
        const val LOG = 1
        const val PLACEHOLDER = 2
    }
}