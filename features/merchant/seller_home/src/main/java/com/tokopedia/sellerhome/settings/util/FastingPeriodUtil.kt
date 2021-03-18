package com.tokopedia.sellerhome.settings.util

import java.util.*

object FastingPeriodUtil {

    private const val SUHOOR_START_TIME = 0
    private const val FASTING_START_TIME = 5
    private const val IFTAR_START_TIME = 17

    fun getFastingPeriod(): FastingPeriod {
        return when(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in SUHOOR_START_TIME until FASTING_START_TIME -> FastingPeriod.SUHOOR
            in FASTING_START_TIME until IFTAR_START_TIME -> FastingPeriod.FASTING
            else -> FastingPeriod.IFTAR
        }
    }

}