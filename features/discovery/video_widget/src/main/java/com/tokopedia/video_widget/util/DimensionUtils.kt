package com.tokopedia.video_widget.util

import android.content.Context

internal object DimensionUtils {
    fun getDensityMatrix(context: Context): Float {
        return context.resources.displayMetrics.density
    }
}