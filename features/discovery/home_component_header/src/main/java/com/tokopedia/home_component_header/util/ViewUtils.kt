package com.tokopedia.home_component_header.util

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.PaintDrawable
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.toPx

/**
 * Created by Frenzel 13/06/23
 */

internal object ViewUtils {
    private const val COLOR_WHITE = 0xffffff
    private const val FORMAT_HEX_COLOR = "#%06x"
    private const val DEFAULT_ROUNDED_CORNER = 12f

    fun View.setGradientBackground(colorArray: ArrayList<String>) {
        try {
            if (colorArray.size > 1) {
                val colors = IntArray(colorArray.size)
                for (i in 0 until colorArray.size) {
                    colors[i] = Color.parseColor(colorArray[i])
                }
                val gradient = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
                gradient.cornerRadius = 0f
                this.background = gradient
            } else {
                this.setBackgroundColor(Color.parseColor(colorArray[0]))
            }
        } catch (_: Exception) {
        }
    }

    fun View.setGradientBackgroundRounded(colorArray: ArrayList<String>, cornerRadius: Float = DEFAULT_ROUNDED_CORNER) {
        try {
            val drawable: Drawable
            if (colorArray.size > 1) {
                val colors = IntArray(colorArray.size)
                for (i in 0 until colorArray.size) {
                    colors[i] = Color.parseColor(colorArray[i])
                }
                drawable = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
                drawable.cornerRadius = cornerRadius.toDpFloat()
            } else {
                drawable = PaintDrawable(Color.parseColor(colorArray[0]))
                drawable.setCornerRadius(cornerRadius.toDpFloat())
            }
            this.background = drawable
        } catch (_: Exception) { }
    }

    // function check is gradient all white, if empty default color is white
    fun getGradientBackgroundViewAllWhite(colorArray: ArrayList<String>, context: Context): Boolean {
        val colorWhite = getHexColorFromIdColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White)
        if (colorArray.isNotEmpty()) {
            if (colorArray.size > 1) {
                val colorArrayNotWhite = colorArray.filter { !it.take(7).equals(colorWhite, ignoreCase = true) }
                if (colorArrayNotWhite.isNotEmpty()) {
                    return false
                }
                return true
            } else {
                return colorArray[0].equals(colorWhite, true)
            }
        } else {
            return true
        }
    }

    fun convertDpToPixel(dp: Float, context: Context): Int {
        val r = context.resources
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.displayMetrics).toInt()
    }

    fun Float.toDpInt(): Int = this.toPx().toInt()

    fun Float.toDpFloat(): Float = this.toPx()

    /**
     *
     * @param context
     * @param idColor example R(dot)color(dot)your_color
     * @return with example (hashtag)FFFFFF hex color uppercase
     *
     */
    private fun getHexColorFromIdColor(context: Context, idColor: Int): String {
        return try {
            String.format(FORMAT_HEX_COLOR, ContextCompat.getColor(context, idColor) and COLOR_WHITE).uppercase()
        } catch (_: Exception) {
            ""
        }
    }

    @ColorInt
    fun Int.invertIfDarkMode(context: Context?): Int {
        return if (context != null && context.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) getContrastColor(this) else this
    }

    private fun getContrastColor(@ColorInt color: Int): Int {
        // Counting the perceptive luminance - human eye favors green color...
        val a = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255.0
        return if (a < 0.5) color else Color.WHITE
    }
}
