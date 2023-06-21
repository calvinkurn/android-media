package com.tokopedia.sellerhomecommon.common

import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.asUpperCase
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by @ilhamsuaib on 16/05/23.
 */

object DataTemplateUtils {

    private const val YESTERDAY_PAST_7_DAYS = "{DATE_YESTERDAY_PAST_7D}"
    private const val YESTERDAY = "{DATE_YESTERDAY}"
    private const val TODAY_WIB = "{NOW_DD_MMMM_YYYY_hh:mm_WIB}"
    private const val PREV_1_DAY = 1L
    private const val PREV_7_DAYS = 7L
    private const val TODAY_DATE_FORMAT = "dd MMMM yyyy (HH:00 z)"
    private const val TIME_ZONE = "Asia/Jakarta"
    private const val TEMPLATE_PATTERN = "\\{([^}]*?)\\}"

    fun parseDateTemplate(text: String): String {
        val regex = mapOf(
            YESTERDAY_PAST_7_DAYS to {
                DateTimeUtil.getFormattedDate(PREV_7_DAYS, DateTimeUtil.FORMAT_DD_MMM_YY)
                    .asUpperCase()
            },
            YESTERDAY to {
                DateTimeUtil.getFormattedDate(PREV_1_DAY, DateTimeUtil.FORMAT_DD_MMM_YY)
                    .asUpperCase()
            },
            TODAY_WIB to {
                val todayInMillis = System.currentTimeMillis().minus(
                    TimeUnit.HOURS.toMillis(Int.ONE.toLong())
                )
                DateTimeUtil.format(
                    todayInMillis,
                    TODAY_DATE_FORMAT,
                    TimeZone.getTimeZone(TIME_ZONE)
                )
            }
        )

        val pattern = TEMPLATE_PATTERN.toRegex()
        return pattern.replace(text) {
            regex[it.value]?.invoke() ?: it.value
        }
    }
}