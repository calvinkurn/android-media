package com.tokopedia.flight.filter.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.filter.presentation.FlightFilterSortListener
import com.tokopedia.flight.filter.presentation.adapter.viewholder.*
import com.tokopedia.flight.filter.presentation.model.*
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel

/**
 * @author by jessica on 2020-02-20
 */

class FlightFilterSortAdapterTypeFactory(val listener: FlightFilterSortListener,
                                         var initialSortOption: Int,
                                         var filterModel: FlightFilterModel) :
        BaseAdapterTypeFactory() {

    private lateinit var sortViewHolder: FlightSortViewHolder
    private lateinit var transitViewHolder: FlightFilterTransitViewHolder
    private lateinit var departureViewHolder: FlightFilterDepartureTimeViewHolder
    private lateinit var arrivalViewHolder: FlightFilterArrivalTimeViewHolder
    private lateinit var airlineViewHolder: FlightFilterWidgetAirlineViewHolder
    private lateinit var facilityViewHolder: FlightFilterFacilityViewHolder
    private lateinit var priceRangeViewHolder: FlightFilterPriceRangeViewHolder

    fun type(model: FlightSortModel): Int = TYPE_FLIGHT_SORT

    fun type(model: PriceRangeModel): Int = FlightFilterPriceRangeViewHolder.LAYOUT

    fun type(model: TransitModel): Int = TYPE_FLIGHT_FILTER_TRANSIT

    fun type(model: DepartureTimeModel): Int = TYPE_FLIGHT_FILTER_DEPARTURE_TIME

    fun type(model: ArrivalTimeModel): Int = TYPE_FLIGHT_FILTER_ARRIVAL_TIME

    fun type(model: FlightFilterAirlineModel): Int = TYPE_FLIGHT_FILTER_AIRLINE

    fun type(model: FlightFilterFacilityModel): Int = TYPE_FLIGHT_FILTER_FACILITY

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            TYPE_FLIGHT_SORT ->
                if (::sortViewHolder.isInitialized) {
                    sortViewHolder.selectedId = initialSortOption
                    sortViewHolder
                } else {
                    sortViewHolder = FlightSortViewHolder(parent, listener, initialSortOption)
                    sortViewHolder
                }
            TYPE_FLIGHT_FILTER_TRANSIT -> if (::transitViewHolder.isInitialized) {
                transitViewHolder.selectedTransits = filterModel.transitTypeList
                        ?: transitViewHolder.selectedTransits
                transitViewHolder
            } else {
                transitViewHolder = FlightFilterTransitViewHolder(parent, listener, filterModel.transitTypeList
                        ?: mutableListOf())
                transitViewHolder
            }
            TYPE_FLIGHT_FILTER_DEPARTURE_TIME -> if (::departureViewHolder.isInitialized) {
                departureViewHolder.selectedDepartureTime = filterModel.departureTimeList
                        ?: departureViewHolder.selectedDepartureTime
                departureViewHolder
            } else {
                departureViewHolder =
                        FlightFilterDepartureTimeViewHolder(parent, listener, filterModel.departureTimeList
                                ?: listOf())
                departureViewHolder
            }
            TYPE_FLIGHT_FILTER_ARRIVAL_TIME -> if (::arrivalViewHolder.isInitialized) {
                arrivalViewHolder.selectedArrivalTime = filterModel.arrivalTimeList
                arrivalViewHolder
            } else {
                arrivalViewHolder = FlightFilterArrivalTimeViewHolder(parent, listener, filterModel.arrivalTimeList
                        ?: listOf())
                arrivalViewHolder
            }
            TYPE_FLIGHT_FILTER_AIRLINE -> if (::airlineViewHolder.isInitialized) {
                airlineViewHolder.selectedAirline = filterModel.airlineList
                        ?: airlineViewHolder.selectedAirline
                airlineViewHolder
            } else {
                airlineViewHolder = FlightFilterWidgetAirlineViewHolder(parent, listener, filterModel.airlineList
                        ?: listOf())
                airlineViewHolder
            }
            TYPE_FLIGHT_FILTER_FACILITY -> if (::facilityViewHolder.isInitialized) {
                facilityViewHolder.selectedFacility = filterModel.facilityList
                        ?: facilityViewHolder.selectedFacility
                facilityViewHolder
            } else {
                facilityViewHolder = FlightFilterFacilityViewHolder(parent, listener, filterModel.facilityList
                        ?: listOf())
                facilityViewHolder
            }
            FlightFilterPriceRangeViewHolder.LAYOUT -> if (::priceRangeViewHolder.isInitialized) {
                priceRangeViewHolder
            } else {
                priceRangeViewHolder = FlightFilterPriceRangeViewHolder(parent, listener)
                priceRangeViewHolder
            }
            else -> super.createViewHolder(parent, type)
        }
    }

    fun getViewHolderLayout(type: Int): Int {
        return when (type) {
            TYPE_FLIGHT_SORT, TYPE_FLIGHT_FILTER_TRANSIT, TYPE_FLIGHT_FILTER_DEPARTURE_TIME,
            TYPE_FLIGHT_FILTER_ARRIVAL_TIME, TYPE_FLIGHT_FILTER_AIRLINE,
            TYPE_FLIGHT_FILTER_FACILITY -> R.layout.item_flight_filter_sort
            else -> type
        }
    }

    fun resetFilter() {
        if (::sortViewHolder.isInitialized) sortViewHolder.resetView()
        if (::transitViewHolder.isInitialized) transitViewHolder.resetView()
        if (::departureViewHolder.isInitialized) departureViewHolder.resetView()
        if (::arrivalViewHolder.isInitialized) arrivalViewHolder.resetView()
        if (::airlineViewHolder.isInitialized) airlineViewHolder.resetView()
        if (::facilityViewHolder.isInitialized) facilityViewHolder.resetView()
        if (::priceRangeViewHolder.isInitialized) priceRangeViewHolder.resetView()
    }

    companion object {
        const val TYPE_FLIGHT_SORT = 0
        const val TYPE_FLIGHT_FILTER_TRANSIT = 1
        const val TYPE_FLIGHT_FILTER_DEPARTURE_TIME = 2
        const val TYPE_FLIGHT_FILTER_ARRIVAL_TIME = 3
        const val TYPE_FLIGHT_FILTER_AIRLINE = 4
        const val TYPE_FLIGHT_FILTER_FACILITY = 5
    }

}