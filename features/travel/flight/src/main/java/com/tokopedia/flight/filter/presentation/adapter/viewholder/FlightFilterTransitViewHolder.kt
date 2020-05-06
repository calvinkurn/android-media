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

class FlightFilterTransitViewHolder(view: View, val listener: FlightFilterSortListener,
                                    var selectedTransits: MutableList<TransitEnum>)
    : AbstractViewHolder<TransitModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_flight_filter_sort
    }

    override fun bind(element: TransitModel) {
        with(itemView) {
            flightFilterSortWidget.titleText = resources.getString(R.string.transit)
            flightFilterSortWidget.isSelectOnlyOneChip = false
            flightFilterSortWidget.hasShowMore = false
            flightFilterSortWidget.maxItemCount = 5
            flightFilterSortWidget.isFlowLayout = true
            flightFilterSortWidget.listener = object : FlightFilterSortFoldableWidget.ActionListener {
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
            flightFilterSortWidget.buildView(getTransitItem())
        }
    }

    private fun getSelectedByTransitEnum(transitEnum: TransitEnum): Boolean {
        for (selectedTransit in selectedTransits) {
            if (transitEnum.id == selectedTransit.id) return true
        }
        return false
    }

    private fun getTransitItem(): List<TransitModel> {
        val data = arrayListOf<TransitModel>()
        listener.getStatisticModel()?.let {
            for (item in it.transitTypeStatList) {
                data.add(generateTransitModelFromTransitEnum(item.transitType))
            }
        }
        return data
    }

    private fun generateTransitModelFromTransitEnum(transitEnum: TransitEnum): TransitModel =
            when (transitEnum) {
                TransitEnum.DIRECT -> TransitModel(transitEnum, getString(R.string.direct), getSelectedByTransitEnum(transitEnum))
                TransitEnum.ONE -> TransitModel(transitEnum, getString(R.string.one_transit_without_1), getSelectedByTransitEnum(transitEnum))
                TransitEnum.TWO -> TransitModel(transitEnum, getString(R.string.two_plus_transit), getSelectedByTransitEnum(transitEnum))
            }

    fun resetView() {
        selectedTransits = arrayListOf()
        for (item in itemView.flightFilterSortWidget.getItems()) {
            item.isSelected = false
        }
        itemView.flightFilterSortWidget.onResetChip()
        itemView.flightFilterSortWidget.notifyDataSetChanged()
    }

}