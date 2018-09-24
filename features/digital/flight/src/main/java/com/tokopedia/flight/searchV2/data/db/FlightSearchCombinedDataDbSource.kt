package com.tokopedia.flight.searchV2.data.db

import rx.Observable

/**
 * Created by Rizky on 21/09/18.
 */
class FlightSearchCombinedDataDbSource(private val comboDao: ComboDao) {

    fun getSearchCombined(journeyId: String): Observable<List<Combo>> {
        return Observable.create {
            it.onNext(comboDao.findCombosByJourneyId(journeyId))
        }
    }

}