package com.tokopedia.topchat.chatroom.view.uimodel

import android.text.format.DateUtils
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory
import java.text.SimpleDateFormat
import java.util.*

class HeaderDateUiModel : Visitable<TopChatTypeFactory> {

    val date: String

    constructor(date: String) {
        this.date = date
    }

    constructor(timeStamp: Long) {
        this.date = convertToDate(timeStamp)
    }

    private val formatter = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH)

    val dateTimestamp: Long
        get() {
            return try {
                formatter.parse(date).time
            } catch (e: Exception) {
                Calendar.getInstance().timeInMillis
            }
        }

    val relativeDate: String
        get() {
            return when {
                DateUtils.isToday(dateTimestamp) -> RELATIVE_TODAY
                DateUtils.isToday(dateTimestamp + DateUtils.DAY_IN_MILLIS) -> RELATIVE_YESTERDAY
                else -> DateFormatUtils.formatDate(DATE_FORMAT, HEADER_DATE_FORMAT, date, Locale.ENGLISH)
            }
        }

    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    private fun convertToDate(timeStamp: Long): String {
        return try {
            formatter.format(Date(timeStamp))
        } catch (e: Exception) {
            formatter.format(Date())
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other is HeaderDateUiModel) {
            return other.date == this.date
        }
        return false
    }

    override fun hashCode(): Int {
        return date.hashCode()
    }

    companion object {
        private const val RELATIVE_TODAY = "Hari ini"
        private const val RELATIVE_YESTERDAY = "Kemarin"
        private const val DATE_FORMAT = "d MMM yyyy"
        private const val HEADER_DATE_FORMAT = "d MMMM, yyyy"
    }
}