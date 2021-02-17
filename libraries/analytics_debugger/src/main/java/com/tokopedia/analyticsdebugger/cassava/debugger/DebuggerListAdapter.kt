package com.tokopedia.analyticsdebugger.cassava.debugger

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.analyticsdebugger.database.GtmLogDB
import com.tokopedia.analyticsdebugger.debugger.ui.viewholder.AnalyticsDebuggerViewHolder

class DebuggerListAdapter() : ListAdapter<GtmLogDB, DebuggerListViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebuggerListViewHolder {
        val vh = LayoutInflater.from(parent.context).inflate(AnalyticsDebuggerViewHolder.LAYOUT, parent, false)
        return DebuggerListViewHolder(vh)
    }

    override fun onBindViewHolder(holder: DebuggerListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: List<GtmLogDB>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<GtmLogDB>() {
            override fun areItemsTheSame(oldItem: GtmLogDB, newItem: GtmLogDB): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GtmLogDB, newItem: GtmLogDB): Boolean {
                return oldItem == newItem
            }
        }
    }
}