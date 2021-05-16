package com.tokopedia.gm.common.constant

import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.kotlin.extensions.view.orZero
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import kotlin.math.abs

object GoldMerchantUtil {

    fun isNewSeller(dateString: String, days: Int): Boolean {
        return totalDays(dateString) < days
    }
    fun totalDays(dateString: String): Int {
        return try {
            val simpleDateFormat = SimpleDateFormat(PATTERN_DATE_SHOP_INFO, DateFormatUtils.DEFAULT_LOCALE)
            val joinDate = simpleDateFormat.parse(dateString)
            val diffInMs: Long = abs(System.currentTimeMillis() - joinDate?.time.orZero())
            val diff = TimeUnit.DAYS.convert(diffInMs, TimeUnit.MILLISECONDS)
            return diff.toInt() + 1
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    fun totalRangeDays(startDate: String, enDate: String): Int {
        return try {
            val simpleDateFormat = SimpleDateFormat(PATTERN_RANGE_DATE_TEXT, DateFormatUtils.DEFAULT_LOCALE)
            val startDateParse = simpleDateFormat.parse(startDate)
            val endDateParse = simpleDateFormat.parse(enDate)
            val diffInMs: Long = abs(startDateParse?.time.orZero() - endDateParse?.time.orZero())
            val diff = TimeUnit.DAYS.convert(diffInMs, TimeUnit.MILLISECONDS)
            return diff.toInt() + 1
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    fun isTenureNewSeller(dateString: String): Boolean {
        return (totalDays(dateString) in START_TENURE_EIGHTY_THREE until NEW_SELLER_DAYS)
    }
}