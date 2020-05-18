package com.tokopedia.flight.filter.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.travel.constant.TravelSortOption
import com.tokopedia.flight.R
import com.tokopedia.flight.filter.presentation.FlightFilterSortListener
import com.tokopedia.flight.filter.presentation.model.BaseFilterSortModel
import com.tokopedia.flight.filter.presentation.model.FlightSortModel
import com.tokopedia.flight.filter.presentation.widget.FlightFilterSortFoldableWidget
import kotlinx.android.synthetic.main.item_flight_filter_sort.view.*

/**
 * @author by jessica on 2020-02-20
 */

class FlightSortViewHolder(view: View, val listener: FlightFilterSortListener, var selectedId: Int) : AbstractViewHolder<FlightSortModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_flight_filter_sort

        private const val MAX_ITEM_COUNT = 5
    }

    override fun bind(element: FlightSortModel) {
        with(itemView) {
            flightFilterSortWidget.titleText = resources.getString(R.string.flight_search_sort_dialog_title)
            flightFilterSortWidget.isSelectOnlyOneChip = true
            flightFilterSortWidget.hasShowMore = true
            flightFilterSortWidget.maxItemCount = MAX_ITEM_COUNT
            flightFilterSortWidget.isFlowLayout = false
            flightFilterSortWidget.hasShowMore = true
            flightFilterSortWidget.listener = object : FlightFilterSortFoldableWidget.ActionListener {
                override fun onChipStateChanged(items: List<BaseFilterSortModel>) {
                    selectedId = (items[0] as FlightSortModel).selectedOption
                    listener.onSortChanged((items[0] as FlightSortModel).selectedOption)
                }

                override fun onClickShowMore() {
                    listener.onClickSeeAllSort()
                }

            }
            flightFilterSortWidget.buildView(getSortItem())
        }
    }

    fun performClickOnSortId(selectedId: Int) {
        itemView.flightFilterSortWidget.performClickOnChipWithPosition(getSortIdByPosition(selectedId))
    }

    private fun getSortIdByPosition(selectedId: Int): Int {
        for ((index, item) in getSortItem().withIndex()) {
            if (item.selectedOption == selectedId) return index
        }
        return 0
    }

    fun resetView() {
        performClickOnSortId(TravelSortOption.CHEAPEST)
    }

    private fun getSortItem(): List<FlightSortModel> {
        return listOf(FlightSortModel(TravelSortOption.CHEAPEST, getString(R.string.flight_search_sort_item_cheapest_price), selectedId == TravelSortOption.CHEAPEST),
                FlightSortModel(TravelSortOption.MOST_EXPENSIVE, getString(R.string.flight_search_sort_item_most_expensive_price), selectedId == TravelSortOption.MOST_EXPENSIVE),
                FlightSortModel(TravelSortOption.EARLIEST_DEPARTURE, getString(R.string.flight_search_sort_item_earliest_departure), selectedId == TravelSortOption.EARLIEST_DEPARTURE),
                FlightSortModel(TravelSortOption.LATEST_DEPARTURE, getString(R.string.flight_search_sort_item_latest_departure), selectedId == TravelSortOption.LATEST_DEPARTURE),
                FlightSortModel(TravelSortOption.SHORTEST_DURATION, getString(R.string.flight_search_sort_shortest_duration), selectedId == TravelSortOption.SHORTEST_DURATION),
                FlightSortModel(TravelSortOption.EARLIEST_ARRIVAL, getString(R.string.flight_search_sort_item_earliest_arrival), selectedId == TravelSortOption.EARLIEST_ARRIVAL),
                FlightSortModel(TravelSortOption.LATEST_ARRIVAL, getString(R.string.flight_search_sort_item_latest_arrival), selectedId == TravelSortOption.LATEST_ARRIVAL)
        )
    }


}