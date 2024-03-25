package com.tokopedia.catalogcommon.util

import android.content.Context
import android.graphics.Color
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

fun Int?.orDefaultColor(context: Context,default: Int = unifyprinciplesR.color.Unify_Static_White): Int {
    return this ?: default.colorResToInt(context)
}

fun String.stringHexColorParseToInt(alphaPercentage:Int = 100): Int {
    return try {
        val colorValue = Color.parseColor(this)
        val alphaValue: Int = (alphaPercentage * 255) / 100
        Color.argb(alphaValue, Color.red(colorValue), Color.green(colorValue), Color.blue(colorValue))
    }catch (e:Exception){
        FirebaseCrashlytics.getInstance().recordException(e)
        Color.TRANSPARENT
    }
}

fun Int.alphaColor(alphaPercentage:Int = 100): Int {
    return try {
        val alphaValue: Int = (alphaPercentage * 255) / 100
        Color.argb(alphaValue, Color.red(this), Color.green(this), Color.blue(this))
    }catch (e:Exception){
        FirebaseCrashlytics.getInstance().recordException(e)
        Color.TRANSPARENT
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

fun getColorDarkMode(
    context: Context,
    darkMode: Boolean,
    @ColorRes darkModeResColor: Int,
    @ColorRes lightModeResColor: Int,
    darkModeAlpha: Int = 100,
    lightModeAlpha: Int = 100,
): Int {
    val darkModeColor = darkModeResColor.colorResToInt(context, darkModeAlpha)
    val lightModeColor = lightModeResColor.colorResToInt(context, lightModeAlpha)
    return if (darkMode) {
        darkModeColor
    } else {
        lightModeColor
    }
}
