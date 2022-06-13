package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analyticsdebugger.cassava.ui.debugger.AnalyticsDebuggerActivity.Companion.newInstance
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.ViewAnalyticsLogUiModel
import com.tokopedia.unifycomponents.UnifyButton

class ViewAnalyticsLogViewHolder(
    itemView: View
): AbstractViewHolder<ViewAnalyticsLogUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_view_analytics_log
    }

    override fun bind(element: ViewAnalyticsLogUiModel) {
        val btn = itemView.findViewById<UnifyButton>(R.id.view_analytics_log_btn)
        btn.setOnClickListener {
            itemView.context.apply { startActivity(newInstance(this)) }
        }
    }
}