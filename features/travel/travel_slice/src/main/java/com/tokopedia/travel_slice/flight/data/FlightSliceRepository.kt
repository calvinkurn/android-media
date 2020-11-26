package com.tokopedia.travel_slice.flight.data

import com.tokopedia.travel_slice.flight.domain.FlightOrderListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by furqan on 26/11/2020
 */
class FlightSliceRepository @Inject constructor(
        private val flightOrderListUseCase: FlightOrderListUseCase) {

    suspend fun getFlightOrderData(isLoggedIn: Boolean): List<FlightOrderListEntity> {
        if (isLoggedIn) {
            return withContext(Dispatchers.IO, block = {
                flightOrderListUseCase.executeOnBackground()
            })
        } else {
            throw Throwable("unauthorized user")
        }
    }

}