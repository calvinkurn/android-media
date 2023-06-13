package com.tokopedia.play.view.type

import android.content.pm.ActivityInfo
import android.content.res.Configuration

/**
 * Created by jegul on 13/04/20
 */
enum class ScreenOrientation(
        private val orientationInt: Int,
        val requestedOrientation: Int,
        val value: String
) {

    Portrait(Configuration.ORIENTATION_PORTRAIT, ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT, "portrait"),
    Landscape(Configuration.ORIENTATION_LANDSCAPE, ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE, "landscape"),

    /**
     * The int in the reversed mode is incorrect
     * This should not be used to match by int
     */
    ReversedPortrait(Configuration.ORIENTATION_PORTRAIT * -1, ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT, "portrait"),
    ReversedLandscape(Configuration.ORIENTATION_LANDSCAPE * -1, ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE, "landscape"),

    Unknown(Configuration.ORIENTATION_UNDEFINED, ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED, "unknown");

    val isLandscape: Boolean
        get() = this == Landscape || this == ReversedLandscape

    val isPortrait: Boolean
        get() = this == Portrait || this == ReversedPortrait

    companion object {
        private val values = values()

        fun getByInt(orientationInt: Int): ScreenOrientation {
            values.forEach {
                if (it.orientationInt == orientationInt) return it
            }
            return Unknown
        }
    }
}
