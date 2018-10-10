package com.tokopedia.flight.searchV2.data.db

import rx.Observable
import javax.inject.Inject

/**
 * Created by Rizky on 21/09/18.
 */
open class FlightSearchCombinedDataDbSource @Inject constructor(private val flightComboDao: FlightComboDao) {

    fun getSearchCombined(journeyId: String): Observable<List<FlightComboTable>> {
        return Observable.create {
            it.onNext(flightComboDao.findCombosByJourneyId(journeyId))
        }
    }

    fun getAllCombos(): Observable<List<FlightComboTable>> {
        return Observable.create {
            it.onNext(flightComboDao.findAllCombos())
        }
    }

    fun insert(item: List<FlightComboTable>) {
        flightComboDao.insert(item)
    }

    fun insert(item: FlightComboTable) {
        flightComboDao.insert(item)
    }

}