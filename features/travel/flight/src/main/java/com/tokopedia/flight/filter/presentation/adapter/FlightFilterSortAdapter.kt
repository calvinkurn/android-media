package com.tokopedia.flight.filter.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.filter.presentation.adapter.viewholder.FlightFilterSortViewHolder
import com.tokopedia.flight.filter.presentation.model.BaseFilterSortModel

/**
 * @author by jessica on 2020-02-20
 */

class FlightFilterSortAdapter(val typeFactory: FlightFilterSortAdapterTypeFactory,
                              val items: MutableList<BaseFilterSortModel>,
                              val listener: ActionListener)
    : RecyclerView.Adapter<FlightFilterSortViewHolder>() {

    var maxItemCount: Int = 5
    var isSelectOnlyOneChip: Boolean = false

    override fun onBindViewHolder(holder: FlightFilterSortViewHolder, position: Int) {
        if (position < maxItemCount) {
            holder.bind(items[position])
            holder.itemView.setOnClickListener {
                if (isSelectOnlyOneChip) resetAllSelectedChip()
                holder.clickChip()
                onChipStateChanged(position, holder.isSelected())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightFilterSortViewHolder {
        return FlightFilterSortViewHolder(LayoutInflater.from(parent.context).inflate(FlightFilterSortViewHolder.LAYOUT, parent, false))
    }

    private fun onChipStateChanged(position: Int, isSelected: Boolean) {
        items[position].isSelected = isSelected

        val selectedItems = mutableListOf<BaseFilterSortModel>()
        for (item in items) {
            if (item.isSelected) selectedItems.add(item)
        }
        listener.onChipStateChanged(selectedItems)
    }

    private fun resetAllSelectedChip() {
        listener.onResetChip()
    }

    override fun getItemCount(): Int = if (items.size > maxItemCount) maxItemCount else items.size

    interface ActionListener {
        fun onResetChip()
        fun onChipStateChanged(items: List<BaseFilterSortModel>)
    }

}