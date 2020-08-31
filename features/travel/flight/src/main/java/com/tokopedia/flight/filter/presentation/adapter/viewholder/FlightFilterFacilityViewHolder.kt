package com.tokopedia.flight.filter.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.filter.presentation.FlightFilterFacilityEnum
import com.tokopedia.flight.filter.presentation.FlightFilterSortListener
import com.tokopedia.flight.filter.presentation.model.BaseFilterSortModel
import com.tokopedia.flight.filter.presentation.model.FlightFilterFacilityModel
import com.tokopedia.flight.filter.presentation.widget.FlightFilterSortFoldableWidget
import kotlinx.android.synthetic.main.item_flight_filter_sort.view.*

/**
 * @author by furqan on 21/02/2020
 */
class FlightFilterFacilityViewHolder(view: View,
                                     private val listener: FlightFilterSortListener,
                                     var selectedFacility: List<FlightFilterFacilityEnum>)
    : AbstractViewHolder<FlightFilterFacilityModel>(view) {

    override fun bind(element: FlightFilterFacilityModel?) {
        with(itemView) {
            flightFilterSortWidget.titleText = getString(R.string.flight_search_filter_facility)
            flightFilterSortWidget.isSelectOnlyOneChip = false
            flightFilterSortWidget.hasShowMore = false
            flightFilterSortWidget.isFlowLayout = true
            flightFilterSortWidget.listener = object : FlightFilterSortFoldableWidget.ActionListener {
                override fun onChipStateChanged(items: List<BaseFilterSortModel>) {
                    selectedFacility = (items as List<FlightFilterFacilityModel>).map { it.facilityEnum }.toList()
                    listener.onFacilityChanged(selectedFacility)
                }

                override fun onClickShowMore() {
                    // No Show More Action
                }
            }
            flightFilterSortWidget.buildView(getItems())
        }
    }

    private fun getItems(): List<FlightFilterFacilityModel> {
        val data = arrayListOf<FlightFilterFacilityModel>()
        listener.getStatisticModel()?.let {
            if (it.isHaveBaggage) {
                data.add(FlightFilterFacilityModel(facilityEnum = FlightFilterFacilityEnum.BAGGAGE,
                        title = FlightFilterFacilityEnum.BAGGAGE.value,
                        isSelected = isSelected(FlightFilterFacilityEnum.BAGGAGE)))
            }
            if (it.isHaveInFlightMeal) {
                data.add(FlightFilterFacilityModel(facilityEnum = FlightFilterFacilityEnum.MEAL,
                        title = FlightFilterFacilityEnum.MEAL.value,
                        isSelected = isSelected(FlightFilterFacilityEnum.MEAL)))
            }
        }
        return data
    }

    private fun isSelected(item: FlightFilterFacilityEnum): Boolean {
        for (facility in selectedFacility) {
            if (facility.id == item.id) return true
        }
        return false
    }

    fun resetView() {
        selectedFacility = arrayListOf()
        for (item in itemView.flightFilterSortWidget.getItems()) {
            item.isSelected = false
        }
        itemView.flightFilterSortWidget.onResetChip()
        itemView.flightFilterSortWidget.notifyDataSetChanged()
    }

}