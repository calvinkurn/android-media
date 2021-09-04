package com.tokopedia.imagepicker_insta

import android.content.res.Resources

fun Int.toPx(): Float {
    return (this * Resources.getSystem().displayMetrics.density)
}