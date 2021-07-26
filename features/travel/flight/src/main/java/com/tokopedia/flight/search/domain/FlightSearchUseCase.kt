package com.tokopedia.flight.search.domain

import com.tokopedia.flight.search.data.FlightSearchRepository
import com.tokopedia.flight.search.data.cloud.single.FlightSearchMetaEntity
import com.tokopedia.flight.search.data.cloud.single.FlightSearchRequestModel
import com.tokopedia.flight.search.presentation.model.FlightSearchMetaModel
import javax.inject.Inject

/**
 * @author by furqan on 09/04/2020
 */
class FlightSearchUseCase @Inject constructor(private val flightSearchRepository: FlightSearchRepository) {

    suspend fun execute(searchParams: FlightSearchRequestModel,
                        isRoundTrip: Boolean,
                        isReturnTrip: Boolean,
                        onwardJourneyId: String = ""): FlightSearchMetaModel =
            if (isRoundTrip && !isReturnTrip) {
                mapToFlightSearchMetaViewModel(flightSearchRepository.getSearchSingleCombined(searchParams, isReturnTrip), searchParams)
            } else if (isRoundTrip && isReturnTrip) {
                mapToFlightSearchMetaViewModel(flightSearchRepository.getSearchCombinedReturn(searchParams, onwardJourneyId, isReturnTrip), searchParams)
            } else {
                mapToFlightSearchMetaViewModel(flightSearchRepository.getSearchSingle(searchParams, isReturnTrip), searchParams)
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
                    airlineList,
                    requestId,
                    internationalTag,
                    backgroundRefreshTime
            )
        }
    }

}