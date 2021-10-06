package com.tokopedia.ordermanagement.snapshot.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by fwidjaja on 1/28/21.
 */
object SnapshotUtils {

    private const val INPUT_PATTERN_WITH_MILLISECONDS = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    private const val INPUT_PATTERN_WITHOUT_MILLISECONDS = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    private const val OUTPUT_PATTERN = "dd MMM yyyy; HH:mm"

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun parseDate(time: String?): String? {
        val inputFormat = SimpleDateFormat(INPUT_PATTERN_WITH_MILLISECONDS, Locale.US)
        val outputFormat = SimpleDateFormat(OUTPUT_PATTERN, Locale.US)
        val date: Date?
        var str: String? = null
        try {
            date = inputFormat.parse(time)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            str = parseDateWithoutMilliseconds(time)
        }
        return str
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun parseDateWithoutMilliseconds(time: String?): String?{
        val inputFormat = SimpleDateFormat(INPUT_PATTERN_WITHOUT_MILLISECONDS, Locale.US)
        val outputFormat = SimpleDateFormat(OUTPUT_PATTERN, Locale.US)
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