package com.tokopedia.common.travel.data

import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.utils.network.CacheUtil
import com.tokopedia.cachemanager.CacheManager
import com.tokopedia.common.travel.data.entity.TravelCalendarHoliday
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TravelCalendarHolidayRepository @Inject constructor(private val cacheManager: CacheManager) {

    fun getHolidayResultsLocal(): TravelCalendarHoliday.HolidayData? {
        return cacheManager.get(KEY_CALENDAR_HOLIDAY, TravelCalendarHoliday.HolidayData::class.java)
    }

    fun saveHolidayResults(travelHolidayData: TravelCalendarHoliday.HolidayData) {
        if (travelHolidayData.data.isNotEmpty()) {
            val jsonString = CacheUtil.convertModelToString(travelHolidayData,
                    object : TypeToken<TravelCalendarHoliday.HolidayData>(){}.type)
            cacheManager.put(KEY_CALENDAR_HOLIDAY, jsonString, DURATION_SAVE_TO_CACHE)
        }
    }

    companion object {
        private val KEY_CALENDAR_HOLIDAY = "calendar_travel_holiday"
        private val DURATION_SAVE_TO_CACHE = TimeUnit.DAYS.toSeconds(7)
    }
}