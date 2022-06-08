package com.tokopedia.shop.flashsale.common.util

import java.util.Date
import java.util.Calendar
import javax.inject.Inject

class DateManager @Inject constructor() {

    companion object {
        private const val ADVANCE_BY_ONE = 1
        private const val TWO_HOURS_FROM_NOW = 2
        private const val THREE_HOURS_FROM_NOW = 3
        private const val THREE_MONTH_FROM_NOW = 3
    }

    fun getCurrentMonth(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.MONTH) + ADVANCE_BY_ONE
    }

    fun getCurrentYear(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.YEAR)
    }


    fun getDefaultMinimumCampaignStartDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR_OF_DAY, THREE_HOURS_FROM_NOW)
        return calendar.time
    }

    fun getMinimumCampaignStartDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR_OF_DAY, TWO_HOURS_FROM_NOW)
        return calendar.time
    }

    fun getMaximumCampaignEndDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, THREE_MONTH_FROM_NOW)
        return calendar.time
    }


}

