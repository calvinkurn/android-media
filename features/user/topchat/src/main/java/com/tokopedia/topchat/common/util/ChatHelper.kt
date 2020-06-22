package com.tokopedia.topchat.common.util

import android.text.format.DateFormat
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import java.util.*

object ChatHelper {

    fun convertToRelativeDate(timeStamp: String): String {
        val smsTime = Calendar.getInstance()
        smsTime.timeInMillis = timeStamp.toLongOrZero()

        val now = Calendar.getInstance()

        val timeFormatString = "HH:mm"
        val dateTimeFormatString = "dd MMM"
        val dateTimeYearFormatString = "dd MMM yy"
        val HOURS = (60 * 60 * 60).toLong()
        return if ((now.get(Calendar.DATE) == smsTime.get(Calendar.DATE))
                && (now.get(Calendar.MONTH) == smsTime.get(Calendar.MONTH))) {
            DateFormat.format(timeFormatString, smsTime).toString()
        } else if ((now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1)
                && (now.get(Calendar.MONTH) == smsTime.get(Calendar.MONTH))) {
            "Kemarin"
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            DateFormat.format(dateTimeFormatString, smsTime).toString()
        } else {
            DateFormat.format(dateTimeYearFormatString, smsTime).toString()
        }
    }

}