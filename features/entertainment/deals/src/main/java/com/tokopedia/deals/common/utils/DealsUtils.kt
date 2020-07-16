package com.tokopedia.deals.common.utils

import android.content.res.Resources
import java.text.NumberFormat
import java.util.*

object DealsUtils {

    private const val RUPIAH_FORMAT = "Rp %s"
    private val locale = Locale("in", "ID")

    val screenWidthDp = Resources.getSystem().displayMetrics.run { widthPixels / density }
    val screenWidthPx = Resources.getSystem().displayMetrics.widthPixels
    val screenHeightDp = Resources.getSystem().displayMetrics.run { heightPixels / density }
    val screenHeightPx = Resources.getSystem().displayMetrics.heightPixels

    fun dpToPx(dp: Int): Int = (dp * Resources.getSystem().displayMetrics.density).toInt()

    fun convertToCurrencyString(value: Long): String {
        return String.format(RUPIAH_FORMAT, NumberFormat.getNumberInstance(locale).format(value))
    }
}