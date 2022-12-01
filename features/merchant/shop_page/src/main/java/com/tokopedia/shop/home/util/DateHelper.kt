package com.tokopedia.shop.home.util

import com.tokopedia.utils.date.DateUtil
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateHelper {

    private const val DEFAULT_SERVER_TIMEZONE = "Asia/Jakarta"
    const val SHOP_NPL_CAMPAIGN_WIDGET_MORE_THAT_1_DAY_DATE_FORMAT = "dd MMMM yyyy  HH:mm"

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

    fun Date.getDayDiffFromTodayWithoutTrim(): Long {
        val leftDate = this
        val rightDate = DateUtil.getCurrentCalendar().time
        val diff = leftDate.time - rightDate.time
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
    }
}
