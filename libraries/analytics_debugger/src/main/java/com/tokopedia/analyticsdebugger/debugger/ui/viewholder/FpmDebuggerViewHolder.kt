package com.tokopedia.analyticsdebugger.debugger.ui.viewholder

import android.text.TextUtils
import android.view.View
import android.widget.TextView

import androidx.annotation.LayoutRes

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.debugger.ui.model.FpmDebuggerViewModel

class FpmDebuggerViewHolder(itemView: View) : AbstractViewHolder<FpmDebuggerViewModel>(itemView) {

    private val name: TextView
    private val duration: TextView
    private val metrics: TextView
    private val txtAtrributes: TextView
    private val timestamp: TextView


    init {

        name = itemView.findViewById(R.id.fpm_text_name)
        duration = itemView.findViewById(R.id.fpm_text_duration)
        metrics = itemView.findViewById(R.id.fpm_text_metrics)
        txtAtrributes = itemView.findViewById(R.id.fpm_text_attributes)
        timestamp = itemView.findViewById(R.id.fpm_text_timestamp)
    }

    override fun bind(element: FpmDebuggerViewModel) {
        val itemName = element.name
        if (TextUtils.isEmpty(itemName)) {
            name.visibility = View.GONE
        } else {
            name.text = itemName
            name.visibility = View.VISIBLE
        }

        duration.text = String.format("Duration: %dms", element.duration)
        metrics.text = String.format("Metrics: %s", element.previewMetrics)
        txtAtrributes.text = String.format("Attributes: %s", element.previewAttributes)
        timestamp.text = element.timestamp
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_fpm_debugger
    }
}
