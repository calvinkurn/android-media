package com.tokopedia.discovery.common.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

/**
 * created by Dhaba
 */
fun Float.toSp(): Float = Resources.getSystem().displayMetrics.scaledDensity * this

fun Float.toDpInt(context: Context): Int =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics)
        .toInt()

fun Float.toDpFloat(context: Context): Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics)