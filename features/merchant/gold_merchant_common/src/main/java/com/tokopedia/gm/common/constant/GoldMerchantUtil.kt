package com.tokopedia.gm.common.constant

import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.kotlin.extensions.view.orZero
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

object GoldMerchantUtil {

    fun isNewSeller(dateString: String, days: Int): Boolean {
        return try {
            val simpleDateFormat = SimpleDateFormat(PATTERN_DATE_SHOP_INFO, DateFormatUtils.DEFAULT_LOCALE)
            val joinDate = simpleDateFormat.parse(dateString)
            val diffInMs: Long = abs(System.currentTimeMillis() - joinDate?.time.orZero())
            val diff = TimeUnit.DAYS.convert(diffInMs, TimeUnit.MILLISECONDS)
            return diff < days
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    fun totalDays(dateString: String): Int {
        return try {
            val simpleDateFormat = SimpleDateFormat(PATTERN_DATE_SHOP_INFO, Locale.getDefault())
            val joinDate = simpleDateFormat.parse(dateString)
            val diffInMs: Long = abs(System.currentTimeMillis() - joinDate?.time.orZero())
            val diff = TimeUnit.DAYS.convert(diffInMs, TimeUnit.MILLISECONDS)
            return diff.toInt()
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    fun isTenureNewSeller(dateString: String): Boolean {
        return try {
            val simpleDateFormat = SimpleDateFormat(PATTERN_DATE_SHOP_INFO, DateFormatUtils.DEFAULT_LOCALE)
            val joinDate = simpleDateFormat.parse(dateString)
            val diffInMs: Long = abs(System.currentTimeMillis() - joinDate?.time.orZero())
            val diff = TimeUnit.DAYS.convert(diffInMs, TimeUnit.MILLISECONDS)
            return (diff in START_TENURE_EIGHTY_THREE until NEW_SELLER_DAYS)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}