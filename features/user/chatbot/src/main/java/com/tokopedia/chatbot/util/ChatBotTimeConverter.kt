package com.tokopedia.chatbot.util

import android.text.format.DateUtils
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import java.text.SimpleDateFormat
import java.util.*

object ChatBotTimeConverter {

    fun getHourTime(replyTime: String): String {
        return try {
            formatTime(replyTime.toLong() /
                    BaseChatViewHolder.MILISECONDS)
        } catch (e: NumberFormatException) {
            replyTime
        }
    }

    private fun formatTime(unixTime: Long): String {
        val localeId = Locale("in", "ID")
        val postTime = Date(unixTime)
        val sdfHour = SimpleDateFormat("HH:mm", localeId)
        TimeZone.setDefault(null)
        sdfHour.timeZone = TimeZone.getDefault()
        return sdfHour.format(postTime)
    }

    fun getDateIndicatorTime(replyTime: String, today: String, yesterday: String): String {
        var time: String
        try {
            var myTime = replyTime.toLong()
            myTime = myTime / BaseChatViewHolder.MILISECONDS
            val date = Date(myTime)
            time = if (DateUtils.isToday(myTime)) {
                today
            } else if (DateUtils.isToday(myTime + DateUtils.DAY_IN_MILLIS)) {
                yesterday
            } else {
                val formatter = SimpleDateFormat("d MMM")
                formatter.format(date)
            }

        } catch (e: NumberFormatException) {
            time = replyTime
        }
        return time
    }
}