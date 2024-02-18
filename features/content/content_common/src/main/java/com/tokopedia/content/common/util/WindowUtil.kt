package com.tokopedia.content.common.util

import android.app.Activity
import androidx.window.layout.WindowMetricsCalculator

enum class WindowWidthSizeClass {
    Compact,
    Medium,
    Expanded;

    companion object {
        internal fun of(widthDp: Float): WindowWidthSizeClass {
            return when {
                widthDp < 600f -> Compact
                widthDp < 840f -> Medium
                else -> Expanded
            }
        }
    }
}

enum class WindowHeightSizeClass {
    Compact,
    Medium,
    Expanded;

    companion object {
        internal fun of(heightDp: Float): WindowHeightSizeClass {
            return when {
                heightDp < 480f -> Compact
                heightDp < 900f -> Medium
                else -> Expanded
            }
        }
    }
}

data class WindowSizeClass(
    val widthSizeClass: WindowWidthSizeClass,
    val heightSizeClass: WindowHeightSizeClass
)

fun Activity.calculateWindowSizeClass(): WindowSizeClass {
    val metrics = WindowMetricsCalculator.getOrCreate()
        .computeCurrentWindowMetrics(this)
    val density = resources.displayMetrics.density
    val widthDp = metrics.bounds.width() / density
    val heightDp = metrics.bounds.height() / density
    return WindowSizeClass(
        WindowWidthSizeClass.of(widthDp),
        WindowHeightSizeClass.of(heightDp)
    )
}
