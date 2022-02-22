package com.tokopedia.affiliate.utils

import android.content.Context
import com.tokopedia.affiliate.TIME_ZONE
import com.tokopedia.affiliate.pattern
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomDatePicker
import com.tokopedia.affiliate_toko.R
import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
     fun getMessage(dayRange: String,context: Context? = null): String {
        val timeZone = TimeZone.getTimeZone(TIME_ZONE)
        val calendar = Calendar.getInstance()
        when(dayRange){
            AffiliateBottomDatePicker.TODAY -> {
                calendar.add(Calendar.HOUR,-2)
                val date = SimpleDateFormat(pattern, Locale.ENGLISH).format(calendar.time)
                val time = SimpleDateFormat("HH:00", Locale.ENGLISH).format(calendar.time)
                return "$date - ${context?.getString(R.string.terkhir_text)} $time"
            }
            AffiliateBottomDatePicker.YESTERDAY -> {
                calendar.add(Calendar.HOUR,-33)
                val dateFormat = SimpleDateFormat(pattern, Locale.ENGLISH)
                dateFormat.timeZone = timeZone
                return dateFormat.format(calendar.time)
            }
            AffiliateBottomDatePicker.SEVEN_DAYS -> {
                calendar.add(Calendar.HOUR,-33)
                val lastDayFormat = SimpleDateFormat(pattern, Locale.ENGLISH)
                lastDayFormat.timeZone = timeZone
                val lastDay = lastDayFormat.format(calendar.time)
                calendar.add(Calendar.HOUR,-144)
                val finalDayFormat = SimpleDateFormat(pattern, Locale.ENGLISH)
                finalDayFormat.timeZone = timeZone
                val finalDay = finalDayFormat.format(calendar.time)
                return "$finalDay - $lastDay"
            }
            AffiliateBottomDatePicker.THIRTY_DAYS -> {
                calendar.add(Calendar.HOUR,-33)
                val lastDayFormat = SimpleDateFormat(pattern, Locale.ENGLISH)
                lastDayFormat.timeZone = timeZone
                val lastDay = lastDayFormat.format(calendar.time)
                calendar.add(Calendar.HOUR,-696)
                val finalDayFormat = SimpleDateFormat(pattern, Locale.ENGLISH)
                finalDayFormat.timeZone = timeZone
                val finalDay = finalDayFormat.format(calendar.time)
                return "$finalDay - $lastDay"
            }
        }
        return ""
    }
}