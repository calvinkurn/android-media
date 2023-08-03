package com.tokopedia.flight.common.util

import android.content.res.Resources

fun Float.toDpInt(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
