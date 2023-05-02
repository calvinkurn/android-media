package com.tokopedia.analyticsdebugger.websocket.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogUiModel

abstract class WebSocketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(model: WebSocketLogUiModel, listener: ((WebSocketLogUiModel) -> Unit)?)
}
