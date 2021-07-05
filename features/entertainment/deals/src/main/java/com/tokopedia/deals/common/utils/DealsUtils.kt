package com.tokopedia.deals.common.utils

import android.content.Context
import android.content.res.Resources
import androidx.annotation.StringRes
import com.tokopedia.deals.R
import com.tokopedia.deals.common.ui.dataview.ProductCategoryDataView
import java.text.NumberFormat
import java.util.*

object DealsUtils {

    private const val RUPIAH_FORMAT = "Rp %s"
    private val locale = Locale("in", "ID")

    fun screenWidthDp() = Resources.getSystem().displayMetrics.run { widthPixels / density }
    fun screenWidthPx() = Resources.getSystem().displayMetrics.widthPixels
    fun screenHeightDp() = Resources.getSystem().displayMetrics.run { heightPixels / density }
    fun screenHeightPx() = Resources.getSystem().displayMetrics.heightPixels

    fun dpToPx(dp: Int): Int = (dp * Resources.getSystem().displayMetrics.density).toInt()

    fun convertToCurrencyString(value: Long): String {
        return String.format(RUPIAH_FORMAT, NumberFormat.getNumberInstance(locale).format(value))
    }

    fun getLabelColor(context: Context, label: String): ProductCategoryDataView {
        val color =  when(label){
            getString(context, R.string.deals_homepage_popular_tag) -> com.tokopedia.unifyprinciples.R.color.Unify_Y400
            getString(context, R.string.deals_homepage_new_deals_tag) -> com.tokopedia.unifyprinciples.R.color.Unify_B500
            getString(context, R.string.deals_homepage_new_halal_tag) -> com.tokopedia.unifyprinciples.R.color.Unify_G500
            getString(context, R.string.deals_homepage_new_hot_deals_tag) -> com.tokopedia.unifyprinciples.R.color.Unify_R500
            else -> com.tokopedia.unifyprinciples.R.color.Unify_N700_68
        }

        return ProductCategoryDataView(label, color)
    }

    fun getString(context: Context, @StringRes stringResId: Int): String {
        return context.getString(stringResId)
    }
}