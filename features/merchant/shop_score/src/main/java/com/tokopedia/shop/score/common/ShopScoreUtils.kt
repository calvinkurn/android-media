package com.tokopedia.shop.score.common

import android.content.Context
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LightingColorFilter
import android.graphics.drawable.Drawable
import android.text.method.LinkMovementMethod
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.gm.common.constant.GoldMerchantUtil
import com.tokopedia.shop.score.R
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


object ShopScoreUtils {

    fun getLevelBarWhite(level: Int): Int {
        return when(level) {
            ShopScoreConstant.SHOP_SCORE_LEVEL_ONE -> R.drawable.ic_one_level_white
            ShopScoreConstant.SHOP_SCORE_LEVEL_TWO -> R.drawable.ic_two_level_white
            ShopScoreConstant.SHOP_SCORE_LEVEL_THREE -> R.drawable.ic_three_level_white
            ShopScoreConstant.SHOP_SCORE_LEVEL_FOUR -> R.drawable.ic_four_level_white
            else -> R.drawable.ic_no_level
        }
    }
}

fun GlobalError.setTypeGlobalError(throwable: Throwable?) {
    if (throwable is IOException) {
        setType(GlobalError.NO_CONNECTION)
    } else {
        setType(GlobalError.SERVER_ERROR)
    }
}

fun Typography.setTextMakeHyperlink(text: String, onClick: () -> Unit) {
    val htmlString = HtmlLinkHelper(context, text)
    this.movementMethod =  LinkMovementMethod.getInstance()
    this.highlightColor = Color.TRANSPARENT
    this.text = htmlString.spannedString
    htmlString.urlList.getOrNull(0)?.setOnClickListener {
        onClick()
    }
}

fun SortFilterItem.toggle() {
    type = if (type == ChipsUnify.TYPE_NORMAL) {
        ChipsUnify.TYPE_SELECTED
    } else {
        ChipsUnify.TYPE_NORMAL
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

fun getNPastMonthTimeStamp(monthBefore: Int): Date {
    val date = Calendar.getInstance(getLocale())
    date.set(Calendar.MONTH, date.get(Calendar.MONTH) - monthBefore)
    return date.time
}

fun getColoredIndicator(context: Context, colorRes: Int): Drawable? {
    val color = ContextCompat.getColor(context, colorRes)
    val drawable = MethodChecker.getDrawable(context, R.drawable.ic_penalty_indicator)
    val filter: ColorFilter = LightingColorFilter(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_Black), color)
    drawable.colorFilter = filter
    return drawable
}

fun rangeTotalDays(periodDate: String): Int {
    val splitPeriodDate = periodDate.split("-")
    val startDate = splitPeriodDate.getOrNull(0) ?: ""
    val endDate = splitPeriodDate.getOrNull(1) ?: ""
    return GoldMerchantUtil.totalRangeDays(startDate, endDate)
}