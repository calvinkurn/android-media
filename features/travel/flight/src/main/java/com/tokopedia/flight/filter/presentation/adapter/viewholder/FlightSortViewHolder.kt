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

class FlightSortViewHolder(val view: View, val listener: FlightFilterSortListener, var selectedId: Int) : AbstractViewHolder<FlightSortModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_flight_filter_sort
    }

    override fun bind(element: FlightSortModel) {
        with(itemView) {
            flight_sort_widget.titleText = resources.getString(R.string.flight_search_sort_dialog_title)
            flight_sort_widget.isSelectOnlyOneChip = true
            flight_sort_widget.hasShowMore = true
            flight_sort_widget.maxItemCount = 5
            flight_sort_widget.isFlowLayout = false
            flight_sort_widget.hasShowMore = true
            flight_sort_widget.listener = object : FlightFilterSortFoldableWidget.ActionListener {
                override fun onChipStateChanged(items: List<BaseFilterSortModel>) {
                    selectedId = (items[0] as FlightSortModel).selectedOption
                    listener.onSortChanged((items[0] as FlightSortModel).selectedOption)
                }

                override fun onClickShowMore() {
                    listener.onClickSeeAllSort()
                }

            }
            flight_sort_widget.buildView(getSortItem())
        }
    }

    fun performClickOnSortId(selectedId: Int) {
        itemView.flight_sort_widget.performClickOnChipWithPosition(getSortIdByPosition(selectedId))
    }

    fun getSortIdByPosition(selectedId: Int): Int {
        for ((index, item) in getSortItem().withIndex()) {
            if (item.selectedOption == selectedId) return index
        }
        return 0
    }

    private fun getSortItem(): List<FlightSortModel> {
        return listOf(FlightSortModel(TravelSortOption.CHEAPEST, getString(R.string.flight_search_sort_item_cheapest_price), selectedId == TravelSortOption.CHEAPEST),
                FlightSortModel(TravelSortOption.MOST_EXPENSIVE, getString(R.string.flight_search_sort_item_most_expensive_price), selectedId == TravelSortOption.MOST_EXPENSIVE),
                FlightSortModel(TravelSortOption.EARLIEST_DEPARTURE, getString(R.string.flight_search_sort_item_earliest_departure), selectedId == TravelSortOption.EARLIEST_DEPARTURE),
                FlightSortModel(TravelSortOption.LATEST_DEPARTURE, getString(R.string.flight_search_sort_item_latest_departure), selectedId == TravelSortOption.LATEST_DEPARTURE),
                FlightSortModel(TravelSortOption.SHORTEST_DURATION, getString(R.string.flight_search_sort_item_shortest_duration), selectedId == TravelSortOption.SHORTEST_DURATION),
                FlightSortModel(TravelSortOption.LONGEST_DURATION, getString(R.string.flight_search_sort_item_longest_duration), selectedId == TravelSortOption.LONGEST_DURATION),
                FlightSortModel(TravelSortOption.EARLIEST_ARRIVAL, getString(R.string.flight_search_sort_item_earliest_arrival), selectedId == TravelSortOption.EARLIEST_ARRIVAL),
                FlightSortModel(TravelSortOption.LATEST_ARRIVAL, getString(R.string.flight_search_sort_item_latest_arrival), selectedId == TravelSortOption.LATEST_ARRIVAL))
    }

}