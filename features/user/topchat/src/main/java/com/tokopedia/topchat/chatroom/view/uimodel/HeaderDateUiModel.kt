package com.tokopedia.topchat.chatroom.view.uimodel

import android.text.format.DateUtils
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory
import com.tokopedia.topchat.common.util.Utils
import java.text.SimpleDateFormat
import java.util.*

class HeaderDateUiModel(
        val date: String = ""
) : Visitable<TopChatTypeFactory> {

    private val dateTimestamp: Long
        get() {
            return try {
                val formatter = SimpleDateFormat("d MMM yyyy", Utils.getLocale())
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
                else -> date
            }
        }

    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        private const val RELATIVE_TODAY = "Hari ini"
        private const val RELATIVE_YESTERDAY = "Kemarin"
    }
}