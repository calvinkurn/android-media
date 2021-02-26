package com.tokopedia.ordermanagement.snapshot.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by fwidjaja on 1/28/21.
 */
object SnapshotUtils {
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun parseDate(time: String?): String? {
        val inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val outputPattern = "dd MMM yyyy; HH:mm"
        val inputFormat = SimpleDateFormat(inputPattern, Locale.US)
        val outputFormat = SimpleDateFormat(outputPattern, Locale.US)
        val date: Date?
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