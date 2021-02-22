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

    fun getExpiredTime(expiredTimeString: String?): Date {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZ")
        return try {
            format.parse(expiredTimeString)
        } catch (e: Exception) {
            e.printStackTrace()
            Date()
        }
    }

    fun isExpired(serverTimeOffset: Long, expiredTime: Date?): Boolean {
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
}