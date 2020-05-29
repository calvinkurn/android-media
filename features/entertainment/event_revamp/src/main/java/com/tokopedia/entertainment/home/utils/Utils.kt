package com.tokopedia.entertainment.home.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Author errysuprayogi on 07,February,2020
 */
class Utils {


    fun formatedSchedule(schedule: String): String? {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yy")
            val newsdf = SimpleDateFormat("dd\nMMM")
            val date: Date
            date = if (schedule.contains("-")) {
                val arr = schedule.split("-").toTypedArray()
                sdf.parse(arr[0].trim { it <= ' ' })
            } else {
                sdf.parse(schedule)
            }
            newsdf.format(date).toUpperCase()
        } catch (e: Exception) {
            ""
        }
    }
}