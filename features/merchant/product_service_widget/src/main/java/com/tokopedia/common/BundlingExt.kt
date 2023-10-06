package com.tokopedia.common

import android.content.Context
import androidx.core.content.ContextCompat
import android.graphics.PorterDuff
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.R

fun Label.forceLightRed() {
    setTextColor(ContextCompat.getColor(context, R.color.dms_static_light_RN500))
    val drawable = ContextCompat.getDrawable(context, R.drawable.label_bg)
    drawable?.setColorFilter(context.resources.getColor(R.color.dms_static_light_RN100), PorterDuff.Mode.SRC_ATOP)

    setBackgroundDrawable(drawable)
}
