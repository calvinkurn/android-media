package com.tokopedia.sellerhomecommon.common.const

import androidx.annotation.StringDef
import com.tokopedia.sellerhomecommon.common.const.DateFilterType.Companion.DATE_TYPE_DAY
import com.tokopedia.sellerhomecommon.common.const.DateFilterType.Companion.DATE_TYPE_MONTH
import com.tokopedia.sellerhomecommon.common.const.DateFilterType.Companion.DATE_TYPE_TODAY
import com.tokopedia.sellerhomecommon.common.const.DateFilterType.Companion.DATE_TYPE_WEEK

/**
 * Created By @ilhamsuaib on 12/08/20
 */

@Retention(AnnotationRetention.SOURCE)
@StringDef(DATE_TYPE_TODAY, DATE_TYPE_DAY, DATE_TYPE_WEEK, DATE_TYPE_MONTH)
annotation class DateFilterType {

    companion object {
        const val DATE_TYPE_TODAY = "today"
        const val DATE_TYPE_DAY = "day"
        const val DATE_TYPE_WEEK = "week"
        const val DATE_TYPE_MONTH = "month"
    }
}