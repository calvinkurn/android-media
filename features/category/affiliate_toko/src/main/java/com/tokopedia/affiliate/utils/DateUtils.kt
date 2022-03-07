package com.tokopedia.affiliate.utils

import android.content.Context
import com.tokopedia.affiliate.*
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomDatePicker
import com.tokopedia.affiliate_toko.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Exception

class DateUtils {
     fun getMessage(dayRange: String,context: Context? = null): String {
        val timeZone = TimeZone.getTimeZone(TIME_ZONE)
        val calendar = Calendar.getInstance()
        when(dayRange){
            AffiliateBottomDatePicker.TODAY -> {
                return try {
                    calendar.add(Calendar.HOUR,-TWO)
                    val date = SimpleDateFormat(PATTERN, Locale.ENGLISH).format(calendar.time)
                    val time = SimpleDateFormat(HOUR_PATTERN, Locale.ENGLISH).format(calendar.time)
                    "$date - ${context?.getString(R.string.terkhir_text)} $time"
                } catch (e: Exception){
                    e.printStackTrace()
                    ""
                }

            }
            AffiliateBottomDatePicker.YESTERDAY -> {
                return try{
                    calendar.add(Calendar.HOUR,-THIRTY_THREE)
                    val dateFormat = SimpleDateFormat(PATTERN, Locale.ENGLISH)
                    dateFormat.timeZone = timeZone
                    dateFormat.format(calendar.time)
                } catch (e: Exception){
                    e.printStackTrace()
                    ""
                }
            }
            AffiliateBottomDatePicker.SEVEN_DAYS -> {
                return try {
                    calendar.add(Calendar.HOUR,-THIRTY_THREE)
                    val lastDayFormat = SimpleDateFormat(PATTERN, Locale.ENGLISH)
                    lastDayFormat.timeZone = timeZone
                    val lastDay = lastDayFormat.format(calendar.time)
                    calendar.add(Calendar.DATE,-SIX)
                    val finalDayFormat = SimpleDateFormat(PATTERN, Locale.ENGLISH)
                    finalDayFormat.timeZone = timeZone
                    val finalDay = finalDayFormat.format(calendar.time)
                    "$finalDay - $lastDay"
                } catch (e: Exception){
                    e.printStackTrace()
                    ""
                }

            }
            AffiliateBottomDatePicker.THIRTY_DAYS -> {
                return try {
                    calendar.add(Calendar.HOUR,-THIRTY_THREE)
                    val lastDayFormat = SimpleDateFormat(PATTERN, Locale.ENGLISH)
                    lastDayFormat.timeZone = timeZone
                    val lastDay = lastDayFormat.format(calendar.time)
                    calendar.add(Calendar.DATE,-TWENTY_NINE)
                    val finalDayFormat = SimpleDateFormat(PATTERN, Locale.ENGLISH)
                    finalDayFormat.timeZone = timeZone
                    val finalDay = finalDayFormat.format(calendar.time)
                    return "$finalDay - $lastDay"
                } catch (e: Exception){
                    e.printStackTrace()
                    ""
                }

            }
        }
        return ""
    }
}