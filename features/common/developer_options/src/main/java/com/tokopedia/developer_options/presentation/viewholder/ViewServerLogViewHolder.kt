package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analyticsdebugger.debugger.ServerLogLogger
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.ViewServerLogUiModel
import com.tokopedia.unifycomponents.UnifyButton

class ViewServerLogViewHolder(
    itemView: View
): AbstractViewHolder<ViewServerLogUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_view_server_log
    }

    override fun bind(element: ViewServerLogUiModel) {
        val btn = itemView.findViewById<UnifyButton>(R.id.view_server_log_btn)
        itemView.context.apply {
            btn.setOnClickListener {
                ServerLogLogger.getInstance(this).openActivity()
            }
        }
    }
}