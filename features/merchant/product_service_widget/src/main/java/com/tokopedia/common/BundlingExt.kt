package com.tokopedia.common

import android.graphics.PorterDuff
import androidx.core.content.ContextCompat
import com.tokopedia.product_service_widget.R
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.R as unifycomponentsR

// This is short term solution, the ideal way should be handled by unify component
fun Label.forceLightRed() {
    setTextColor(ContextCompat.getColor(context, R.color.dms_static_light_RN500))
    val drawable = ContextCompat.getDrawable(context, unifycomponentsR.drawable.label_bg)
    drawable?.setColorFilter(context.resources.getColor(R.color.dms_static_light_RN100), PorterDuff.Mode.SRC_ATOP)

    setBackgroundDrawable(drawable)
}
