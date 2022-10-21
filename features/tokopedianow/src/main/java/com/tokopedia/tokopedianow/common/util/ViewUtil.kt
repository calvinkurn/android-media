package com.tokopedia.tokopedianow.common.util

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.widget.TextView

object ViewUtil {
    fun TextView.setDimenAsTextSize(id: Int) {
        setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            context.resources.getDimension(id)
        )
    }

    fun getDpFromDimen(context: Context, id: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            context.resources.getDimension(id),
            context.resources.displayMetrics
        )
    }

    fun safeParseColor(color: String, defaultColor: Int): Int {
        return try {
            Color.parseColor(color)
        }
        catch (throwable: Throwable) {
            throwable.printStackTrace()
            defaultColor
        }
    }
}
