package com.tokopedia.shop.review.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {
    fun generateTimeYearly(postTime: String): String {
        val localeID = Locale("en", "ID")
        return try {
            val sdfDay = SimpleDateFormat("dd MMM", localeID)
            val sdfYear = SimpleDateFormat("dd MMM yyyy", localeID)
            val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm", localeID)
            val postDate = sdf.parse(postTime)
            val currentTime = Date()
            val calPostDate = Calendar.getInstance()
            calPostDate.time = postDate
            val calCurrentTime = Calendar.getInstance()
            calCurrentTime.time = currentTime
            if (calCurrentTime[Calendar.YEAR] == calPostDate[Calendar.YEAR]) sdfDay.format(postDate) else {
                sdfYear.format(postDate)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            postTime
        }
    }
}