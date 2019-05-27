package com.tokopedia.hotel.common.util

import com.tokopedia.common.travel.utils.TravelDateUtil
import java.util.concurrent.TimeUnit

class HotelUtils {

    companion object {
        val ONE_DAY: Long = TimeUnit.DAYS.toMillis(1)

        fun countNightDifference(checkInDate: String, checkOutDate: String): Long =
                ((TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, checkOutDate).time -
                        TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, checkInDate).time)) / ONE_DAY
    }

}