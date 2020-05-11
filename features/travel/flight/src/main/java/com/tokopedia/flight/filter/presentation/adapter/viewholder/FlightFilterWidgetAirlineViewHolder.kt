package com.tokopedia.flight.filter.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.filter.presentation.FlightFilterSortListener
import com.tokopedia.flight.filter.presentation.model.BaseFilterSortModel
import com.tokopedia.flight.filter.presentation.model.FlightFilterAirlineModel
import com.tokopedia.flight.filter.presentation.widget.FlightFilterSortFoldableWidget
import kotlinx.android.synthetic.main.item_flight_filter_sort.view.*

/**
 * @author by furqan on 21/02/2020
 */
class FlightFilterWidgetAirlineViewHolder(view: View,
                                          private val listener: FlightFilterSortListener,
                                          var selectedAirline: List<String>)
    : AbstractViewHolder<FlightFilterAirlineModel>(view) {

    override fun bind(element: FlightFilterAirlineModel) {
        with(itemView) {
            flightFilterSortWidget.titleText = getString(R.string.flight_search_filter_airplane)
            flightFilterSortWidget.isSelectOnlyOneChip = false
            flightFilterSortWidget.hasShowMore = true
            flightFilterSortWidget.maxItemCount = MAX_ITEM_SHOWED
            flightFilterSortWidget.isFlowLayout = true
            flightFilterSortWidget.listener = object : FlightFilterSortFoldableWidget.ActionListener {
                override fun onChipStateChanged(items: List<BaseFilterSortModel>) {
                    selectedAirline = (items as List<FlightFilterAirlineModel>).map {
                        it.airlineId
                    }.toList()
                    listener.onAirlineChanged(selectedAirline)
                }

                override fun onClickShowMore() {
                    listener.onClickSeeAllAirline()
                }
            }
            flightFilterSortWidget.buildView(getItems())
        }
    }

    fun onSelectedAirlineChanged(selectedAirline: List<String>) {
        this.selectedAirline = selectedAirline
        itemView.flightFilterSortWidget.getItems().let {
            for (item in it) {
                item.isSelected = this.selectedAirline.contains((item as FlightFilterAirlineModel).airlineId)
            }
        }
        itemView.flightFilterSortWidget.onResetChip()
        itemView.flightFilterSortWidget.notifyDataSetChanged()
    }

    private fun getItems(): List<FlightFilterAirlineModel> {
        return listener.getAirlineList().map {
            FlightFilterAirlineModel(
                    airlineId = it.airlineDB.id,
                    title = it.airlineDB.name,
                    isSelected = selectedAirline.contains(it.airlineDB.id)
            )
        }
    }

    fun resetView() {
        selectedAirline = arrayListOf()
        for (item in itemView.flightFilterSortWidget.getItems()) {
            item.isSelected = false
        }
        itemView.flightFilterSortWidget.onResetChip()
        itemView.flightFilterSortWidget.notifyDataSetChanged()
    }

    companion object {
        const val MAX_ITEM_SHOWED = 5
    }
}