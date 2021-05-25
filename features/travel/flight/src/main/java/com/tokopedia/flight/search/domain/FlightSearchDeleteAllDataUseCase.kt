package com.tokopedia.flight.search.domain

import com.tokopedia.flight.search.data.FlightSearchRepository
import javax.inject.Inject

/**
 * @author by furqan on 15/04/2020
 */
class FlightSearchDeleteAllDataUseCase @Inject constructor(
        private val flightSearchRepository: FlightSearchRepository) {

    suspend fun execute() {
        flightSearchRepository.deleteAllFlightSearchData()
    }
}