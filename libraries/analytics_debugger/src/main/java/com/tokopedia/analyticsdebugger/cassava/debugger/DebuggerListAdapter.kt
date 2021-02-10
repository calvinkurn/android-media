package com.tokopedia.analyticsdebugger.cassava.debugger

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analyticsdebugger.database.GtmLogDB
import com.tokopedia.analyticsdebugger.debugger.ui.viewholder.AnalyticsDebuggerViewHolder

class DebuggerListAdapter() : RecyclerView.Adapter<DebuggerListViewHolder>() {

    private val data: MutableList<GtmLogDB> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebuggerListViewHolder {
        val vh = LayoutInflater.from(parent.context).inflate(AnalyticsDebuggerViewHolder.LAYOUT, parent, false)
        return DebuggerListViewHolder(vh)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: DebuggerListViewHolder, position: Int) {
        holder.bind(data[position])
    }

    fun submitList(logs: List<GtmLogDB>) {
        data.clear()
        data.addAll(logs)
        notifyDataSetChanged()
    }
}