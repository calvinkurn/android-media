package com.tokopedia.travelcalendar.data

import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.utils.network.CacheUtil
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.travelcalendar.data.entity.TravelCalendarHoliday
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TravelCalendarHolidayRepository @Inject constructor() {

    fun getHolidayResultsLocal(): TravelCalendarHoliday.HolidayData? {
        return try {
            return PersistentCacheManager.instance.get(KEY_CALENDAR_HOLIDAY, TravelCalendarHoliday.HolidayData::class.java)
        } catch (throwable:Throwable){
            TravelCalendarHoliday.HolidayData()
        }
    }

    fun saveHolidayResults(travelHolidayData: TravelCalendarHoliday.HolidayData) {
        if (travelHolidayData.data.isNotEmpty()) {
            val jsonString = CacheUtil.convertModelToString(travelHolidayData,
                    object : TypeToken<TravelCalendarHoliday.HolidayData>() {}.type)
            PersistentCacheManager.instance.put(KEY_CALENDAR_HOLIDAY, jsonString, DURATION_SAVE_TO_CACHE)
        }
    }

    companion object {
        private val KEY_CALENDAR_HOLIDAY = "calendar_travel_holiday"
        private val DURATION_SAVE_TO_CACHE = TimeUnit.DAYS.toSeconds(7)
    }
}