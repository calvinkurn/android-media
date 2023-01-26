package com.tokopedia.analyticsdebugger.websocket.ui.adapter.viewholder

import android.view.View
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.websocket.ui.adapter.WebSocketViewHolder
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogUiModel
import com.tokopedia.unifyprinciples.Typography

class TopchatWebSocketLogViewHolder(itemView: View) : WebSocketViewHolder(itemView) {

    private val tvTitle = itemView.findViewById<Typography>(R.id.tv_websocket_log_title)
    private val tvSource = itemView.findViewById<Typography>(R.id.tv_websocket_log_source)
    private val tvDateTime = itemView.findViewById<Typography>(R.id.tv_websocket_log_date_time)
    private val tvUrl = itemView.findViewById<Typography>(R.id.tv_websocket_log_url)

    override fun bind(model: WebSocketLogUiModel, listener: ((WebSocketLogUiModel) -> Unit)?) {
        tvTitle.text = model.event
        tvDateTime.text = model.dateTime

        if (model.topchatDetailInfo != null) {
            tvSource.text = model.topchatDetailInfo.source
            tvUrl.text = model.topchatDetailInfo.url
        }

        itemView.setOnClickListener {
            listener?.invoke(model)
        }
    }
}
