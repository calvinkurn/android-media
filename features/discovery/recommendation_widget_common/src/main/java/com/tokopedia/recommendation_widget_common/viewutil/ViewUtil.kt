package com.tokopedia.recommendation_widget_common.viewutil

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.util.TypedValue
import androidx.annotation.ColorInt
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.DividerUnify

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
    fun validateHomeComponentDivider(
        dividerType: Int,
        dividerTop: DividerUnify?,
        dividerBottom: DividerUnify?
    ) {
        val DIVIDER_NO_DIVIDER = 0
        val DIVIDER_TOP = 1
        val DIVIDER_BOTTOM = 2
        val DIVIDER_TOP_AND_BOTTOM = 3
        //TODO delete hardcoded divider
//        when(channelModel?.channelConfig?.dividerType) {
        when(1) {
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
}