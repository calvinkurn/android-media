package com.tokopedia.shop.common.util

import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHour
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Rafli Syam on 03/05/2021
 */
object OperationalHoursUtil {

    // Days Name List
    private const val MONDAY = "Senin"
    private const val TUESDAY = "Selasa"
    private const val WEDNESDAY = "Rabu"
    private const val THURSDAY = "Kamis"
    private const val FRIDAY = "Jumat"
    private const val SATURDAY = "Sabtu"
    private const val SUNDAY = "Minggu"

    // Time Constant
    const val MIN_START_TIME = "00:00:00"
    const val MAX_END_TIME = "23:59:59"
    private const val SUBSTRING_START_TIME_INDEX = 0
    private const val SUBSTRING_HOUR_TIME_INDEX = 2
    private const val SUBSTRING_END_TIME_INDEX = 5

    // DateTime format
    const val ALL_DAY = "Buka 24 Jam"
    const val HOLIDAY = "Libur"
    private const val DEFAULT_TIMEZONE = "WIB"
    private const val HOUR = "Jam"

    private const val INDONESIA_LANGUAGE_ID = "id"
    private const val INDONESIA_COUNTRY_ID = "ID"
    private const val DEFAULT_HOUR = 23
    private const val DEFAULT_MINUTE = 59
    private const val DEFAULT_SECONDS = 59
    private val defaultLocale = Locale(INDONESIA_LANGUAGE_ID, INDONESIA_COUNTRY_ID)
    private val defaultLocalFormatter = SimpleDateFormat("dd MMMM yyyy", defaultLocale)
    private val shortDateFormatter = SimpleDateFormat("dd/MM/yyyy", defaultLocale)

    /**
     * Day of operational represent as Int: 1 (Monday) - 7 (Sunday)
     * https://tokopedia.atlassian.net/wiki/spaces/MC/pages/742297683/Shop+Operational+Hours+-+GQL+Query#Get-Shop-Operational-Hours
     */
    private val daylistMap: Map<Int, String> = mapOf(
            1 to MONDAY,
            2 to TUESDAY,
            3 to WEDNESDAY,
            4 to THURSDAY,
            5 to FRIDAY,
            6 to SATURDAY,
            7 to SUNDAY
    )

    /**
     * Get the name of the day in Bahasa from id
     * @return [String]
     */
    fun getDayName(dayId: Int): String {
        return daylistMap[dayId] ?: ""
    }

    /**
     * Get day with format 1 - 7 from Monday to Sunday
     * @return [Int]
     */
    fun getOrdinalDate(dayOfWeek: Int): Int {
        return if (dayOfWeek == 1) {
            dayOfWeek + 6
        }
        else dayOfWeek - 1
    }

    /**
     * Generate Shop operational hours wording
     * Example : "Jam 10.00 - 16.00 WIB"
     * @return [String]
     */
    fun generateDatetime(startTime: String, endTime: String): String {
        return if (startTime == MIN_START_TIME && endTime == MAX_END_TIME) {
            // 24 hours
            ALL_DAY
        } else if (startTime == endTime) {
            // Holiday
            HOLIDAY
        } else {
            // Operational hours format
            "$HOUR ${formatDateTime(startTime)} - ${formatDateTime(endTime)} $DEFAULT_TIMEZONE"
        }
    }

    /**
     * Convert "17:00:00" to "17.00 WIB"
     * @return [String]
     */
    fun formatDateTimeWithDefaultTimezone(time: String): String {
        return "${time.replace(":", ".").substring(SUBSTRING_START_TIME_INDEX, SUBSTRING_END_TIME_INDEX)} $DEFAULT_TIMEZONE"
    }

    /**
     * Get hour from "17:00:00", it will return 17
     * @return [String]
     */
    fun getHourFromFormattedTime(time: String): String {
        return time.substring(SUBSTRING_START_TIME_INDEX, SUBSTRING_HOUR_TIME_INDEX)
    }

    /**
     * Get minute from "17:35:00", it will return 35
     * @return [String]
     */
    fun getMinuteFromFormattedTime(time: String): String {
        return time.substring(3, 5)
    }

    /**
     * Generate server datetime
     * Example : "17:45:00"
     * @return [String]
     */
    fun generateServerDateTimeFormat(selectedHour: Int, selectedMinutes: Int): String {
        var newSelectedHour = selectedHour.toString()
        var newSelectedMinutes = selectedMinutes.toString()
        if (selectedHour < 10) {
            newSelectedHour = "0$selectedHour"
        }
        if (selectedMinutes < 10) {
            newSelectedMinutes = "0$selectedMinutes"
        }
        return "$newSelectedHour:$newSelectedMinutes:00"
    }

    fun toShortDateFormat(date: Date): String {
        return shortDateFormatter.format(date)
    }

    fun toIndonesianDateFormat(date: Date): String {
        return defaultLocalFormatter.format(date)
    }

    /**
     * Set default time before send the datetime to server
     * the default time must 23:59:59
     * @return [Date]
     */
    fun setDefaultServerTimeForSelectedDate(selectedDate: Date): Date {
        return Calendar.getInstance().let { calendar ->
            calendar.time = selectedDate
            calendar.add(Calendar.HOUR_OF_DAY, DEFAULT_HOUR)
            calendar.add(Calendar.MINUTE, DEFAULT_MINUTE)
            calendar.add(Calendar.SECOND, DEFAULT_SECONDS)
            calendar.time
        }
    }

    /**
     * if seller never set ops hour before,
     * then backend will return empty list
     * so our apps will show default "24 Jam" everyday
     * @return [MutableList]
     */
    fun generateDefaultOpsHourList(): MutableList<ShopOperationalHour> {
        return mutableListOf<ShopOperationalHour>().let { list ->
            // 1 represent MONDAY , 7 represent SUNDAY
            for (i in 1..7) {
                list.add(ShopOperationalHour(
                        day = i,
                        startTime = MIN_START_TIME,
                        endTime = MAX_END_TIME
                ))
            }
            list
        }
    }

    /**
     * Convert time from 18:00:00 -> 18.00 without timezone
     * @return [String]
     */
    private fun formatDateTime(time: String): String {
        return time.replace(":", ".").substring(SUBSTRING_START_TIME_INDEX, SUBSTRING_END_TIME_INDEX)
    }
}