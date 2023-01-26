package com.tokopedia.analyticsdebugger.websocket.ui.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.websocket.ui.adapter.WebSocketViewHolder
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogUiModel
import com.tokopedia.unifyprinciples.Typography

/**
 * Created By : Jonathan Darwin on December 03, 2021
 */
class PlayWebSocketLogViewHolder(itemView: View): WebSocketViewHolder(itemView) {

    private val tvTitle = itemView.findViewById<Typography>(R.id.tv_websocket_log_title)
    private val tvSource = itemView.findViewById<Typography>(R.id.tv_websocket_log_source)
    private val tvDateTime = itemView.findViewById<Typography>(R.id.tv_websocket_log_date_time)
    private val tvChannelId = itemView.findViewById<Typography>(R.id.tv_websocket_log_channel_id)
    private val tvGcToken = itemView.findViewById<Typography>(R.id.tv_websocket_log_gc_token)

    override fun bind(model: WebSocketLogUiModel, listener: ((WebSocketLogUiModel) -> Unit)?) {
        tvTitle.text = model.event
        tvDateTime.text = model.dateTime

        if (model.playGeneralInfo != null) {
            tvSource.text = model.playGeneralInfo.source
            tvChannelId.text = model.playGeneralInfo.channelId
            tvGcToken.text = model.playGeneralInfo.gcToken
        }
        
        itemView.setOnClickListener { 
            listener?.invoke(model)
        }
    }
}
