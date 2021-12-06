package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analyticsdebugger.sse.ui.activity.SSELoggingActivity.Companion.newInstance
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.PlayWebSocketSseLoggingUiModel
import com.tokopedia.unifycomponents.UnifyButton

class PlayWebSocketSseLoggingViewHolder(
    itemView: View
): AbstractViewHolder<PlayWebSocketSseLoggingUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_play_web_socket_sse_logging
    }

    override fun bind(element: PlayWebSocketSseLoggingUiModel) {
        val btn = itemView.findViewById<UnifyButton>(R.id.view_sse_logging_btn)
        btn.setOnClickListener {
            itemView.context.apply {
                startActivity(newInstance(this))
            }
        }
    }
}