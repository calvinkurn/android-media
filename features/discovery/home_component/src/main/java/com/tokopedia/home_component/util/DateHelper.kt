package com.tokopedia.home_component.util

import android.annotation.SuppressLint
import com.tokopedia.kotlin.extensions.view.orZero
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by DevAra on 28/04/20.
 */
object DateHelper {

    private const val DEFAULT_VIEW_FORMAT = "dd MMM yyyy"
    private val DEFAULT_LOCALE = Locale("in", "ID")

    @SuppressLint("SimpleDateFormat")
    fun getExpiredTime(
        expiredTimeString: String?,
        format: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZ")
    ): Date {
        return try {
            expiredTimeString?.let { format.parse(it) } ?: Date()
        } catch (_: Exception) {
            Date()
        }
    }

    fun isExpired(serverTimeOffset: Long = 0L, expiredTime: Date?): Boolean {
        val serverTime = Date(System.currentTimeMillis()).time + serverTimeOffset
        return expiredTime?.time.orZero() <= serverTime
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
