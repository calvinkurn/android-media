package com.tokopedia.tkpd.flashsale.common.extension

import com.tokopedia.campaign.utils.constant.LocaleConstant
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

private const val ONE_MONTH_OFFSET = 1

fun Date.toCalendar(): Calendar {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar
}

fun Date.dateOnly(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.time
}

fun Date.removeTimeZone(): Date {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this.time + calendar.timeZoneOffsetInMillis
    return calendar.time
}

/**
 * This method will return time-zone offset in millis,
 * calculation based on recommended formula from now deprecated [Date.getTimezoneOffset]:
 *
 * > replaced by -(Calendar.get(Calendar.ZONE_OFFSET) + Calendar.get(Calendar.DST_OFFSET)) / (60 * 1000)
 *
 * However we need it in millis, so no need to divide with (60 * 1000) anymore.
 *
 * @return the time-zone offset, in millis, for the current time zone.
 */
private val Calendar.timeZoneOffsetInMillis: Int
    get() = -(get(Calendar.ZONE_OFFSET) + get(Calendar.DST_OFFSET))
