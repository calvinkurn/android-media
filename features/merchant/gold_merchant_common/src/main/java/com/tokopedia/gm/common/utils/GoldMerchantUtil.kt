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

    fun getDayNameFromCreatedDate(dateString: String): Int {
        return try {
            val simpleDateFormat =
                SimpleDateFormat(PATTERN_DATE_SHOP_INFO, DateFormatUtils.DEFAULT_LOCALE)
            val calendar = Calendar.getInstance()
            simpleDateFormat.parse(dateString)?.let { calendar.time = it }
            calendar.add(Calendar.DATE, totalDays(dateString).toInt())
            return calendar.get(Calendar.DAY_OF_WEEK)
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    fun getNNStartShowProtectedParameterNewSeller(dateString: String): Long {
        return try {
            val calendar = Calendar.getInstance(DateFormatUtils.DEFAULT_LOCALE)
            val simpleDateFormat =
                SimpleDateFormat(PATTERN_DATE_SHOP_INFO, DateFormatUtils.DEFAULT_LOCALE)
            simpleDateFormat.parse(dateString)?.let { calendar.timeInMillis = it.time }
            val firstMonday = when (calendar.get(Calendar.DAY_OF_WEEK)) {
                Calendar.TUESDAY -> SIX_NUMBER
                Calendar.WEDNESDAY -> FIVE_NUMBER
                Calendar.THURSDAY -> FOUR_NUMBER
                Calendar.FRIDAY -> THREE_NUMBER
                Calendar.SATURDAY -> TWO_NUMBER
                Calendar.SUNDAY -> ONE_NUMBER
                Calendar.MONDAY -> SEVEN_NUMBER
                else -> ZERO_NUMBER
            }
            val shopDateCreatedInMs = calendar.timeInMillis
            calendar.add(Calendar.DAY_OF_YEAR, firstMonday)
            val diffInMs: Long = abs(shopDateCreatedInMs - calendar.timeInMillis)
            return TimeUnit.DAYS.convert(diffInMs, TimeUnit.MILLISECONDS)
        } catch (e: Exception) {
            e.printStackTrace()
            ZERO_NUMBER.toLong()
        }
    }

    fun isTenureNewSeller(dateString: String): Boolean {
        return (totalDays(dateString) in START_TENURE_EIGHTY_THREE until NEW_SELLER_DAYS)
    }

    fun format(timeMillis: Long, pattern: String, locale: Locale = DateFormatUtils.DEFAULT_LOCALE): String {
        val sdf = SimpleDateFormat(pattern, locale)
        return sdf.format(timeMillis)
    }

    fun getNNextDaysBasedOnFirstMonday(totalRemainderDays: Int, isAddedWeek: Boolean = false): Int {
        val calendar = Calendar.getInstance(DateFormatUtils.DEFAULT_LOCALE)
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

    fun getNNextDaysBasedOnShopScoreCalculation(dateString: String): String {
        val calendar = Calendar.getInstance(DateFormatUtils.DEFAULT_LOCALE)
        val simpleDateFormat =
            SimpleDateFormat(PATTERN_DATE_SHOP_INFO, DateFormatUtils.DEFAULT_LOCALE)
        simpleDateFormat.parse(dateString)?.let { calendar.time = it }
        val firstMonday = when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.TUESDAY -> SIX_NUMBER
            Calendar.WEDNESDAY -> FIVE_NUMBER
            Calendar.THURSDAY -> FOUR_NUMBER
            Calendar.FRIDAY -> THREE_NUMBER
            Calendar.SATURDAY -> TWO_NUMBER
            Calendar.SUNDAY -> ONE_NUMBER
            Calendar.MONDAY -> ZERO_NUMBER
            else -> ZERO_NUMBER
        }
        calendar.add(Calendar.DAY_OF_YEAR, firstMonday + THIRTY_DAYS)
        return format(calendar.timeInMillis, PATTERN_DATE_TEXT)
    }

    fun GlobalError.setTypeGlobalError(throwable: Throwable?) {
        if (throwable is IOException) {
            setType(GlobalError.NO_CONNECTION)
        } else {
            setType(GlobalError.SERVER_ERROR)
        }
    }
}