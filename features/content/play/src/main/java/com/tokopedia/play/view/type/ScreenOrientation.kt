package com.tokopedia.play.view.type

import android.content.res.Configuration

/**
 * Created by jegul on 13/04/20
 */
enum class ScreenOrientation(private val orientationInt: Int) {

    Portrait(Configuration.ORIENTATION_PORTRAIT),
    Landscape(Configuration.ORIENTATION_LANDSCAPE),
    Unknown(-1);

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