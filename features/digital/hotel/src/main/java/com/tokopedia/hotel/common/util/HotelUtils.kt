package com.tokopedia.hotel.common.util

import com.tokopedia.common.travel.utils.TravelDateUtil
import java.text.SimpleDateFormat
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
    }

}