package com.tokopedia.hotel.common.util

import com.tokopedia.common.travel.utils.TravelDateUtil
import java.util.*
import java.util.concurrent.TimeUnit

class HotelUtils {

    companion object {
        val ONE_DAY: Long = TimeUnit.DAYS.toMillis(1)

        fun countCurrentDayDifference(date: String): Long =
                ((TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, date).time -
                        TravelDateUtil.getCurrentCalendar().timeInMillis) / ONE_DAY) + 1

        fun countDayDifference(date1: String, date2: String): Long =
                (TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, date2).time -
                        TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, date1).time) / ONE_DAY

        fun validateCheckInAndCheckOutDate(checkInDate: String, checkOutDate: String): Pair<String, String> {
            val todayWithoutTime = TravelDateUtil.removeTime(TravelDateUtil.getCurrentCalendar().time)
            val tomorrow = TravelDateUtil.addTimeToSpesificDate(todayWithoutTime, Calendar.DATE, 1)
            val dayAfterTomorrow = TravelDateUtil.addTimeToSpesificDate(todayWithoutTime, Calendar.DATE, 2)

            var updatedCheckInDate = checkInDate
            var updatedCheckOutDate = checkOutDate

            //if check in and / or check out param is empty
            if (updatedCheckInDate.isBlank() && updatedCheckOutDate.isBlank()) {
                updatedCheckInDate = TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, tomorrow)
                updatedCheckOutDate = TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, dayAfterTomorrow)
            } else if (updatedCheckInDate.isBlank()) {
                val checkout = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, updatedCheckOutDate)
                val dayBeforeCheckOut = TravelDateUtil.addTimeToSpesificDate(checkout, Calendar.DATE, -1)
                updatedCheckInDate = TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, dayBeforeCheckOut)
            } else if (updatedCheckOutDate.isBlank()) {
                val checkin = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, updatedCheckInDate)
                val dayAfterCheckIn = TravelDateUtil.addTimeToSpesificDate(checkin, Calendar.DATE, 1)
                updatedCheckOutDate = TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, dayAfterCheckIn)
            }

            //if check in date has passed
            if (countCurrentDayDifference(updatedCheckInDate) < 1) {
                updatedCheckInDate = TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, tomorrow)
            }
            //if check out date has passed or check out date < check in date
            if (countCurrentDayDifference(updatedCheckOutDate) < 1) {
                val checkin = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, updatedCheckInDate)
                val dayAfterCheckIn = TravelDateUtil.addTimeToSpesificDate(checkin, Calendar.DATE, 1)
                updatedCheckOutDate = TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, dayAfterCheckIn)
            }

            if (countDayDifference(updatedCheckInDate, updatedCheckOutDate) < 1) {
                updatedCheckInDate = TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, tomorrow)
                updatedCheckOutDate = TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, dayAfterTomorrow)
            }

            return Pair(updatedCheckInDate, updatedCheckOutDate)
        }
    }

}