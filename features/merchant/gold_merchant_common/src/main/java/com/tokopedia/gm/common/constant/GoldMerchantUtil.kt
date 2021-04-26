package com.tokopedia.gm.common.constant

import com.tokopedia.kotlin.extensions.view.orZero
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.roundToInt

object GoldMerchantUtil {

    private const val LOCALE_ID = "id"

    infix fun String.diffDays(days: Int): Boolean {
        val simpleDateFormat = SimpleDateFormat(PATTERN_DATE_SHOP_INFO, Locale(LOCALE_ID))
        val joinDate = simpleDateFormat.parse(this)
        val diffInMs: Long = abs(System.currentTimeMillis() - joinDate?.time.orZero())
        val diff = TimeUnit.DAYS.convert(diffInMs, TimeUnit.MILLISECONDS)
        return diff < days
    }
    fun String.totalDays(): Int {
        val simpleDateFormat = SimpleDateFormat(PATTERN_DATE_SHOP_INFO, Locale.getDefault())
        val joinDate = simpleDateFormat.parse(this)
        val diffInMs: Long = abs(System.currentTimeMillis() - joinDate?.time.orZero())
        val diff = TimeUnit.DAYS.convert(diffInMs, TimeUnit.MILLISECONDS)
        return diff.toInt()
    }

    fun String.isTenureNewSeller(): Boolean {
        val simpleDateFormat = SimpleDateFormat(PATTERN_DATE_SHOP_INFO, Locale(LOCALE_ID))
        val joinDate = simpleDateFormat.parse(this)
        val diffInMs: Long = abs(System.currentTimeMillis() - joinDate?.time.orZero())
        val diff = TimeUnit.DAYS.convert(diffInMs, TimeUnit.MILLISECONDS)
        return (diff in START_TENURE_EIGHTY_THREE until NEW_SELLER_DAYS)
    }
}