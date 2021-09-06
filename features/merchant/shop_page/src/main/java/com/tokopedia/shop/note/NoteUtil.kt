package com.tokopedia.shop.note

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

object NoteUtil {
    fun convertUnixToFormattedDate(time: Int): String {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale("in", "ID", ""))
        return getFormattedDate(dateFormat,time)
    }

    fun convertUnixToFormattedTime(time: Int): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale("in", "ID", ""))
        return getFormattedDate(dateFormat,time)
    }

    private fun getFormattedDate(dateFormat: SimpleDateFormat, time: Int): String{
        dateFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        return dateFormat.format(Date(time * DateUtils.SECOND_IN_MILLIS))
    }
}