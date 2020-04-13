package com.tokopedia.analyticsdebugger.debugger.ui.viewholder

import android.text.TextUtils
import android.view.View
import android.widget.TextView

import androidx.annotation.LayoutRes

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.debugger.ui.model.TopAdsDebuggerViewModel

class TopAdsDebuggerViewHolder(itemView: View) : AbstractViewHolder<TopAdsDebuggerViewModel>(itemView) {

    private val urlText: TextView
    private val eventTypeText: TextView
    private val sourceNameText: TextView
    private val timestamp: TextView


    init {
        urlText = itemView.findViewById(R.id.topads_text_url)
        eventTypeText = itemView.findViewById(R.id.topads_text_event_type)
        sourceNameText = itemView.findViewById(R.id.topads_text_source_name)
        timestamp = itemView.findViewById(R.id.topads_text_timestamp)
    }

    override fun bind(element: TopAdsDebuggerViewModel) {
        urlText.text = element.url
        eventTypeText.text = element.eventType
        sourceNameText.text = element.sourceName
        timestamp.text = element.timestamp
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_topads_debugger
    }
}
