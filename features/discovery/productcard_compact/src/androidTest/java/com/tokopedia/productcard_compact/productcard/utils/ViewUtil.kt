package com.tokopedia.productcard_compact.productcard.utils

import android.content.Context
import android.util.TypedValue

internal object ViewUtil {
    fun getDpFromDimen(context: Context, id: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            context.resources.getDimension(id),
            context.resources.displayMetrics
        )
    }
}
