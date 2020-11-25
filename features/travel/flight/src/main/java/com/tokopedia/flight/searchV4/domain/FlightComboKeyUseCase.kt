package com.tokopedia.flight.searchV4.domain

import com.tokopedia.flight.searchV4.data.FlightSearchRepository
import javax.inject.Inject

/**
 * @author by furqan on 16/04/2020
 */
class FlightComboKeyUseCase @Inject constructor(private val flightSearchRepository: FlightSearchRepository) {

    suspend fun execute(onwardJourneyId: String, returnJourneyId: String): String =
            flightSearchRepository.getComboKey(onwardJourneyId, returnJourneyId)
}