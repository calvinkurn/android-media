package com.tokopedia.play.view.type

import android.app.Activity
import androidx.window.layout.WindowMetricsCalculator

/**
 * Created by kenny.hadisaputra on 10/01/23
 */
data class ScreenOrientation2(
    val orientation: ScreenOrientation,
    val widthSizeClass: SizeClass,
    val heightSizeClass: SizeClass,
) {
    val isLandscape: Boolean
        get() = orientation == ScreenOrientation.Landscape || orientation == ScreenOrientation.ReversedLandscape

    val isPortrait: Boolean
        get() = orientation == ScreenOrientation.Portrait || orientation == ScreenOrientation.ReversedPortrait

    companion object {

        fun get(activity: Activity): ScreenOrientation2 {
            val metrics = WindowMetricsCalculator.getOrCreate()
                .computeCurrentWindowMetrics(activity)
            val widthDp = metrics.bounds.width() /
                activity.resources.displayMetrics.density
            val heightDp = metrics.bounds.height() /
                activity.resources.displayMetrics.density

            return ScreenOrientation2(
                orientation = ScreenOrientation.getByInt(
                    activity.resources.configuration.orientation,
                ),
                widthSizeClass = when {
                    widthDp < 600f -> SizeClass.Compact
                    widthDp < 840f -> SizeClass.Medium
                    else -> SizeClass.Expanded
                },
                heightSizeClass = when {
                    heightDp < 480f -> SizeClass.Compact
                    heightDp < 900f -> SizeClass.Medium
                    else -> SizeClass.Expanded
                },
            )
        }
    }
}

val ScreenOrientation2.Default: ScreenOrientation2
    get() = ScreenOrientation2(
        ScreenOrientation.Unknown,
        SizeClass.Compact,
        SizeClass.Compact,
    )

enum class SizeClass {

    Compact,
    Medium,
    Expanded,
}

val SizeClass.isCompact: Boolean
    get() = this == SizeClass.Compact

val ScreenOrientation2.isCompact: Boolean
    get() = widthSizeClass == SizeClass.Compact || heightSizeClass == SizeClass.Compact
