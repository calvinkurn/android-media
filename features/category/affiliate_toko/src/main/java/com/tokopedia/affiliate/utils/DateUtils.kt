package com.tokopedia.affiliate.utils

import android.content.Context
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomDatePicker
import com.tokopedia.affiliate_toko.R
import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
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
                calendar.add(Calendar.DATE,-1)
                return SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(calendar.time)
            }
            AffiliateBottomDatePicker.SEVEN_DAYS -> {
                calendar.add(Calendar.DATE,-1)
                val lastDay = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(calendar.time)
                calendar.add(Calendar.DATE,-6)
                val finalDay = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(calendar.time)
                return "$finalDay - $lastDay"
            }
            AffiliateBottomDatePicker.THIRTY_DAYS -> {
                calendar.add(Calendar.DATE,-1)
                val lastDay = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(calendar.time)
                calendar.add(Calendar.DATE,-29)
                val finalDay = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(calendar.time)
                return "$finalDay - $lastDay"
            }
        }
        return ""
    }
}