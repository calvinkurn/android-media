package com.tokopedia.mvc.presentation.creation.util

import java.util.*

object CreationUtil {

    private const val ROUNDED_PERIOD_IN_MINUTES = 30
    private const val ROUNDED_PERIOD_HAPPEN_PER_DAY = 4

    fun Date.roundTimePerHalfHour(): Date {
        val calendar = Calendar.getInstance()
        calendar.time = this

        val unroundedMinutes: Int = calendar.get(Calendar.MINUTE)
        val mod = unroundedMinutes % ROUNDED_PERIOD_IN_MINUTES
        calendar.add(
            Calendar.MINUTE,
            if (mod < ROUNDED_PERIOD_HAPPEN_PER_DAY) -mod else ROUNDED_PERIOD_IN_MINUTES - mod
        )
        return calendar.time
    }
}
