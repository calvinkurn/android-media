package com.tokopedia.discovery.common.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

fun Float.toSp(): Float = Resources.getSystem().displayMetrics.scaledDensity * this

fun Float.toDpInt(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Float.toDpFloat(): Float = this * Resources.getSystem().displayMetrics.density