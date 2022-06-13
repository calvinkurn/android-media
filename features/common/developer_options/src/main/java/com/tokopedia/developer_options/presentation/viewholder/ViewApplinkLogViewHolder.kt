package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analyticsdebugger.debugger.ApplinkLogger
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.ViewApplinkLogUiModel
import com.tokopedia.unifycomponents.UnifyButton

class ViewApplinkLogViewHolder(
    itemView: View
): AbstractViewHolder<ViewApplinkLogUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_view_applink_log
    }

    override fun bind(element: ViewApplinkLogUiModel) {
        val btn = itemView.findViewById<UnifyButton>(R.id.view_applink_log_btn)
        btn.setOnClickListener {
            ApplinkLogger.getInstance(itemView.context).openActivity()
        }
    }
}