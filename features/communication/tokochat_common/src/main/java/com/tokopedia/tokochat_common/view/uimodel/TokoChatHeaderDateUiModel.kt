package com.tokopedia.tokochat_common.view.uimodel

import android.text.format.DateUtils
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.tokochat_common.util.TokoChatValueUtil.DATE_FORMAT
import com.tokopedia.tokochat_common.util.TokoChatValueUtil.HEADER_DATE_FORMAT
import com.tokopedia.tokochat_common.util.TokoChatValueUtil.RELATIVE_TODAY
import com.tokopedia.tokochat_common.util.TokoChatValueUtil.RELATIVE_YESTERDAY
import java.util.*

/**
 * Date will be used when dateTimeStamp is not from today or yesterday
 */
class TokoChatHeaderDateUiModel (
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

    companion object {

        /**
         * Get relative date for header date
         */
        fun getRelativeDate(
            date: String,
            dateTimestamp: Long,
        ): String {
            return when {
                DateUtils.isToday(dateTimestamp) -> RELATIVE_TODAY
                DateUtils.isToday(dateTimestamp + DateUtils.DAY_IN_MILLIS) -> RELATIVE_YESTERDAY
                else -> DateFormatUtils.formatDate(DATE_FORMAT, HEADER_DATE_FORMAT, date, Locale.ENGLISH)
            }
        }
    }
}
