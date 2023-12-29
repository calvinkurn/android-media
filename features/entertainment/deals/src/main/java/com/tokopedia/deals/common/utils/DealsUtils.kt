package com.tokopedia.deals.common.utils

import android.content.Context
import android.content.res.Resources
import androidx.annotation.StringRes
import com.tokopedia.deals.R
import com.tokopedia.deals.common.ui.dataview.ProductCategoryDataView
import com.tokopedia.kotlin.extensions.view.ONE
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import java.util.Date

object DealsUtils {

    private const val RUPIAH_FORMAT = "Rp %s"
    private const val timePattern = "d MMM yyyy"
    private const val timeZone = "Asia/Jakarta"
    private const val delimiter = "~"
    private const val ENTER_HTML = "<br><br>"
    private const val DOT_HTML = "\u2022"
    private const val TIME_MS = 1000L
    private val locale = Locale("in", "ID")
    private const val SPACE = " "
    private const val DOUBLE_SPACE = "  "
    private const val LINE_TRIM = ' '

    fun screenWidthDp() = Resources.getSystem().displayMetrics.run { widthPixels / density }
    fun screenWidthPx() = Resources.getSystem().displayMetrics.widthPixels
    fun screenHeightDp() = Resources.getSystem().displayMetrics.run { heightPixels / density }
    fun screenHeightPx() = Resources.getSystem().displayMetrics.heightPixels

    fun dpToPx(dp: Int): Int = (dp * Resources.getSystem().displayMetrics.density).toInt()

    fun getExpandableItemText(texts: String): String? {
        return if (texts.isNotEmpty()) {
            val splitArray = texts.split(delimiter).toTypedArray()
            val tncBuffer = StringBuilder()
            for (i in splitArray.indices) {
                val line = splitArray[i]
                if (i < splitArray.size - Int.ONE) {
                    tncBuffer.append(SPACE).append(DOT_HTML).append(DOUBLE_SPACE)
                        .append(line.trim { it <= LINE_TRIM }).append(ENTER_HTML)
                } else {
                    tncBuffer.append(SPACE).append(DOT_HTML).append(DOUBLE_SPACE).append(line.trim { it <= LINE_TRIM })
                }
            }
            tncBuffer.toString()
        } else {
            null
        }
    }

    fun convertToCurrencyString(value: Long): String {
        return String.format(RUPIAH_FORMAT, NumberFormat.getNumberInstance(locale).format(value))
    }

    fun convertEpochToString(time: Int): String {
        val sdf = SimpleDateFormat(timePattern, locale)
        sdf.timeZone = TimeZone.getTimeZone(timeZone)
        val epochTime = time * TIME_MS
        val date = Date(epochTime)
        return sdf.format(date)
    }

    fun getLabelColor(context: Context, label: String): ProductCategoryDataView {
        val color =  when(label){
            getString(context, R.string.deals_homepage_popular_tag) -> com.tokopedia.unifyprinciples.R.color.Unify_YN400
            getString(context, R.string.deals_homepage_new_deals_tag) -> com.tokopedia.unifyprinciples.R.color.Unify_BN500
            getString(context, R.string.deals_homepage_new_halal_tag) -> com.tokopedia.unifyprinciples.R.color.Unify_GN500
            getString(context, R.string.deals_homepage_new_hot_deals_tag) -> com.tokopedia.unifyprinciples.R.color.Unify_RN500
            else -> com.tokopedia.unifyprinciples.R.color.Unify_NN950_68
        }

        return ProductCategoryDataView(label, color)
    }

    fun getString(context: Context, @StringRes stringResId: Int): String {
        return context.getString(stringResId)
    }
}
