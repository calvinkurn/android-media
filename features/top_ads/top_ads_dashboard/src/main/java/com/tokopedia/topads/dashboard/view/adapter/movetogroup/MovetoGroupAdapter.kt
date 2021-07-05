package com.tokopedia.topads.dashboard.view.adapter.movetogroup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.data.model.CountDataItem
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewholder.MovetoGroupViewHolder
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewmodel.MovetoGroupModel


class MovetoGroupAdapter(val typeFactory: MovetoGroupAdapterTypeFactory) : RecyclerView.Adapter<MovetoGroupViewHolder<MovetoGroupModel>>() {

    private var lastSelected: Int = 0
    var items: MutableList<MovetoGroupModel> = mutableListOf()
    private var countList: MutableList<CountDataItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovetoGroupViewHolder<MovetoGroupModel> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.holder(viewType, view) as MovetoGroupViewHolder<MovetoGroupModel>
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type(typeFactory)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: MovetoGroupViewHolder<MovetoGroupModel>, position: Int) {
        holder.bind(items[position], lastSelected, countList)
    }

    fun updateData(data: MutableList<MovetoGroupModel>) {
        items = data
        notifyDataSetChanged()
    }

    fun setItemCount(countList: List<CountDataItem>) {
        this.countList = countList.toMutableList()
        notifyDataSetChanged()
    }

    fun setLastSelected(pos: Int) {
        lastSelected = pos
        notifyDataSetChanged()
    }
}