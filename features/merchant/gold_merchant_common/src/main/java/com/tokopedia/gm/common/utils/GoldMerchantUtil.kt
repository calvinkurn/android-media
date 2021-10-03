package com.tokopedia.gm.common.utils

import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.gm.common.constant.*
import com.tokopedia.kotlin.extensions.view.orZero
import java.io.IOException
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
            val simpleDateFormat =
                SimpleDateFormat(PATTERN_DATE_SHOP_INFO, DateFormatUtils.DEFAULT_LOCALE)
            val joinDate = simpleDateFormat.parse(dateString)
            val diffInMs: Long = abs(System.currentTimeMillis() - joinDate?.time.orZero())
            return TimeUnit.DAYS.convert(diffInMs, TimeUnit.MILLISECONDS)
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    fun getIsExistingSellerStartMonday(dateString: String): Boolean {
        return try {
            val simpleDateFormat =
                SimpleDateFormat(PATTERN_DATE_SHOP_INFO, DateFormatUtils.DEFAULT_LOCALE)
            val calendar = Calendar.getInstance()
            simpleDateFormat.parse(dateString)?.let { calendar.time = it }
            calendar.add(Calendar.DATE, NEW_SELLER_DAYS)
            return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getIsExistingSellerRangeInMonday(dateString: String): Int {
        return try {
            val simpleDateFormat =
                SimpleDateFormat(PATTERN_DATE_SHOP_INFO, DateFormatUtils.DEFAULT_LOCALE)
            val calendar = Calendar.getInstance()
            simpleDateFormat.parse(dateString)?.let { calendar.time = it }
            calendar.add(Calendar.DATE, NEW_SELLER_DAYS)
            val nextMonday = (TOTAL_MONDAY_DAYS - calendar.get(Calendar.DAY_OF_WEEK)-1)
            calendar.add(Calendar.DATE, nextMonday)
            return calendar.get(Calendar.DAY_OF_WEEK)
        } catch (e: Exception) {
            e.printStackTrace()
            Calendar.SUNDAY-1
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

    fun getNNextDaysBasedOnFirstMonday(totalRemainderDays: Int, isAddedWeek: Boolean = false): Int {
        val calendar = Calendar.getInstance(getLocale())
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + totalRemainderDays)
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.TUESDAY -> SIX_NUMBER
            Calendar.WEDNESDAY -> FIVE_NUMBER
            Calendar.THURSDAY -> FOUR_NUMBER
            Calendar.FRIDAY -> THREE_NUMBER
            Calendar.SATURDAY -> TWO_NUMBER
            Calendar.SUNDAY -> if (isAddedWeek) ONE_NUMBER + SEVEN_NUMBER else ONE_NUMBER
            Calendar.MONDAY -> if (isAddedWeek) SEVEN_NUMBER else ZERO_NUMBER
            else -> ZERO_NUMBER
        }
    }

    fun GlobalError.setTypeGlobalError(throwable: Throwable?) {
        if (throwable is IOException) {
            setType(GlobalError.NO_CONNECTION)
        } else {
            setType(GlobalError.SERVER_ERROR)
        }
    }
}