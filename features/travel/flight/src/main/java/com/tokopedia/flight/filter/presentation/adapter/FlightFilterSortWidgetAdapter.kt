package com.tokopedia.flight.filter.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.filter.presentation.adapter.viewholder.FlightFilterSortWidgetViewHolder
import com.tokopedia.flight.filter.presentation.model.BaseFilterSortModel

/**
 * @author by jessica on 2020-02-20
 */

class FlightFilterSortWidgetAdapter(val items: MutableList<BaseFilterSortModel>,
                                    val listener: ActionListener)
    : RecyclerView.Adapter<FlightFilterSortWidgetViewHolder>() {

    var maxItemCount: Int = 5
    var isSelectOnlyOneChip: Boolean = false

    override fun onBindViewHolder(holder: FlightFilterSortWidgetViewHolder, position: Int) {
        if (position < maxItemCount) {
            holder.bind(items[position])
            holder.itemView.setOnClickListener {
                if (isSelectOnlyOneChip) resetAllSelectedChip()
                holder.clickChip()
                onChipStateChanged(position, holder.isSelected())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightFilterSortWidgetViewHolder {
        return FlightFilterSortWidgetViewHolder(LayoutInflater.from(parent.context).inflate(FlightFilterSortWidgetViewHolder.LAYOUT, parent, false))
    }

    private fun onChipStateChanged(position: Int, isSelected: Boolean) {
        items[position].isSelected = isSelected

        val selectedItems = mutableListOf<BaseFilterSortModel>()
        for (item in items) {
            if (item.isSelected) selectedItems.add(item)
        }
        listener.onChipStateChanged(selectedItems)
    }

    fun resetAllSelectedChip() {
        for (item in items) {
            item.isSelected = false
        }
        listener.onResetChip()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = if (items.size > maxItemCount) maxItemCount else items.size

    interface ActionListener {
        fun onResetChip()
        fun onChipStateChanged(items: List<BaseFilterSortModel>)
    }

}