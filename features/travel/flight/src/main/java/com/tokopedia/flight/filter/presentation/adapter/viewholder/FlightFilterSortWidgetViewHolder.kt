package com.tokopedia.flight.filter.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.R
import com.tokopedia.flight.filter.presentation.model.BaseFilterSortModel
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.layout_flight_filter_sort_chip.view.*

/**
 * @author by jessica on 2020-02-19
 */

class FlightFilterSortWidgetViewHolder(itemView: View)
    : RecyclerView.ViewHolder(itemView) {

    fun bind(element: BaseFilterSortModel) {
        itemView.flightFilterSortChip.chipText = element.title
        itemView.flightFilterSortChip.chipType = ChipsUnify.TYPE_NORMAL
        itemView.flightFilterSortChip.chipSize = ChipsUnify.SIZE_MEDIUM
        if (element.isSelected) clickChip()
    }

    fun clickChip() {
        with(itemView) {
            if (flightFilterSortChip.chipType == ChipsUnify.TYPE_SELECTED) {
                unselectChip()
            } else flightFilterSortChip.chipType = ChipsUnify.TYPE_SELECTED
        }
    }

    fun isSelected(): Boolean = itemView.flightFilterSortChip.chipType == ChipsUnify.TYPE_SELECTED

    fun unselectChip() {
        with(itemView) {
            flightFilterSortChip.chipType = ChipsUnify.TYPE_NORMAL
            flightFilterSortChip.chip_text.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
        }
    }

    companion object {
        val LAYOUT = R.layout.layout_flight_filter_sort_chip
    }

}