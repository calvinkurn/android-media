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
                                          private var selectedAirline: List<String>)
    : AbstractViewHolder<FlightFilterAirlineModel>(view) {

    override fun bind(element: FlightFilterAirlineModel) {
        with(itemView) {
            flight_sort_widget.titleText = getString(R.string.flight_search_filter_airplane)
            flight_sort_widget.isSelectOnlyOneChip = false
            flight_sort_widget.hasShowMore = true
            flight_sort_widget.maxItemCount = 5
            flight_sort_widget.isFlowLayout = true
            flight_sort_widget.listener = object : FlightFilterSortFoldableWidget.ActionListener {
                override fun onChipStateChanged(items: List<BaseFilterSortModel>) {
                    // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onClickShowMore() {
                    listener.onClickSeeAllAirline()
                }
            }
            flight_sort_widget.buildView(getItems())
        }
    }

    fun onSelectedAirlineChanged(selectedAirline: List<String>) {
        this.selectedAirline = selectedAirline
    }

    private fun getItems(): List<FlightFilterAirlineModel> {
        return listener.getAirlineList().map {
            FlightFilterAirlineModel(
                    title = it.airlineDB.name,
                    isSelected = selectedAirline.contains(it.airlineDB.id)
            )
        }
    }
}