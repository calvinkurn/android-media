package com.tokopedia.flight.search.data.db

import rx.Observable
import javax.inject.Inject

/**
 * Created by Rizky on 21/09/18.
 */
open class FlightSearchCombinedDataDbSource @Inject constructor(private val flightComboDao: FlightComboDao) {

    open fun getSearchOnwardCombined(onwardJourneyId: String): Observable<List<FlightComboTable>> {
        return Observable.create {
            it.onNext(flightComboDao.findCombosByOnwardJourneyId(onwardJourneyId))
        }
    }

    open fun getSearchReturnCombined(returnOnwardId: String): Observable<List<FlightComboTable>> {
        return Observable.create {
            it.onNext(flightComboDao.findCombosByReturnJourneyId(returnOnwardId))
        }
    }

    fun getComboData(onwardJourneyId: String, returnJourneyId: String): Observable<List<FlightComboTable>> =
            Observable.create {
                it.onNext(flightComboDao.findCombosByOnwardAndReturnJourneyId(onwardJourneyId, returnJourneyId))
            }

    open fun insert(item: List<FlightComboTable>) {
        flightComboDao.insert(item)
    }

    fun insert(item: FlightComboTable) {
        flightComboDao.insert(item)
    }

    fun deleteAllFlightSearchCombinedData() {
        flightComboDao.deleteTable()
    }

}