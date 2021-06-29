package com.tokopedia.hotel.common.util

import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.toDate
import com.tokopedia.utils.date.toString
import java.util.*

class HotelUtils {

    companion object {

        fun validateCheckInAndCheckOutDate(checkInDate: String, checkOutDate: String): Pair<String, String> {
            val todayWithoutTime = DateUtil.removeTime(DateUtil.getCurrentCalendar().time)
            val tomorrow = DateUtil.addTimeToSpesificDate(todayWithoutTime, Calendar.DATE, 1)
            val dayAfterTomorrow = DateUtil.addTimeToSpesificDate(todayWithoutTime, Calendar.DATE, 2)

            var updatedCheckInDate = checkInDate
            var updatedCheckOutDate = checkOutDate

            //if check in and / or check out param is empty
            if (updatedCheckInDate.isBlank() && updatedCheckOutDate.isBlank()) {
                updatedCheckInDate = tomorrow.toString(DateUtil.YYYY_MM_DD)
                updatedCheckOutDate = dayAfterTomorrow.toString(DateUtil.YYYY_MM_DD)
            } else if (updatedCheckInDate.isBlank()) {
                val checkout = updatedCheckOutDate.toDate(DateUtil.YYYY_MM_DD)
                val dayBeforeCheckOut = DateUtil.addTimeToSpesificDate(checkout, Calendar.DATE, -1)
                updatedCheckInDate = dayBeforeCheckOut.toString(DateUtil.YYYY_MM_DD)
            } else if (updatedCheckOutDate.isBlank()) {
                val checkin = updatedCheckInDate.toDate(DateUtil.YYYY_MM_DD)
                val dayAfterCheckIn = DateUtil.addTimeToSpesificDate(checkin, Calendar.DATE, 1)
                updatedCheckOutDate = dayAfterCheckIn.toString(DateUtil.YYYY_MM_DD)
            }

            //if check in date has passed
            if (DateUtil.getDayDiffFromToday(updatedCheckInDate) < 1) {
                updatedCheckInDate = tomorrow.toString(DateUtil.YYYY_MM_DD)
            }
            //if check out date has passed or check out date < check in date
            if (DateUtil.getDayDiffFromToday(updatedCheckOutDate) < 1) {
                val checkin = updatedCheckInDate.toDate(DateUtil.YYYY_MM_DD)
                val dayAfterCheckIn = DateUtil.addTimeToSpesificDate(checkin, Calendar.DATE, 1)
                updatedCheckOutDate = dayAfterCheckIn.toString(DateUtil.YYYY_MM_DD)
            }

            if (DateUtil.getDayDiff(updatedCheckInDate, updatedCheckOutDate) < 1) {
                updatedCheckInDate = tomorrow.toString(DateUtil.YYYY_MM_DD)
                updatedCheckOutDate = dayAfterTomorrow.toString(DateUtil.YYYY_MM_DD)
            }

            return Pair(updatedCheckInDate, updatedCheckOutDate)
        }
    }

}