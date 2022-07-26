package com.tokopedia.home_component.util

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat

/**
 * Created by Lukas on 28/09/20.
 */

const val COLOR_DEFAULT_VAL = 1
const val RED_MULTIPLIER = 0.299
const val GREEN_MULTIPLIER = 0.587
const val BLUE_MULTIPLIER = 0.114
const val COLOR_DIVIDER_VAL = 255.0
const val COLOR_THRESHOLD = 0.5
const val COLOR_WHITE = 0xffffff
const val FORMAT_HEX_COLOR = "#%06x"

@ColorInt
fun Int.invertIfDarkMode(context: Context?): Int{
    return if (context != null && context.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) getContrastColor(this) else this
}

private fun getContrastColor(@ColorInt color: Int): Int {
    // Counting the perceptive luminance - human eye favors green color...
    val a = COLOR_DEFAULT_VAL - (RED_MULTIPLIER * Color.red(color) + GREEN_MULTIPLIER * Color.green(color) + BLUE_MULTIPLIER * Color.blue(color)) / COLOR_DIVIDER_VAL
    return if (a < COLOR_THRESHOLD) color else Color.WHITE
}

/**
 *
 * @param context
 * @param idColor example R(dot)color(dot)your_color
 * @return with example (hashtag)FFFFFF hex color uppercase
 *
 */
fun getHexColorFromIdColor(context: Context, idColor: Int) : String {
    return try {
        String.format(FORMAT_HEX_COLOR, ContextCompat.getColor(context, idColor) and COLOR_WHITE).uppercase()
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}