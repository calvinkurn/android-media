package com.tokopedia.hotel.common.util

import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.hotel.homepage.presentation.fragment.HotelHomepageFragment

class HotelUtils {

    companion object {
        fun countNightDifference(checkInDate: String, checkOutDate: String): Long =
                ((TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, checkOutDate).time -
                        TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, checkInDate).time)) / HotelHomepageFragment.ONE_DAY
    }

}