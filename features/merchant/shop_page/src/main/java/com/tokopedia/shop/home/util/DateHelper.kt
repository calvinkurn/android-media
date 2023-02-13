package com.tokopedia.shop.home.util

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateHelper {

    private const val DEFAULT_SERVER_TIMEZONE = "Asia/Jakarta"
    const val SHOP_NPL_CAMPAIGN_WIDGET_MORE_THAT_1_DAY_DATE_FORMAT = "dd MMM yyyy  HH:mm"

    fun getDateFromString(dateString: String): Date {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        format.timeZone = TimeZone.getTimeZone(DEFAULT_SERVER_TIMEZONE)
        return try {
            format.parse(dateString)
        } catch (e: Exception) {
            e.printStackTrace()
            Date()
        }
    }

    fun String.millisecondsToDays(): Long {
        return TimeUnit.MILLISECONDS.toDays(this.toLongOrZero())
    }
}
