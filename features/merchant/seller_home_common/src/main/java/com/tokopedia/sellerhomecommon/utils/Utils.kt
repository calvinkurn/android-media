package com.tokopedia.sellerhomecommon.utils

import android.content.Context
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.BaseWidgetUiModel
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil.FORMAT_DD_MMM_YYYY
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil.FORMAT_HH_MM
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil.ONE_DAY_MILLIS
import java.util.*

/**
 * Created By @ilhamsuaib on 20/05/20
 */

object Utils {

    inline fun <reified T : BaseWidgetUiModel<*>> getWidgetDataKeys(widgets: List<BaseWidgetUiModel<*>>?): List<String> {
        return widgets.orEmpty().filterIsInstance<T>().map { it.dataKey }
    }

    fun fromHtmlWithoutExtraSpace(text: String): String {
        return text.replace(Regex("<p.*?>|</p.*?>|\\n*\$"), "")
    }

    object LastUpdated {

        fun getCopy(context: Context, timeInMillis: Long): String {
            val nowMillis = Date().time
            val nowCal = getCalendar(nowMillis)
            val lastUpdatedCal = getCalendar(timeInMillis)
            val isSameDay = lastUpdatedCal.time == nowCal.time
            val isYesterday = lastUpdatedCal.timeInMillis
                .minus(nowCal.timeInMillis) < ONE_DAY_MILLIS
            return when {
                isSameDay -> {
                    context.getString(
                        R.string.shc_last_updated_same_day,
                        DateTimeUtil.format(timeInMillis, FORMAT_HH_MM)
                    )
                }
                isYesterday -> {
                    context.getString(R.string.shc_last_updated_yesterday)
                }
                else -> {
                    context.getString(
                        R.string.shc_last_updated_more_then_yesterday,
                        DateTimeUtil.format(timeInMillis, FORMAT_DD_MMM_YYYY)
                    )
                }
            }
        }

        private fun getCalendar(timeInMillis: Long): Calendar {
            return Calendar.getInstance().apply {
                time = Date(timeInMillis)
                set(Calendar.HOUR_OF_DAY, DateTimeUtil.ZERO)
                set(Calendar.MINUTE, DateTimeUtil.ZERO)
                set(Calendar.SECOND, DateTimeUtil.ZERO)
                set(Calendar.MILLISECOND, DateTimeUtil.ZERO)
            }
        }
    }
}