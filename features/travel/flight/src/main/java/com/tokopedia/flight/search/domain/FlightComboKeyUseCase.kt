package com.tokopedia.flight.search.domain

import com.tokopedia.flight.search.data.FlightSearchRepository
import javax.inject.Inject

/**
 * @author by furqan on 16/04/2020
 */
class FlightComboKeyUseCase @Inject constructor(private val flightSearchRepository: FlightSearchRepository) {

    suspend fun execute(onwardJourneyId: String, returnJourneyId: String): String =
            flightSearchRepository.getComboKey(onwardJourneyId, returnJourneyId)
}