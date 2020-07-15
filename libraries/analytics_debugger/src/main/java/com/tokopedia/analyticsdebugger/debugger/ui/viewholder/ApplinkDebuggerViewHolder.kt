package com.tokopedia.analyticsdebugger.debugger.ui.viewholder

import android.view.View
import android.widget.TextView

import androidx.annotation.LayoutRes

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.debugger.ui.model.ApplinkDebuggerViewModel

class ApplinkDebuggerViewHolder(itemView: View) : AbstractViewHolder<ApplinkDebuggerViewModel>(itemView) {

    private val applinkName: TextView
    private val traces: TextView
    private val timestamp: TextView


    init {

        applinkName = itemView.findViewById(R.id.applink_text_name)
        traces = itemView.findViewById(R.id.applink_text_traces)
        timestamp = itemView.findViewById(R.id.applink_text_timestamp)
    }

    override fun bind(element: ApplinkDebuggerViewModel) {
        applinkName.text = element.applink
        traces.text = element.previewTrace
        timestamp.text = element.timestamp
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_applink_debugger
    }
}
