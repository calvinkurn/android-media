package com.tokopedia.video_widget.util

import android.content.Context

internal object DimensionUtils {
    fun getDensityMatrix(context: Context): Float {
        return context.resources.displayMetrics.density
    }

    fun getDpFromInt(context: Context, size: Int) : Float {
        return getDensityMatrix(context) * size
    }
}