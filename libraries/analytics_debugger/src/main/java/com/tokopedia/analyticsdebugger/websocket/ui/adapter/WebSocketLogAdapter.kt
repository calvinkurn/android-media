package com.tokopedia.analyticsdebugger.websocket.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogUiModel

/**
 * Created By : Jonathan Darwin on December 03, 2021
 */
class WebSocketLogAdapter: RecyclerView.Adapter<WebSocketLogViewHolder>() {

    private val data = mutableListOf<WebSocketLogUiModel>()
    private var listener: ((WebSocketLogUiModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebSocketLogViewHolder =
        WebSocketLogViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_websocket_log_list, parent, false))

    override fun onBindViewHolder(holder: WebSocketLogViewHolder, position: Int) {
        holder.bind(data[position], listener)
    }

    override fun getItemCount(): Int = data.size

    fun submitList(newList: List<WebSocketLogUiModel>) {
        val diffCallback = WebSocketLogDiffCallback(data, newList)
        val dispatch = DiffUtil.calculateDiff(diffCallback)
        dispatch.dispatchUpdatesTo(this)

        data.clear()
        data.addAll(newList)
    }

    fun setOnClickListener(listener: (WebSocketLogUiModel) -> Unit) {
        this.listener = listener
    }
}