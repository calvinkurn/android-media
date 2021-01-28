package com.tokopedia.ordermanagement.snapshot.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by fwidjaja on 1/28/21.
 */
object SnapshotUtils {
    fun parseDate(time: String?): String? {
        val inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val outputPattern = "dd MMM yyyy; HH:mm"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(outputPattern)
        var date: Date? = null
        var str: String? = null
        try {
            date = inputFormat.parse(time)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str
    }
}