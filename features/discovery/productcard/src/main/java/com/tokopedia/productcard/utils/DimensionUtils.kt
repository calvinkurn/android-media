package com.tokopedia.productcard.utils

import android.content.Context

internal object DimensionUtils {
    fun getDensityMatrix(context: Context): Float {
        return context.resources.displayMetrics.density
    }
}