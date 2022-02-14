package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analyticsdebugger.debugger.TopAdsLogger
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.ViewTopAdsLogUiModel
import com.tokopedia.unifycomponents.UnifyButton

class ViewTopAdsLogViewHolder(
    itemView: View
): AbstractViewHolder<ViewTopAdsLogUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_view_topads_log
    }

    override fun bind(element: ViewTopAdsLogUiModel) {
        val btn = itemView.findViewById<UnifyButton>(R.id.view_topads_log_btn)
        btn.setOnClickListener {
            TopAdsLogger.getInstance(itemView.context).openActivity()
        }
    }
}