package com.tokopedia.flight.filter.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.filter.presentation.FlightFilterSortListener
import com.tokopedia.flight.filter.presentation.model.BaseFilterSortModel
import com.tokopedia.flight.filter.presentation.model.TransitModel
import com.tokopedia.flight.filter.presentation.widget.FlightFilterSortFoldableWidget
import com.tokopedia.flight.search.presentation.model.filter.TransitEnum
import kotlinx.android.synthetic.main.item_flight_filter_sort.view.*

/**
 * @author by jessica on 2020-02-21
 */

class FlightFilterTransitViewHolder(view: View, val listener: FlightFilterSortListener, var selectedTransits: List<TransitEnum>) : AbstractViewHolder<TransitModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_flight_filter_sort
    }

    override fun bind(element: TransitModel) {
        with(itemView) {
            flight_sort_widget.titleText = resources.getString(R.string.transit)
            flight_sort_widget.isSelectOnlyOneChip = false
            flight_sort_widget.hasShowMore = false
            flight_sort_widget.maxItemCount = 5
            flight_sort_widget.isFlowLayout = true
            flight_sort_widget.listener = object : FlightFilterSortFoldableWidget.ActionListener {
                override fun onChipStateChanged(items: List<BaseFilterSortModel>) {
                    var transits = mutableListOf<TransitEnum>()
                    items.forEach {
                        transits.add((it as TransitModel).transitEnum)
                    }
                    selectedTransits = transits
                    listener.onTransitFilterChanged(transits)
                }

                override fun onClickShowMore() {
                    //do nothing
                }
            }
            flight_sort_widget.buildView(getTransitItem())
        }
    }

    private fun getSelectedByTransitEnum(transitEnum: TransitEnum): Boolean {
        for (selectedTransit in selectedTransits) {
            if (transitEnum.id == selectedTransit.id) return true
        }
        return false
    }

    private fun getTransitItem(): List<TransitModel> {
        return listOf(TransitModel(TransitEnum.DIRECT, getString(R.string.direct), getSelectedByTransitEnum(TransitEnum.DIRECT)),
                TransitModel(TransitEnum.ONE, getString(R.string.one_trasit), getSelectedByTransitEnum(TransitEnum.ONE)),
                TransitModel(TransitEnum.TWO, getString(R.string.two_transit), getSelectedByTransitEnum(TransitEnum.TWO)),
                TransitModel(TransitEnum.THREE_OR_MORE, getString(R.string.more_than_2_transit), getSelectedByTransitEnum(TransitEnum.THREE_OR_MORE)))
    }

}