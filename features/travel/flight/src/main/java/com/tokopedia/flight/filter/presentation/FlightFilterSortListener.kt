package com.tokopedia.flight.filter.presentation

import com.tokopedia.flight.search.presentation.model.filter.DepartureTimeEnum
import com.tokopedia.flight.search.presentation.model.filter.TransitEnum
import com.tokopedia.flight.search.presentation.model.resultstatistics.AirlineStat

/**
 * @author by jessica on 2020-02-20
 */

interface FlightFilterSortListener {
    fun onSortChanged(selectedSortOption: Int)
    fun onClickSeeAllSort()
    fun onTransitFilterChanged(transitTypeList: List<TransitEnum>)
    fun onDepartureTimeFilterChanged(departureTimeList: List<DepartureTimeEnum>)
    fun onArrivalTimeFilterChanged(arrivalTimeList: List<DepartureTimeEnum>)
    fun onClickSeeAllAirline()

    fun getAirlineList(): List<AirlineStat>
    fun onAirlineChanged(checkedAirlines: List<String>)
}