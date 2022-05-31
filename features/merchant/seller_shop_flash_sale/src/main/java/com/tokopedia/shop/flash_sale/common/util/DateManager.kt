package com.tokopedia.shop.flash_sale.common.util

import java.util.*
import javax.inject.Inject

class DateManager @Inject constructor() {

    companion object {
        private const val ADVANCE_BY_ONE = 1
    }

    fun getCurrentMonth(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.MONTH) + ADVANCE_BY_ONE
    }

    fun getCurrentYear(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.YEAR)
    }


}