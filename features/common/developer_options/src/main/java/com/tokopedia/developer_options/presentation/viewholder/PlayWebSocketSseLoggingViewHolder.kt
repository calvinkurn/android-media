package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analyticsdebugger.sse.ui.activity.SSELoggingActivity
import com.tokopedia.analyticsdebugger.websocket.ui.activity.WebSocketLoggingActivity
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogPageSource
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.PlayWebSocketSseLoggingUiModel
import com.tokopedia.unifycomponents.UnifyButton

class PlayWebSocketSseLoggingViewHolder(
    itemView: View
): AbstractViewHolder<PlayWebSocketSseLoggingUiModel>(itemView)
{
    private val btnViewSSELogging = itemView.findViewById<UnifyButton>(R.id.view_sse_logging_btn)
    private val btnViewWebSocketLogging = itemView.findViewById<UnifyButton>(R.id.view_websocket_logging_btn)

    init {
        btnViewSSELogging.setOnClickListener {
            itemView.context.apply {
                startActivity(SSELoggingActivity.newInstance(this))
            }
        }
        btnViewWebSocketLogging.setOnClickListener {
            itemView.context.apply {
                startActivity(WebSocketLoggingActivity.newInstance(this, WebSocketLogPageSource.PLAY))
            }
        }
    }
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_play_web_socket_sse_logging
    }

    override fun bind(element: PlayWebSocketSseLoggingUiModel) {
    }
}
