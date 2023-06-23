package com.tokopedia.shop.score.common

import android.content.Context
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LightingColorFilter
import android.graphics.drawable.Drawable
import android.text.method.LinkMovementMethod
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant.PATTER_DATE_EDT
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*



object ShopScoreUtils {

    fun getLevelBarWhite(level: Long): Int {
        return when (level) {
            ShopScoreConstant.SHOP_SCORE_LEVEL_ONE -> R.drawable.ic_one_level_white
            ShopScoreConstant.SHOP_SCORE_LEVEL_TWO -> R.drawable.ic_two_level_white
            ShopScoreConstant.SHOP_SCORE_LEVEL_THREE -> R.drawable.ic_three_level_white
            ShopScoreConstant.SHOP_SCORE_LEVEL_FOUR -> R.drawable.ic_four_level_white
            else -> R.drawable.ic_no_level
        }
    }
}


fun Typography.setTextMakeHyperlink(text: String, onClick: () -> Unit) {
    val htmlString = HtmlLinkHelper(context, text)
    this.movementMethod = LinkMovementMethod.getInstance()
    this.highlightColor = Color.TRANSPARENT
    this.text = htmlString.spannedString
    htmlString.urlList.getOrNull(0)?.setOnClickListener {
        onClick()
    }
}

fun getNowTimeStamp(): Long {
    val date = Calendar.getInstance(getLocale())
    return date.timeInMillis
}

fun format(timeMillis: Long, pattern: String, locale: Locale = getLocale()): String {
    val sdf = SimpleDateFormat(pattern, locale)
    return sdf.format(timeMillis)
}

fun getLocale(): Locale {
    return Locale("id")
}

fun getPastDaysPenaltyTimeStamp(shouldStartFromMonday: Boolean = false): Date {
    val date = Calendar.getInstance(getLocale())
    val totalDays = ShopScoreConstant.FOUR_WEEKS + getNPastDaysPenalty(shouldStartFromMonday)
    date.set(Calendar.DAY_OF_YEAR, date.get(Calendar.DAY_OF_YEAR) - totalDays)
    return date.time
}

fun getHistoryPenaltyTimeStamp(): Date {
    val date = Calendar.getInstance(getLocale())
    val totalDays = ShopScoreConstant.TWELVE_WEEKS + ShopScoreConstant.FOUR_WEEKS
    date.set(Calendar.DAY_OF_YEAR, date.get(Calendar.DAY_OF_YEAR) - totalDays)
    return date.time
}

fun getNotYetDeductedPenaltyTimeStamp(): Date {
    val date = Calendar.getInstance(getLocale())
    date.set(Calendar.DAY_OF_YEAR, date.get(Calendar.DAY_OF_YEAR) - getNPastDaysPenalty(true))
    return date.time
}

fun getNPastDaysPenalty(
    shouldStartFromMonday: Boolean = false
): Int {
    val calendar = Calendar.getInstance(getLocale())
    calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR))
    val nPastDays =
        when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SATURDAY -> ShopScoreConstant.SIX_NUMBER
            Calendar.FRIDAY -> ShopScoreConstant.FIVE_NUMBER
            Calendar.THURSDAY -> ShopScoreConstant.FOUR_NUMBER
            Calendar.WEDNESDAY -> ShopScoreConstant.THREE_NUMBER
            Calendar.TUESDAY -> ShopScoreConstant.TWO_NUMBER
            Calendar.MONDAY -> ShopScoreConstant.ONE_NUMBER
            else -> ShopScoreConstant.ZERO_NUMBER
        }
    return if (shouldStartFromMonday) {
        nPastDays - Int.ONE
    } else {
        nPastDays
    }
}

fun getNPastDaysTimeStamp(daysBefore: Int): Date {
    val date = Calendar.getInstance(getLocale())
    date.set(Calendar.DAY_OF_YEAR, date.get(Calendar.DAY_OF_YEAR) - daysBefore)
    return date.time
}

fun getColoredIndicator(context: Context, colorRes: Int): Drawable? {
    val color = ContextCompat.getColor(context, colorRes)
    val drawable = MethodChecker.getDrawable(context, R.drawable.ic_penalty_indicator)
    val filter: ColorFilter = LightingColorFilter(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_Black), color)
    drawable.colorFilter = filter
    return drawable
}


fun String.getNumberFormat(defaultNumber: Long): Long {
    return try {
        this.toLong()
    } catch (e: NumberFormatException) {
        defaultNumber
    }
}

fun String.convertToFormattedDate(): String? {
    return try {
        SimpleDateFormat(ShopScoreConstant.PATTERN_DATE_PARAM, getLocale()).parse(this)?.let {
            com.tokopedia.shop.score.common.format(it.time, PATTER_DATE_EDT, getLocale())
        }
    } catch (ex: Exception) {
        Timber.e(ex)
        null
    }
}
