package com.tokopedia.topads.dashboard.view.adapter.beranda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.beranda.Chip
import com.tokopedia.unifycomponents.ChipsUnify

class AdPlacementRvAdapter(
    private val list: List<Chip>,
    private val itemClick: (Chip) -> Unit
) : RecyclerView.Adapter<AdPlacementRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.topads_dash_item_insight_tab_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[holder.adapterPosition]
        holder.chip.chipText = item.title
        holder.chip.chipType =
            if (item.isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL

        holder.chip.setOnClickListener {
            itemClick(item)
        }
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val chip: ChipsUnify = view.findViewById(R.id.tabInsightId)
    }
}