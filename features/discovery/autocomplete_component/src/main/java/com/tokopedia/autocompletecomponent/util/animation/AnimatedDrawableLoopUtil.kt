package com.tokopedia.autocompletecomponent.util.animation

import android.graphics.drawable.Drawable
import android.os.Build

interface AnimatedDrawableLoopUtil {
    fun startLoopAnimation(drawable: Drawable?)
    fun stopLoopAnimation(drawable: Drawable?)

    companion object {
        fun getInstance(): AnimatedDrawableLoopUtil {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                AnimatedDrawableMarshmallowOrAboveLoopUtil()
            } else {
                AnimatedDrawableCompatLoopUtil()
            }
        }
    }
}
