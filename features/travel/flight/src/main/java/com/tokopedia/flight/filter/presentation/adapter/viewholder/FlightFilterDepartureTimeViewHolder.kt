package com.tokopedia.flight.filter.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.filter.presentation.FlightFilterSortListener
import com.tokopedia.flight.filter.presentation.model.BaseFilterSortModel
import com.tokopedia.flight.filter.presentation.model.DepartureTimeModel
import com.tokopedia.flight.filter.presentation.widget.FlightFilterSortFoldableWidget
import com.tokopedia.flight.search.presentation.model.filter.DepartureTimeEnum
import kotlinx.android.synthetic.main.item_flight_filter_sort.view.*

/**
 * @author by jessica on 2020-02-21
 */

class FlightFilterDepartureTimeViewHolder(view: View, val listener: FlightFilterSortListener, var selectedDepartureTime: List<DepartureTimeEnum>) : AbstractViewHolder<DepartureTimeModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_flight_filter_sort
    }

    override fun bind(element: DepartureTimeModel) {
        with(itemView) {
            flightFilterSortWidget.titleText = resources.getString(R.string.flight_search_filter_departure_time)
            flightFilterSortWidget.isSelectOnlyOneChip = false
            flightFilterSortWidget.hasShowMore = false
            flightFilterSortWidget.maxItemCount = 5
            flightFilterSortWidget.isFlowLayout = false
            flightFilterSortWidget.listener = object : FlightFilterSortFoldableWidget.ActionListener {
                override fun onChipStateChanged(items: List<BaseFilterSortModel>) {
                    var departureTimes = mutableListOf<DepartureTimeEnum>()
                    items.forEach {
                        departureTimes.add((it as DepartureTimeModel).departureTimeEnum)
                    }
                    selectedDepartureTime = departureTimes
                    listener.onDepartureTimeFilterChanged(departureTimes)
                }

                override fun onClickShowMore() {
                    //do nothing
                }
            }
            flightFilterSortWidget.buildView(getItems())
        }
    }

    private fun getSelectedByDepartureTimeEnum(departureTimeEnum: DepartureTimeEnum): Boolean {
        for (selectedTransit in selectedDepartureTime) {
            if (departureTimeEnum.id == selectedTransit.id) return true
        }
        return false
    }

    private fun getItems(): List<DepartureTimeModel> {
        val data = arrayListOf<DepartureTimeModel>()
        listener.getStatisticModel()?.let {
            for (item in it.arrivalTimeStatList) {
                data.add(generateDepartureTimeModelFromTimeEnum(item.departureTime))
            }
        }
        return data
    }

    private fun generateDepartureTimeModelFromTimeEnum(timeEnum: DepartureTimeEnum): DepartureTimeModel =
            when (timeEnum) {
                DepartureTimeEnum._00 -> DepartureTimeModel(timeEnum, getString(R.string.departure_0000_to_0600_with_desc), getSelectedByDepartureTimeEnum(timeEnum))
                DepartureTimeEnum._06 -> DepartureTimeModel(timeEnum, getString(R.string.departure_0600_to_1200_with_desc), getSelectedByDepartureTimeEnum(timeEnum))
                DepartureTimeEnum._12 -> DepartureTimeModel(timeEnum, getString(R.string.departure_1200_to_1800_with_desc), getSelectedByDepartureTimeEnum(timeEnum))
                DepartureTimeEnum._18 -> DepartureTimeModel(timeEnum, getString(R.string.departure_1800_to_2400_with_desc), getSelectedByDepartureTimeEnum(timeEnum))
            }

    fun resetView() {
        selectedDepartureTime = arrayListOf()
        for (item in itemView.flightFilterSortWidget.getItems()) {
            item.isSelected = false
        }
        itemView.flightFilterSortWidget.onResetChip()
        itemView.flightFilterSortWidget.notifyDataSetChanged()
    }

}