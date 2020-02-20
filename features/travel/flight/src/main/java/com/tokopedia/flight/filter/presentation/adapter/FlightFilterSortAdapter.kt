package com.tokopedia.flight.filter.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.filter.presentation.data.BaseFilterSortModel
import com.tokopedia.flight.filter.presentation.adapter.viewholder.FlightFilterSortViewHolder

/**
 * @author by jessica on 2020-02-20
 */

class FlightFilterSortAdapter(val typeFactory: FlightFilterSortAdapterTypeFactory,
                              val items: MutableList<BaseFilterSortModel>,
                              val listener: ActionListener)
    : BaseAdapter<FlightFilterSortAdapterTypeFactory>(typeFactory, items as List<Visitable<*>>) {

    private var maxItemCount: Int = 5
    var isSelectOnlyOneChip: Boolean = false

    override fun onBindViewHolder(holder: AbstractViewHolder<out Visitable<*>>, position: Int) {
        if (position < maxItemCount) {
            super.onBindViewHolder(holder, position)
            holder.itemView.setOnClickListener {
                if (isSelectOnlyOneChip) resetAllSelectedChip()
                (holder as FlightFilterSortViewHolder).clickChip()
                onChipStateChanged(position, holder.isSelected())
            }
        }
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

    interface ActionListener{
        fun onResetChip()
        fun onChipStateChanged(items: List<BaseFilterSortModel>)
    }

}