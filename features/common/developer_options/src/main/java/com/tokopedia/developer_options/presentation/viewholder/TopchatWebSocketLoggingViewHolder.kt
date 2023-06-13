package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analyticsdebugger.websocket.ui.activity.WebSocketLoggingActivity
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogPageSource
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.TopchatWebSocketLoggingUiModel
import com.tokopedia.unifycomponents.UnifyButton

class TopchatWebSocketLoggingViewHolder(
    itemView: View
): AbstractViewHolder<TopchatWebSocketLoggingUiModel>(itemView)
{
    private val btnViewWebSocketLogging = itemView.findViewById<UnifyButton>(R.id.view_websocket_logging_btn)

    init {
        btnViewWebSocketLogging.setOnClickListener {
            itemView.context.apply {
                startActivity(WebSocketLoggingActivity.newInstance(this, WebSocketLogPageSource.TOPCHAT))
            }
        }
    }
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_topchat_websocket_logging
    }

    override fun bind(element: TopchatWebSocketLoggingUiModel) = Unit
}

