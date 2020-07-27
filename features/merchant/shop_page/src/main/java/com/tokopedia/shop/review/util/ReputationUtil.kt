package com.tokopedia.shop.review.util

import android.content.Context
import android.util.TypedValue
import java.util.*

object ReputationUtil {
    fun convertMapObjectToString(map: HashMap<String, Any>): HashMap<String, String> {
        val newMap = HashMap<String, String>()
        for ((key, value) in map) {
            newMap[key] = value.toString()
        }
        return newMap
    }

    fun DptoPx(context: Context, dp: Int): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics)
    }
}