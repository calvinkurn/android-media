package com.tokopedia.flight.searchV4.domain

import com.tokopedia.flight.searchV4.data.FlightSearchRepository
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