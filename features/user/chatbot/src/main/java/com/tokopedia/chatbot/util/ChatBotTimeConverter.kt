package com.tokopedia.chatbot.util

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
}