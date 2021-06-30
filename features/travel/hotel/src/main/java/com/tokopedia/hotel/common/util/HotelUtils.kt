package com.tokopedia.hotel.common.util

import com.tokopedia.utils.date.*
import java.util.*

class HotelUtils {

    companion object {

        fun validateCheckInAndCheckOutDate(checkInDate: String, checkOutDate: String): Pair<String, String> {
            val todayWithoutTime = DateUtil.getCurrentCalendar().time.removeTime()
            val tomorrow = todayWithoutTime.addTimeToSpesificDate(Calendar.DATE, 1)
            val dayAfterTomorrow = todayWithoutTime.addTimeToSpesificDate(Calendar.DATE, 2)

            var updatedCheckInDate = checkInDate
            var updatedCheckOutDate = checkOutDate

            //if check in and / or check out param is empty
            if (updatedCheckInDate.isBlank() && updatedCheckOutDate.isBlank()) {
                updatedCheckInDate = tomorrow.toString(DateUtil.YYYY_MM_DD)
                updatedCheckOutDate = dayAfterTomorrow.toString(DateUtil.YYYY_MM_DD)
            } else if (updatedCheckInDate.isBlank()) {
                val checkout = updatedCheckOutDate.toDate(DateUtil.YYYY_MM_DD)
                val dayBeforeCheckOut = checkout.addTimeToSpesificDate(Calendar.DATE, -1)
                updatedCheckInDate = dayBeforeCheckOut.toString(DateUtil.YYYY_MM_DD)
            } else if (updatedCheckOutDate.isBlank()) {
                val checkin = updatedCheckInDate.toDate(DateUtil.YYYY_MM_DD)
                val dayAfterCheckIn = checkin.addTimeToSpesificDate(Calendar.DATE, 1)
                updatedCheckOutDate = dayAfterCheckIn.toString(DateUtil.YYYY_MM_DD)
            }

            //if check in date has passed
            if (DateUtil.getDayDiffFromToday(updatedCheckInDate) < 1) {
                updatedCheckInDate = tomorrow.toString(DateUtil.YYYY_MM_DD)
            }
            //if check out date has passed or check out date < check in date
            if (DateUtil.getDayDiffFromToday(updatedCheckOutDate) < 1) {
                val checkin = updatedCheckInDate.toDate(DateUtil.YYYY_MM_DD)
                val dayAfterCheckIn = checkin.addTimeToSpesificDate(Calendar.DATE, 1)
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