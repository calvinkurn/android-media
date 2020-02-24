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
                                     private var selectedFacility: List<FlightFilterFacilityEnum>)
    : AbstractViewHolder<FlightFilterFacilityModel>(view) {

    override fun bind(element: FlightFilterFacilityModel?) {
        with(itemView) {
            flight_sort_widget.titleText = getString(R.string.flight_search_filter_facility)
            flight_sort_widget.isSelectOnlyOneChip = false
            flight_sort_widget.hasShowMore = false
            flight_sort_widget.isFlowLayout = true
            flight_sort_widget.listener = object : FlightFilterSortFoldableWidget.ActionListener {
                override fun onChipStateChanged(items: List<BaseFilterSortModel>) {
                    selectedFacility = (items as List<FlightFilterFacilityModel>).map { it.facilityEnum }.toList()
                    listener.onFacilityChanged(selectedFacility)
                }

                override fun onClickShowMore() {
                    // No Show More Action
                }
            }
            flight_sort_widget.buildView(getItems())
        }
    }

    private fun getItems(): List<FlightFilterFacilityModel> {
        return listOf(
                FlightFilterFacilityModel(facilityEnum = FlightFilterFacilityEnum.BAGGAGE,
                        title = FlightFilterFacilityEnum.BAGGAGE.value,
                        isSelected = isSelected(FlightFilterFacilityEnum.BAGGAGE)),
                FlightFilterFacilityModel(facilityEnum = FlightFilterFacilityEnum.MEAL,
                        title = FlightFilterFacilityEnum.MEAL.value,
                        isSelected = isSelected(FlightFilterFacilityEnum.MEAL))
        )
    }

    private fun isSelected(item: FlightFilterFacilityEnum): Boolean {
        for (facility in selectedFacility) {
            if (facility.id == item.id) return true
        }
        return false
    }

    fun resetView() {
        selectedFacility = arrayListOf()
        for (item in itemView.flight_sort_widget.getItems()) {
            item.isSelected = false
        }
        itemView.flight_sort_widget.onResetChip()
        itemView.flight_sort_widget.notifyDataSetChanged()
    }
}