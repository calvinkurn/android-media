package com.tokopedia.flight.search.domain

import com.tokopedia.flight.search.data.FlightSearchRepository
import com.tokopedia.flight.search.data.cloud.combine.FlightCombineRequestModel
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author by furqan on 14/04/2020
 */
class FlightSearchCombineUseCase @Inject constructor(private val flightSearchRepository: FlightSearchRepository) {

    suspend fun execute(combineParams: FlightCombineRequestModel)
            : Boolean {

        val numOfAttempts = intArrayOf(0)
        val pollDelay = intArrayOf(0)
        var isStopLooping: Boolean

        do {
            delay(TimeUnit.SECONDS.toMillis(pollDelay[0].toLong()))

            flightSearchRepository.getSearchCombined(combineParams).let {
                pollDelay[0] = it.refreshTime
                numOfAttempts[0]++

                isStopLooping = !it.needRefresh || numOfAttempts[0] >= it.maxRetry
            }
        } while (!isStopLooping)

        return isStopLooping
    }

}