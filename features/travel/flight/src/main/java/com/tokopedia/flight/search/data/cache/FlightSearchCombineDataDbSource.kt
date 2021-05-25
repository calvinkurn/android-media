package com.tokopedia.flight.search.data.cache

import com.tokopedia.flight.search.data.cache.db.FlightComboTable
import com.tokopedia.flight.search.data.cache.db.dao.FlightComboDao
import javax.inject.Inject

/**
 * @author by furqan on 14/04/2020
 */
class FlightSearchCombineDataDbSource @Inject constructor(private val flightComboDao: FlightComboDao) {

    suspend fun getSearchOnwardCombined(onwardJourneyId: String): List<FlightComboTable> =
            flightComboDao.findCombosByOnwardJourneyId(onwardJourneyId)

    suspend fun getSearchReturnCombined(returnOnwardId: String): List<FlightComboTable> =
            flightComboDao.findCombosByReturnJourneyId(returnOnwardId)

    suspend fun getComboData(onwardJourneyId: String, returnJourneyId: String): List<FlightComboTable> =
            flightComboDao.findCombosByOnwardAndReturnJourneyId(onwardJourneyId, returnJourneyId)

    suspend fun insert(item: List<FlightComboTable>) {
        flightComboDao.insert(item)
    }

    suspend fun insert(item: FlightComboTable) {
        flightComboDao.insert(item)
    }

    suspend fun deleteAllSearchCombinedData() {
        flightComboDao.deleteTable()
    }

}