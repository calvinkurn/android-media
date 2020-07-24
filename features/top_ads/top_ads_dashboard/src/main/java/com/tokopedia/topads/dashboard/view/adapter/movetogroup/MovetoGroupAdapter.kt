package com.tokopedia.topads.dashboard.view.adapter.movetogroup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewholder.MovetoGroupViewHolder
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewmodel.MovetoGroupViewModel


class MovetoGroupAdapter(val typeFactory: MovetoGroupAdapterTypeFactory) : RecyclerView.Adapter<MovetoGroupViewHolder<MovetoGroupViewModel>>() {

    private var lastSelected: Int = 0
    var items: MutableList<MovetoGroupViewModel> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovetoGroupViewHolder<MovetoGroupViewModel> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.holder(viewType, view) as MovetoGroupViewHolder<MovetoGroupViewModel>
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type(typeFactory)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: MovetoGroupViewHolder<MovetoGroupViewModel>, position: Int) {
        holder.bind(items[position], lastSelected)
    }

    fun updateData(data: MutableList<MovetoGroupViewModel>) {
        items = data
        notifyDataSetChanged()
    }

    fun setLastSelected(pos: Int) {
        lastSelected = pos
        notifyDataSetChanged()
    }
}