package com.tokopedia.home_component.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by DevAra on 28/04/20.
 */
object DateHelper {

    private const val DEFAULT_VIEW_FORMAT = "dd MMM yyyy"
    private val DEFAULT_LOCALE = Locale("in", "ID")

    fun getExpiredTime(
        expiredTimeString: String?,
        format: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZ")
    ): Date {
        return try {
            format.parse(expiredTimeString)
        } catch (e: Exception) {
            e.printStackTrace()
            Date()
        }
    }

    fun isExpired(serverTimeOffset: Long = 0L, expiredTime: Date?): Boolean {
        val serverTime = Date(System.currentTimeMillis())
        serverTime.time = serverTime.time + serverTimeOffset
        return serverTime.after(expiredTime)
    }

    fun formatDateToUi(date: Date): String? {
        return try {
            val toFormat: DateFormat = SimpleDateFormat(DEFAULT_VIEW_FORMAT, DEFAULT_LOCALE)
            toFormat.isLenient = false
            toFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun getCountdownTimer(expiredTime: Date): Calendar {
        return Calendar.getInstance().apply {
            val currentDate = Date()
            val currentMillisecond: Long = currentDate.time + 0
            val timeDiff = expiredTime.time - currentMillisecond
            add(Calendar.SECOND, (timeDiff / 1000 % 60).toInt())
            add(Calendar.MINUTE, (timeDiff / (60 * 1000) % 60).toInt())
            add(Calendar.HOUR, (timeDiff / (60 * 60 * 1000)).toInt())
        }
    }
}
