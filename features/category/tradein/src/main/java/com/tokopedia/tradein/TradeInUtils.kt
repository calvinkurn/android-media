package com.tokopedia.tradein

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TradeInUtils {
    companion object {
        const val TIMER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm"

        fun parseData(date: String?, timerFormat: String = TIMER_DATE_FORMAT): Date? {
            return date?.let {
                try {
                    SimpleDateFormat(timerFormat, Locale.getDefault())
                        .parse(date)
                } catch (parseException: ParseException) {
                    null
                }
            }
        }

        fun isExpiryTimeOver(saleEndDate: String, timerFormat: String = TIMER_DATE_FORMAT): Boolean {
            if (saleEndDate.isEmpty()) return true
            val currentSystemTime = Calendar.getInstance().time
            val parsedDate = parseData(saleEndDate, timerFormat)
            return if (parsedDate != null) {
                currentSystemTime.time >= parsedDate.time
            } else {
                false
            }
        }
    }
}