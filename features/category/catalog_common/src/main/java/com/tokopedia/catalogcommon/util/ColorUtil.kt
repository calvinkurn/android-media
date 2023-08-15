package com.tokopedia.catalogcommon.util

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.tokopedia.catalogcommon.R

fun Int?.orDefaultColor(context: Context,default: Int = R.color.Unify_N150_20): Int {
    return this ?: default.colorResToInt(context)
}

fun String.stringHexColorParseToInt(alphaPercentage:Int = 100): Int {
    val colorValue = Color.parseColor(this)
    val alphaValue: Int = (alphaPercentage * 255) / 100
    return Color.argb(alphaValue, Color.red(colorValue), Color.green(colorValue), Color.blue(colorValue))
}

fun Int.colorResToInt(context: Context, alphaPercentage:Int = 100): Int {
    val colorValue = ContextCompat.getColor(context, this)
    val alphaValue: Int = (alphaPercentage * 255) / 100
    return Color.argb(alphaValue, Color.red(colorValue), Color.green(colorValue), Color.blue(colorValue))
}
