package com.tokopedia.catalogcommon.util

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat

fun Int?.orDefaultColor(context: Context,default: Int = com.tokopedia.unifyprinciples.R.color.Unify_N150_20): Int {
    return this ?: default.colorResToInt(context)
}

fun String.stringHexColorParseToInt(alphaPercentage:Int = 100): Int {
    val colorValue = Color.parseColor(this)
    val alphaValue: Int = (alphaPercentage * 255) / 100
    return try {
        Color.argb(alphaValue, Color.red(colorValue), Color.green(colorValue), Color.blue(colorValue))
    }catch (e:Exception){
        Color.RED
    }
}

fun Int.colorResToInt(context: Context, alphaPercentage:Int = 100): Int {
    val colorValue = ContextCompat.getColor(context, this)
    val alphaValue: Int = (alphaPercentage * 255) / 100
    return Color.argb(alphaValue, Color.red(colorValue), Color.green(colorValue), Color.blue(colorValue))
}
 fun colorMapping(
    darkMode: Boolean,
    forDarkModeColor: String,
    forLightModeColor: String,
    alphaPercentageDarkModeColor: Int = 100,
    alphaPercentageLightModeColor: Int = 100,
 ): Int {
    return if (darkMode) {
        forDarkModeColor.stringHexColorParseToInt(alphaPercentageDarkModeColor)
    } else {
        forLightModeColor.stringHexColorParseToInt(alphaPercentageLightModeColor)
    }
}

fun colorMapping(
    darkMode: Boolean,
    forDarkModeColor: Int,
    forLightModeColor: Int
): Int {
    return if (darkMode) {
        forDarkModeColor
    } else {
        forLightModeColor
    }
}
