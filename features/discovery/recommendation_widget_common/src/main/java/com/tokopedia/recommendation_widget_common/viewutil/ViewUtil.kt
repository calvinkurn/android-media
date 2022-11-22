package com.tokopedia.recommendation_widget_common.viewutil

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.util.TypedValue
import androidx.annotation.ColorInt
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifycomponents.toPx

/**
 * Created by yfsx on 5/3/21.
 */

fun convertDpToPixel(dp: Float, context: Context): Int {
    val r = context.resources
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.displayMetrics).toInt()
}

@ColorInt
fun Int.invertIfDarkMode(context: Context?): Int{
    return if (context != null && context.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) getContrastColor(this) else this
}

private fun getContrastColor(@ColorInt color: Int): Int {
    // Counting the perceptive luminance - human eye favors green color...
    val a = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255.0
    return if (a < 0.5) color else Color.WHITE
}

object ChannelWidgetUtil {
    private const val DIVIDER_NO_DIVIDER = 0
    private const val DIVIDER_TOP = 1
    private const val DIVIDER_BOTTOM = 2
    private const val DIVIDER_TOP_AND_BOTTOM = 3
    private const val DEFAULT_BOTTOM_PADDING = 8

    fun validateHomeComponentDivider(
        dividerType: Int,
        dividerSize: Int,
        dividerTop: DividerUnify?,
        dividerBottom: DividerUnify?
    ) {
        dividerTop?.layoutParams?.height = dividerSize.toPx()
        dividerBottom?.layoutParams?.height = dividerSize.toPx()
        when(dividerType) {
            DIVIDER_NO_DIVIDER -> {
                dividerTop?.invisible()
                dividerBottom?.gone()
            }
            DIVIDER_TOP -> {
                dividerTop?.visible()
                dividerBottom?.gone()
            }
            DIVIDER_BOTTOM -> {
                dividerTop?.invisible()
                dividerBottom?.visible()
            }
            DIVIDER_TOP_AND_BOTTOM -> {
                dividerTop?.visible()
                dividerBottom?.visible()
            }
        }
    }

    private fun DividerUnify.invisible() {
        this.layoutParams?.height = DEFAULT_BOTTOM_PADDING.toPx()
        this.setBackgroundResource(android.R.color.transparent)
        this.visible()
    }
}

fun Float.toDpInt(): Int = this.toPx().toInt()
