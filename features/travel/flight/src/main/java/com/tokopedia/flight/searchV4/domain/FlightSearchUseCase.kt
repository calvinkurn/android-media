package com.tokopedia.flight.searchV4.domain

import com.tokopedia.flight.search.presentation.model.FlightSearchMetaModel
import com.tokopedia.flight.searchV4.data.FlightSearchRepository
import com.tokopedia.flight.searchV4.data.cloud.single.FlightSearchMetaEntity
import com.tokopedia.flight.searchV4.data.cloud.single.FlightSearchRequestModel
import javax.inject.Inject

/**
 * @author by furqan on 09/04/2020
 */
class FlightSearchUseCase @Inject constructor(private val flightSearchRepository: FlightSearchRepository) {

    suspend fun execute(searchParams: FlightSearchRequestModel,
                        isRoundTrip: Boolean,
                        isReturnTrip: Boolean,
                        onwardJourneyId: String = ""): FlightSearchMetaModel {

        return return if (isRoundTrip && !isReturnTrip) {
            mapToFlightSearchMetaViewModel(flightSearchRepository.getSearchSingleCombined(searchParams, isReturnTrip), searchParams)
        } else if (isRoundTrip && isReturnTrip) {
            mapToFlightSearchMetaViewModel(flightSearchRepository.getSearchCombinedReturn(searchParams, onwardJourneyId, isReturnTrip), searchParams)
        } else {
            mapToFlightSearchMetaViewModel(flightSearchRepository.getSearchSingle(searchParams, isReturnTrip), searchParams)
        }
    }

    private fun mapToFlightSearchMetaViewModel(meta: FlightSearchMetaEntity,
                                               flightSearchApiRequestModel: FlightSearchRequestModel):
            FlightSearchMetaModel {
        with(meta) {
            return FlightSearchMetaModel(
                    flightSearchApiRequestModel.departure,
                    flightSearchApiRequestModel.arrival,
                    flightSearchApiRequestModel.date,
                    needRefresh,
                    refreshTime,
                    maxRetry,
                    0,
                    0,
                    airlineList,
                    requestId
            )
        }
    }

}