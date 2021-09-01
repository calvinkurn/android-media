package com.tokopedia.gm.common.utils

import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.gm.common.constant.*
import com.tokopedia.kotlin.extensions.view.orZero
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

object GoldMerchantUtil {

    fun isNewSeller(dateString: String): Boolean {
        return totalDays(dateString) < NEW_SELLER_DAYS
    }
    fun totalDays(dateString: String): Long {
        return try {
            val simpleDateFormat = SimpleDateFormat(PATTERN_DATE_SHOP_INFO, DateFormatUtils.DEFAULT_LOCALE)
            val joinDate = simpleDateFormat.parse(dateString)
            val diffInMs: Long = abs(System.currentTimeMillis() - joinDate?.time.orZero())
            return TimeUnit.DAYS.convert(diffInMs, TimeUnit.MILLISECONDS)
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    fun isTenureNewSeller(dateString: String): Boolean {
        return (totalDays(dateString) in START_TENURE_EIGHTY_THREE until NEW_SELLER_DAYS)
    }

    fun getLocale(): Locale {
        return Locale("id")
    }

    fun format(timeMillis: Long, pattern: String, locale: Locale = getLocale()): String {
        val sdf = SimpleDateFormat(pattern, locale)
        return sdf.format(timeMillis)
    }

    //shop age = 7
    //target = 59
    //diff = 69 - 7 = 52
    //now + diff + H+ (Monday)
    fun getNNextDaysBasedOnFirstMonday(totalRemainderDays: Int): Int {
        val calendar = Calendar.getInstance(getLocale())
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + totalRemainderDays)
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.TUESDAY -> SIX_NUMBER
            Calendar.WEDNESDAY -> FIVE_NUMBER
            Calendar.THURSDAY -> FOUR_NUMBER
            Calendar.FRIDAY -> THREE_NUMBER
            Calendar.SATURDAY -> TWO_NUMBER
            Calendar.SUNDAY -> ONE_NUMBER
            Calendar.MONDAY -> ZERO_NUMBER
            else -> ZERO_NUMBER
        }
    }
}