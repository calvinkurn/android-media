package com.tokopedia.flight.filter.presentation

import com.tokopedia.flight.search.presentation.model.filter.DepartureTimeEnum
import com.tokopedia.flight.search.presentation.model.filter.TransitEnum
import com.tokopedia.flight.search.presentation.model.statistics.AirlineStat
import com.tokopedia.flight.search.presentation.model.statistics.FlightSearchStatisticModel

/**
 * @author by jessica on 2020-02-20
 */

interface FlightFilterSortListener {
    fun getStatisticModel(): FlightSearchStatisticModel?
    fun getAirlineList(): List<AirlineStat>

    fun onSortChanged(selectedSortOption: Int)
    fun onClickSeeAllSort()
    fun onTransitFilterChanged(transitTypeList: List<TransitEnum>)
    fun onDepartureTimeFilterChanged(departureTimeList: List<DepartureTimeEnum>)
    fun onArrivalTimeFilterChanged(arrivalTimeList: List<DepartureTimeEnum>)
    fun onClickSeeAllAirline()
    fun onAirlineChanged(checkedAirlines: List<String>)
    fun onFacilityChanged(selectedFacilities: List<FlightFilterFacilityEnum>)
    fun onPriceRangeChanged(minPrice: Int, maxPrice: Int)

}