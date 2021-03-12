package com.tokopedia.analyticsdebugger.debugger.ui.viewholder

import androidx.annotation.LayoutRes

import android.text.TextUtils
import android.view.View
import android.widget.TextView

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.debugger.ui.model.AnalyticsDebuggerViewModel

/**
 * @author okasurya on 5/16/18.
 */
class AnalyticsDebuggerViewHolder(itemView: View) : AbstractViewHolder<AnalyticsDebuggerViewModel>(itemView) {

    private val eventName: TextView = itemView.findViewById(R.id.text_event_name)
    private val eventCategory: TextView = itemView.findViewById(R.id.text_event_category)
    private val data: TextView = itemView.findViewById(R.id.text_data_excerpt)
    private val timestamp: TextView = itemView.findViewById(R.id.text_timestamp)


    override fun bind(element: AnalyticsDebuggerViewModel) {
        val name = element.name
        if (TextUtils.isEmpty(name)) {
            eventName.visibility = View.GONE
        } else {
            eventName.text = name
            eventName.visibility = View.VISIBLE
        }

        val category = element.category
        if (TextUtils.isEmpty(category)) {
            eventCategory.visibility = View.GONE
        } else {
            eventCategory.text = category
            eventCategory.visibility = View.VISIBLE
        }
        data.text = element.dataExcerpt
        timestamp.text = element.timestamp
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_analytics_debugger
    }
}
