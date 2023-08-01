package com.tokopedia.editor.ui.gesture.util

import android.graphics.PointF
import kotlin.math.sqrt

class Vector2D : PointF() {

    fun normalize() {
        val length = sqrt(x * x + y * y)
        x /= length
        y /= length
    }
}
