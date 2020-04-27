package com.tokopedia.kotlin.extensions.view

import android.content.res.Resources

fun Float?.orZero(): Float = this ?: 0f

fun Float.toDp(): Float = this / Resources.getSystem().displayMetrics.density

fun Float.toPx(): Float = this * Resources.getSystem().displayMetrics.density