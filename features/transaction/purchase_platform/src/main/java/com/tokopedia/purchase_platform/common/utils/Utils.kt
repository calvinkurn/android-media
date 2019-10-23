package com.tokopedia.purchase_platform.common.utils

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.util.DisplayMetrics
import com.tokopedia.design.utils.CurrencyFormatUtil
import kotlin.math.roundToInt

/**
 * Created by fwidjaja on 2019-04-25.
 */
object Utils {
    @JvmStatic
    fun getFormattedCurrency(price: Int): String {
        return if (price == 0) "" else CurrencyFormatUtil.getThousandSeparatorString(price.toDouble(), false, 0).formattedString

    }

    @JvmStatic
    fun getHtmlFormat(text: String): String {
        if (TextUtils.isEmpty(text)) {
            return SpannableStringBuilder("").toString()
        }
        val replacedText = text.replace("&amp;", "&")
        val result: Spanned
        result = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> Html.fromHtml(replacedText, Html.FROM_HTML_MODE_LEGACY)
            else -> Html.fromHtml(replacedText)
        }
        return result.toString()
    }

    @JvmStatic
    fun convertDpToPixel(dp: Float, context: Context): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        val px = dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        return px.roundToInt()
    }
}