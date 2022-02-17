package com.tokopedia.affiliate.utils

import android.content.Context
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomDatePicker
import com.tokopedia.affiliate_toko.R
import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    init {
        val TIME_ZONE = "Asia/Jakarta"
        TimeZone.setDefault(TimeZone.getTimeZone(TIME_ZONE))
    }
     fun getMessage(dayRange: String,context: Context? = null): String {
        val calendar = Calendar.getInstance()
        when(dayRange){
            AffiliateBottomDatePicker.TODAY -> {
                calendar.add(Calendar.HOUR,-2)
                val date = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(calendar.time)
                val time = SimpleDateFormat("HH:00", Locale.ENGLISH).format(calendar.time)
                return "$date - ${context?.getString(R.string.terkhir_text)} $time"
            }
            AffiliateBottomDatePicker.YESTERDAY -> {
                calendar.add(Calendar.HOUR,-33)
                return SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(calendar.time)
            }
            AffiliateBottomDatePicker.SEVEN_DAYS -> {
                calendar.add(Calendar.HOUR,-33)
                val lastDay = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(calendar.time)
                calendar.add(Calendar.HOUR,-144)
                val finalDay = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(calendar.time)
                return "$finalDay - $lastDay"
            }
            AffiliateBottomDatePicker.THIRTY_DAYS -> {
                calendar.add(Calendar.HOUR,-33)
                val lastDay = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(calendar.time)
                calendar.add(Calendar.HOUR,-696)
                val finalDay = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(calendar.time)
                return "$finalDay - $lastDay"
            }
        }
        return ""
    }
}