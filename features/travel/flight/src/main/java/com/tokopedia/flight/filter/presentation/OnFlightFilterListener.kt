package com.tokopedia.flight.filter.presentation

import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.search.presentation.model.statistics.FlightSearchStatisticModel

/**
 * @author by furqan on 19/02/2020
 */
interface OnFlightFilterListener {

    fun getFlightSearchStaticticModel(): FlightSearchStatisticModel?

    fun getFlightFilterModel(): FlightFilterModel?

    fun getFlightSelectedSort(): Int

    fun onFlightFilterAirlineSaved(selectedAirlines: List<String>)

}