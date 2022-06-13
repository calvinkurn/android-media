package com.tokopedia.analyticsdebugger.websocket.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogUiModel
import com.tokopedia.unifyprinciples.Typography

/**
 * Created By : Jonathan Darwin on December 03, 2021
 */
class WebSocketLogViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val tvTitle = itemView.findViewById<Typography>(R.id.tv_websocket_log_title)
    private val tvSource = itemView.findViewById<Typography>(R.id.tv_websocket_log_source)
    private val tvDateTime = itemView.findViewById<Typography>(R.id.tv_websocket_log_date_time)
    private val tvChannelId = itemView.findViewById<Typography>(R.id.tv_websocket_log_channel_id)
    private val tvGcToken = itemView.findViewById<Typography>(R.id.tv_websocket_log_gc_token)

    fun bind(webSocketLogUiModel: WebSocketLogUiModel, listener: ((WebSocketLogUiModel) -> Unit)?) {

        tvTitle.text = webSocketLogUiModel.event
        tvSource.text = webSocketLogUiModel.generalInfo.source
        tvDateTime.text = webSocketLogUiModel.dateTime
        tvChannelId.text = webSocketLogUiModel.generalInfo.channelId
        tvGcToken.text = webSocketLogUiModel.generalInfo.gcToken
        
        itemView.setOnClickListener { 
            listener?.invoke(webSocketLogUiModel)
        }
    }
}