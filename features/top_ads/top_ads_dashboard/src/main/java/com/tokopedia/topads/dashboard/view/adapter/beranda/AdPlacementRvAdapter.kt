package com.tokopedia.topads.dashboard.view.adapter.beranda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.Chip
import com.tokopedia.unifycomponents.ChipsUnify

class AdPlacementRvAdapter(private val itemClick: (Chip) -> Unit) :
    RecyclerView.Adapter<AdPlacementRvAdapter.ViewHolder>() {

    private val list = mutableListOf<Chip>()
    private var lastSelectedItem: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.topads_dash_item_insight_tab_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[holder.adapterPosition]
        holder.chip.chipText = item.title

        holder.chip.setOnClickListener {
            val list = list
            item.isSelected = true
            holder.chip.chipType = ChipsUnify.TYPE_SELECTED

            lastSelectedItem?.let {
                list[it].isSelected = false
                notifyItemChanged(it)
            }
            lastSelectedItem = holder.adapterPosition
            itemClick(item)
        }
    }

    override fun getItemCount() = list.size

    fun addItems(list: List<Chip>) {
        list.forEach { this.list.add(it) }
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val chip = view.findViewById<ChipsUnify>(R.id.tabInsightId)
    }

    companion object {
        fun createInstance(itemClick: (Chip) -> Unit) = AdPlacementRvAdapter(itemClick)
    }
}