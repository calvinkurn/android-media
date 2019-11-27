package com.tokopedia.salam.umrah.common.util

import com.tokopedia.common.travel.utils.TravelDateUtil
import java.text.SimpleDateFormat
import java.util.*
/**
 * @author by M on 7/11/2019
 */
object UmrahDateUtil {
    fun getDate(format: String, date: String): String? {
        return TravelDateUtil.dateToString(format, TravelDateUtil
                .stringToDate(TravelDateUtil.YYYY_MM_DD, date))
    }

    fun getDay(format: String, date: String): String? {
        return SimpleDateFormat(format, Locale("id")).format(TravelDateUtil
                .stringToDate(TravelDateUtil.YYYY_MM_DD, date))
    }

    fun getDateGregorianID(date: Array<Int>, format: String): String{
        val date = "${date[2]}-${date[1]+1}-${date[0]}"
        return SimpleDateFormat(format, Locale("id")).format(TravelDateUtil
                .stringToDate(TravelDateUtil.YYYY_MM_DD, date))
    }

    fun getDateGregorian(date: Array<Int>): String{
        return "${date[2]}-${date[1]+1}-${date[0]}"
    }
}