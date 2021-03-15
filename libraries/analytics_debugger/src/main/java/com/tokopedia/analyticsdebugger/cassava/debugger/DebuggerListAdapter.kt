package com.tokopedia.analyticsdebugger.cassava.debugger

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analyticsdebugger.database.GtmLogDB

class DebuggerListAdapter() : ListAdapter<GtmLogDB, DebuggerListViewHolder>(DIFF_CALLBACK) {

    private var itemListener: ((GtmLogDB) -> Unit)? = null

    fun setItemClickListener(listener: (GtmLogDB) -> Unit) {
        itemListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebuggerListViewHolder {
        val vh = LayoutInflater.from(parent.context)
                .inflate(DebuggerListViewHolder.LAYOUT, parent, false)
        return DebuggerListViewHolder(vh).apply {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    itemListener?.invoke(getItem(adapterPosition))
                }
            }
        }
    }

    override fun onBindViewHolder(holder: DebuggerListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: List<GtmLogDB>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GtmLogDB>() {
            override fun areItemsTheSame(oldItem: GtmLogDB, newItem: GtmLogDB): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GtmLogDB, newItem: GtmLogDB): Boolean {
                return oldItem == newItem
            }
        }
    }
}