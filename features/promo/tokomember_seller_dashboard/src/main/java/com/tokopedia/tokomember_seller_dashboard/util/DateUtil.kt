package com.tokopedia.tokomember_seller_dashboard.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    @SuppressLint("SimpleDateFormat")
    fun getTimeFromUnix(calendar: Calendar): String {
        return SimpleDateFormat("HH:mm").format(calendar.time)
    }

    @SuppressLint("SimpleDateFormat")
    fun getDateFromUnix(calendar: Calendar): String {
        return SimpleDateFormat("yyyy-mm-dd").format(calendar.time)
    }
}