package com.tokopedia.shop.home.util

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateHelper {

    private const val DEFAULT_SERVER_TIMEZONE = "Asia/Jakarta"
    const val SHOP_NPL_CAMPAIGN_WIDGET_MORE_THAT_1_DAY_DATE_FORMAT = "dd MMM yyyy  HH : mm"
    const val SHOP_CAMPAIGN_BANNER_TIMER_MORE_THAN_1_DAY_DATE_FORMAT = "dd MMM yyyy | HH : mm"
    const val SHOP_CAMPAIGN_BANNER_TIMER_MORE_THAN_1_DAY_DATE_FORMAT_ENDED = "dd MMM yyyy | HH:mm"
    fun getDateFromString(
        dateString: String,
        timeZone: TimeZone? = TimeZone.getTimeZone(DEFAULT_SERVER_TIMEZONE)
    ): Date {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        if(null!= timeZone){
            format.timeZone = timeZone
        }
        return try {
            format.parse(dateString)
        } catch (e: Exception) {
            e.printStackTrace()
            Date()
        }
    }

    fun getDefaultTimeZone(): TimeZone {
        return TimeZone.getTimeZone(DEFAULT_SERVER_TIMEZONE)
    }

    fun String.millisecondsToDays(): Long {
        return TimeUnit.MILLISECONDS.toDays(this.toLongOrZero())
    }

    fun Long.millisecondsToDays(): Long {
        return TimeUnit.MILLISECONDS.toDays(this)
    }
}
