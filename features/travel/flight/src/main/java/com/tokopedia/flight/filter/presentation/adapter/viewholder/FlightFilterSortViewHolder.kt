package com.tokopedia.flight.filter.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.filter.presentation.adapter.FlightFilterSortAdapterTypeFactory
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.layout_flight_filter_sort_chip.view.*

/**
 * @author by jessica on 2020-02-19
 */

abstract class FlightFilterSortViewHolder<T : Visitable<FlightFilterSortAdapterTypeFactory>>(itemView: View)
    : AbstractViewHolder<T>(itemView) {

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