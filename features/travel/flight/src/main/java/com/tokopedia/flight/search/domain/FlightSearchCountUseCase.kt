package com.tokopedia.flight.search.domain

import com.tokopedia.flight.search.data.FlightSearchRepository
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
import javax.inject.Inject

/**
 * Created by Rizky on 15/10/18.
 */
class FlightSearchCountUseCase @Inject constructor(
        private val flightSearchRepository: FlightSearchRepository) {

    suspend fun execute(flightFilterModel: FlightFilterModel): Int =
            flightSearchRepository.getSearchCount(flightFilterModel)

}