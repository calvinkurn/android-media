package com.tokopedia.analyticsdebugger.cassava.debugger

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.database.GtmLogDB
import com.tokopedia.analyticsdebugger.debugger.helper.formatDataExcerpt
import com.tokopedia.analyticsdebugger.cassava.validator.Utils

class DebuggerListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val eventName: TextView = itemView.findViewById(R.id.text_event_name)
    private val data: TextView = itemView.findViewById(R.id.text_data_excerpt)
    private val timestamp: TextView = itemView.findViewById(R.id.text_timestamp)

    fun bind(element: GtmLogDB) {
        val name = element.name
        if (TextUtils.isEmpty(name)) {
            eventName.visibility = View.GONE
        } else {
            eventName.text = name
            eventName.visibility = View.VISIBLE
        }
        data.text = formatDataExcerpt(element.data)
        timestamp.text = Utils.getTimeStampFormat(element.timestamp)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_debugger_list
    }
}