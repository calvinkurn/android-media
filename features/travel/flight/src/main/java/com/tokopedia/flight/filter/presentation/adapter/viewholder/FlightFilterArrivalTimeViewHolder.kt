package com.tokopedia.flight.filter.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.filter.presentation.FlightFilterSortListener
import com.tokopedia.flight.filter.presentation.model.ArrivalTimeModel
import com.tokopedia.flight.filter.presentation.model.BaseFilterSortModel
import com.tokopedia.flight.filter.presentation.viewmodel.FlightFilterViewModel.Companion.ARRIVAL_TIME_ORDER
import com.tokopedia.flight.filter.presentation.widget.FlightFilterSortFoldableWidget
import com.tokopedia.flight.search.presentation.model.filter.DepartureTimeEnum
import kotlinx.android.synthetic.main.item_flight_filter_sort.view.*

/**
 * @author by jessica on 2020-02-21
 */

class FlightFilterArrivalTimeViewHolder(view: View, val listener: FlightFilterSortListener,
                                        var selectedArrivalTime: List<DepartureTimeEnum>)
    : AbstractViewHolder<ArrivalTimeModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_flight_filter_sort
    }

    override fun bind(element: ArrivalTimeModel) {
        with(itemView) {
            flight_sort_widget.titleText = resources.getString(R.string.flight_search_filter_arrival_time)
            flight_sort_widget.isSelectOnlyOneChip = false
            flight_sort_widget.hasShowMore = false
            flight_sort_widget.maxItemCount = 5
            flight_sort_widget.isFlowLayout = false
            flight_sort_widget.listener = object : FlightFilterSortFoldableWidget.ActionListener {
                override fun onChipStateChanged(items: List<BaseFilterSortModel>) {
                    var arrivalTimes = mutableListOf<DepartureTimeEnum>()
                    items.forEach {
                        arrivalTimes.add((it as ArrivalTimeModel).arrivalTimeEnum)
                    }
                    selectedArrivalTime = arrivalTimes
                    listener.onArrivalTimeFilterChanged(arrivalTimes)
                }

                override fun onClickShowMore() {
                    //do nothing
                }
            }
            if (listener.shouldReset(ARRIVAL_TIME_ORDER)) resetSelectedArrivalTime()
            flight_sort_widget.buildView(getItems())
        }
    }

    private fun getSelectedByArrivalTimeEnum(arrivalTimeEnum: DepartureTimeEnum): Boolean {
        for (selectedTransit in selectedArrivalTime) {
            if (arrivalTimeEnum.id == selectedTransit.id) return true
        }
        return false
    }

    private fun getItems(): List<ArrivalTimeModel> {
        val data = arrayListOf<ArrivalTimeModel>()
        listener.getStatisticModel()?.let {
            for (item in it.arrivalTimeStatList) {
                data.add(generateArrivalTimeModelFromTimeEnum(item.departureTime))
            }
        }
        return data
    }

    private fun generateArrivalTimeModelFromTimeEnum(timeEnum: DepartureTimeEnum): ArrivalTimeModel =
            when (timeEnum) {
                DepartureTimeEnum._00 -> ArrivalTimeModel(timeEnum, getString(R.string.departure_0000_to_0600_with_desc), getSelectedByArrivalTimeEnum(timeEnum))
                DepartureTimeEnum._06 -> ArrivalTimeModel(timeEnum, getString(R.string.departure_0600_to_1200_with_desc), getSelectedByArrivalTimeEnum(timeEnum))
                DepartureTimeEnum._12 -> ArrivalTimeModel(timeEnum, getString(R.string.departure_1200_to_1800_with_desc), getSelectedByArrivalTimeEnum(timeEnum))
                DepartureTimeEnum._18 -> ArrivalTimeModel(timeEnum, getString(R.string.departure_1800_to_2400_with_desc), getSelectedByArrivalTimeEnum(timeEnum))
            }

    fun resetView() {
        for (item in itemView.flight_sort_widget.getItems()) {
            item.isSelected = false
        }
        resetSelectedArrivalTime()
        itemView.flight_sort_widget.onResetChip()
        itemView.flight_sort_widget.notifyDataSetChanged()
    }

    private fun resetSelectedArrivalTime() {
        selectedArrivalTime = arrayListOf()
        listener.hasBeenReset(ARRIVAL_TIME_ORDER)
    }

}