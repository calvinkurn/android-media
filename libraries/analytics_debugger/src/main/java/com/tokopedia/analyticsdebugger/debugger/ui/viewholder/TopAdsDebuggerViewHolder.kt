package com.tokopedia.analyticsdebugger.debugger.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.debugger.helper.getTopAdsStatusColor
import com.tokopedia.analyticsdebugger.debugger.ui.model.TopAdsDebuggerViewModel
import kotlinx.android.synthetic.main.item_topads_debugger.view.*

class TopAdsDebuggerViewHolder(itemView: View) : AbstractViewHolder<TopAdsDebuggerViewModel>(itemView) {

    override fun bind(element: TopAdsDebuggerViewModel) {
        itemView.viewHolderUrlText.text = element.previewUrl
        itemView.viewHolderEventTypeText.text = element.eventType
        itemView.viewHolderSourceNameText.text = element.sourceName
        itemView.viewHolderProductIdText.text = element.productId + " - " + element.productName
        itemView.viewHolderTimestampText.text = element.timestamp
        itemView.viewHolderStatusText.text = element.eventStatus
        itemView.viewHolderStatusText.setTextColor(getTopAdsStatusColor(itemView.context, element.eventStatus))
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_topads_debugger
    }
}
