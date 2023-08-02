package com.tokopedia.editor.ui.gesture.util

import kotlin.math.atan2

object VectorAngle {

    @JvmStatic
    fun get(vector1: Vector2D, vector2: Vector2D): Float {
        vector1.normalize()
        vector2.normalize()

        return ((180.0 / Math.PI) * (atan2(vector2.y, vector2.x) - atan2(vector1.y, vector1.x))).toFloat()
    }
}
