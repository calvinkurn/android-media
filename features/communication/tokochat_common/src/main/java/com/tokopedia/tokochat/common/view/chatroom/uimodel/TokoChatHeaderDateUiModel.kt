package com.tokopedia.tokochat.common.view.chatroom.uimodel

import com.tokopedia.tokochat.common.util.TokoChatTimeUtil.getRelativeDate

/**
 * Date will be used when dateTimeStamp is not from today or yesterday
 */
class TokoChatHeaderDateUiModel(
    var date: String,
    var dateTimestamp: Long
) {
    val relativeDate: String
        get() {
            return getRelativeDate(date, dateTimestamp)
        }

    override fun equals(other: Any?): Boolean {
        if (other is TokoChatHeaderDateUiModel) {
            return other.date == this.date
        }
        return false
    }

    override fun hashCode(): Int {
        return date.hashCode()
    }
}
