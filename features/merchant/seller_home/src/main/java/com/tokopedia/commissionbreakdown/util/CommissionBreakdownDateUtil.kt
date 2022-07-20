package com.tokopedia.commissionbreakdown.util

import java.util.*

object CommissionBreakdownDateUtil {

    fun areTheDatesSame(date1 : Date?, date2 : Date?): Boolean {
        if(date1 == null || date2 == null)
            return false
        val cal1 = Calendar.getInstance()
        val cal2 = Calendar.getInstance()
        cal1.time = date1
        cal2.time = date2
        return (cal1[Calendar.DAY_OF_YEAR] == cal2[Calendar.DAY_OF_YEAR] &&
                cal1[Calendar.YEAR] == cal2[Calendar.YEAR])
    }
}
