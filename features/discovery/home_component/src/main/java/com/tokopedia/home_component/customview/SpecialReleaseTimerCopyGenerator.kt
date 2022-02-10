package com.tokopedia.home_component.customview

import java.util.*

object SpecialReleaseTimerCopyGenerator {
    private const val TWO_DAYS_MILLISECOND = 172800000L
    private const val TWENTY_FOUR_HOURS = 86400000L
    private const val ONE_HOUR = 3600000L
    private const val ONE_MINUTE = 60000L
    private const val FIFTEEN_MINUTE = 900000L
    private const val TIMER_FORMAT = "Sisa %s %s"
    private const val DAYS = "Hari"
    private const val HOURS = "Jam"
    private const val MINUTES = "Menit"
    private const val SEGERA_BERAKHIR = "Segera Berakhir"

    fun getCopy(currentTimeDate: Date, expiredTimeDate: Date, offset: Long = 0L): String {
        val currentTime = currentTimeDate.time
        val expiredTime = expiredTimeDate.time

        val diff = expiredTime - currentTime
        if (diff > TWO_DAYS_MILLISECOND) {
            return ""
        } else if (
            diff <= TWO_DAYS_MILLISECOND && diff >= TWENTY_FOUR_HOURS
        ) {
            val daysRemaining = diff/TWENTY_FOUR_HOURS
            return String.format(TIMER_FORMAT, Math.round(daysRemaining.toDouble()), DAYS)
        } else if (
            diff < TWENTY_FOUR_HOURS && diff >= ONE_HOUR
        ) {
            val hoursRemaining = diff/ ONE_HOUR
            return String.format(TIMER_FORMAT, Math.round(hoursRemaining.toDouble()), HOURS)
        } else if (
            diff < ONE_HOUR && diff >= FIFTEEN_MINUTE
        ) {
            val minutesRemaining = diff/ ONE_MINUTE
            return String.format(TIMER_FORMAT, Math.round(minutesRemaining.toDouble()), MINUTES)
        } else if (
            diff < FIFTEEN_MINUTE
        ) {
            return SEGERA_BERAKHIR
        } else {
            return "Unknown"
        }
    }
}