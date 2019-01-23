package com.tokopedia.travelcalendar.data

import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.data.model.storage.CacheManager
import com.tokopedia.abstraction.common.utils.network.CacheUtil
import com.tokopedia.travelcalendar.data.entity.HolidayEntity
import com.tokopedia.travelcalendar.data.entity.HolidayResultEntity
import com.tokopedia.travelcalendar.domain.ITravelCalendarRepository
import com.tokopedia.travelcalendar.view.CALENDAR_YYYYMMDD
import com.tokopedia.travelcalendar.view.convertDate
import com.tokopedia.travelcalendar.view.getZeroTime
import com.tokopedia.travelcalendar.view.model.HolidayDetail
import com.tokopedia.travelcalendar.view.model.HolidayResult
import rx.Observable
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 14/05/18.
 */
class TravelCalendarRepository @Inject constructor(private val cacheManager: CacheManager) : ITravelCalendarRepository {

    override fun getHolidayResults(holidayEntityObservable: Observable<HolidayEntity>): Observable<List<HolidayResult>> {
        return holidayEntityObservable
                .doOnNext {
                    if (it != null && it.holidayResultEntities != null) {
                        val jsonString = CacheUtil.convertModelToString(it,
                                object : TypeToken<HolidayEntity>() {
                                }.type)
                        cacheManager.save(KEY_CALENDAR_HOLIDAY, jsonString, DURATION_SAVE_TO_CACHE)
                    }
                }
                .map { it -> it.holidayResultEntities }
                .map { convertHolidayMapper(it) }
    }

    override fun getHolidayResultsLocal(): Observable<List<HolidayResult>> {
        return Observable.just(true)
                .map {
                    if (cache != null) {
                        CacheUtil.convertStringToModel<HolidayEntity>(cache, object : TypeToken<HolidayEntity>() {
                        }.type)
                    } else
                        throw RuntimeException("Cache has expired")
                }
                .map { it -> it.holidayResultEntities }
                .map { convertHolidayMapper(it) }
    }

    private val cache: String?
        get() = cacheManager.get(KEY_CALENDAR_HOLIDAY)

    private fun convertHolidayMapper(holidayResultEntities: List<HolidayResultEntity>): List<HolidayResult> {
        return holidayResultEntities.map {
            val dateHoliday =  it.attributes.date.convertDate(CALENDAR_YYYYMMDD)
            val zeroTimeHolidayDate = Calendar.getInstance().getZeroTime(dateHoliday)
            val holidayDetail = HolidayDetail(it.attributes.date, it.attributes.label, zeroTimeHolidayDate)

            val holidayResult = HolidayResult(it.id, holidayDetail)
            return@map holidayResult
        }
    }

    companion object {

        private val KEY_CALENDAR_HOLIDAY = "calendar_holiday"
        private val DURATION_SAVE_TO_CACHE = TimeUnit.DAYS.toSeconds(7)
    }
}
