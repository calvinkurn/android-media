package com.tokopedia.sellerhomecommon.utils

import com.tokopedia.kotlin.extensions.view.orZero
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created By @ilhamsuaib on 20/05/20
 */

object DateTimeUtil {

    private const val COUNTRY_ID = "ID"
    private const val LANGUAGE_ID = "in"
    const val FORMAT_DD_MMM_YYYY = "dd MMM yyyy"
    const val FORMAT_HOUR_24 = "HH:00"
    const val FORMAT_DD_MMMM_YYYY = "$FORMAT_DD_MMM_YYYY, $FORMAT_HOUR_24"
    const val FORMAT_DD_MM_YYYY = "dd-MM-yyyy"
    const val FORMAT_MMMM_YYYY = "MMMM yyyy"
    const val FORMAT_DD_MMM = "dd MMM"
    const val FORMAT_DD = "dd"
    const val FORMAT_MMM = "MMM"
    const val TIME_ZONE = "Asia/Jakarta"
    val ONE_DAY_MILLIS: Long = TimeUnit.DAYS.toMillis(1)

    val localeID = Locale("in", "ID")

    fun format(timeMillis: Long, pattern: String, locale: Locale = getLocale()): String {
        val sdf = SimpleDateFormat(pattern, locale)
        return sdf.format(timeMillis)
    }

    fun format(
        timeMillis: Long,
        pattern: String,
        timeZone: TimeZone,
        locale: Locale = getLocale()
    ): String {
        val sdf = SimpleDateFormat(pattern, locale).apply {
            setTimeZone(timeZone)
        }
        return sdf.format(timeMillis)
    }

    fun format(dateStr: String, sourcePattern: String, outputPattern: String): String {
        return try {
            val sdf = SimpleDateFormat(sourcePattern, getLocale())
            val sourceDate = sdf.parse(dateStr)
            val outputSdf = SimpleDateFormat(outputPattern, getLocale())
            sourceDate?.let {
                return outputSdf.format(it)
            }
            return dateStr
        } catch (e: Exception) {
            dateStr
        }
    }

    fun getNPastDaysTimestamp(daysBefore: Long): Long {
        return Calendar.getInstance(getLocale()).timeInMillis.minus(
            TimeUnit.DAYS.toMillis(
                daysBefore
            )
        )
    }

    fun getNNextDaysTimestamp(days: Long): Long {
        return Calendar.getInstance(getLocale()).timeInMillis.plus(TimeUnit.DAYS.toMillis(days))
    }

    fun getFormattedDate(daysBefore: Long, format: String): String {
        return format(getNPastDaysTimestamp(daysBefore), format)
    }

    fun getTimeInMillis(dateStr: String, format: String): Long {
        val sdf = SimpleDateFormat(format, getLocale())
        return try {
            val date = sdf.parse(dateStr)
            date?.time.orZero()
        } catch (e: Exception) {
            Timber.e(e)
            Date().time
        }
    }

    private fun getLocale(): Locale {
        return Locale(LANGUAGE_ID, COUNTRY_ID)
    }
}