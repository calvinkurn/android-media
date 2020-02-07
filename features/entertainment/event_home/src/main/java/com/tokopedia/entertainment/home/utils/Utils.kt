package com.tokopedia.entertainment.home.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Author errysuprayogi on 07,February,2020
 */
class Utils {

    var sdf: SimpleDateFormat = SimpleDateFormat("dd'/'MM'/'yy", Locale("in", "ID", ""))
    var newsdf: SimpleDateFormat = SimpleDateFormat("dd':'MMM'", Locale("in", "ID", ""))


    fun formatedSchedule(schedule: String): String {
        var date = Date()
        if(schedule.contains("-")){
            var arr = schedule.split("-")
            date = sdf.parse(arr[0].trim())
        } else{
            date = sdf.parse(schedule)
        }
        return newsdf.format(date)
    }
}