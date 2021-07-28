package com.tokopedia.gm.common.constant

import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.kotlin.extensions.view.orZero
import java.text.SimpleDateFormat
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
}