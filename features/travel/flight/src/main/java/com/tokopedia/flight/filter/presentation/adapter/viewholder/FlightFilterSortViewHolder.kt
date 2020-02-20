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

class FlightFilterSortViewHolder(itemView: View)
    : RecyclerView.ViewHolder(itemView) {

    fun bind(element: BaseFilterSortModel) {
        itemView.chips.chipText = element.title
        itemView.chips.chipType = ChipsUnify.TYPE_NORMAL
        itemView.chips.chipSize = ChipsUnify.SIZE_MEDIUM
        if (element.isSelected) clickChip()
    }

    fun clickChip() {
        with(itemView) {
            if (chips.chipType == ChipsUnify.TYPE_SELECTED) {
                unselectChip()
            } else chips.chipType = ChipsUnify.TYPE_SELECTED
        }
    }

    fun isSelected(): Boolean = itemView.chips.chipType == ChipsUnify.TYPE_SELECTED

    fun unselectChip() {
        with(itemView) {
            chips.chipType = ChipsUnify.TYPE_NORMAL
            chips.chip_text.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Neutral_N700_68))
        }
    }

    companion object {
        val LAYOUT = R.layout.layout_flight_filter_sort_chip
    }

}