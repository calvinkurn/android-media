package com.tokopedia.tokochat.common.util

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

object TokoChatTimeConverter {

    fun formatTime(unixTime: Long): String {
        val localeID = Locale("in", "ID")
        val postTime = Date(unixTime)
        val sdfHour = SimpleDateFormat("HH:mm", localeID)
        return sdfHour.format(postTime)
    }
}
