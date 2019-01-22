package com.tokopedia.travelcalendar.domain

import com.tokopedia.travelcalendar.data.entity.HolidayEntity
import com.tokopedia.travelcalendar.view.model.HolidayResult

import rx.Observable

/**
 * Created by nabillasabbaha on 14/05/18.
 */
interface ITravelCalendarRepository {

    fun getHolidayResults(holidayEntityObservable: Observable<HolidayEntity>): Observable<List<HolidayResult>>

    fun getHolidayResultsLocal(): Observable<List<HolidayResult>>
}
