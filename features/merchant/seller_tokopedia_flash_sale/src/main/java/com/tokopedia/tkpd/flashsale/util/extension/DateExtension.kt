package com.tokopedia.tkpd.flashsale.util.extension

import java.util.Calendar
import java.util.Date

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