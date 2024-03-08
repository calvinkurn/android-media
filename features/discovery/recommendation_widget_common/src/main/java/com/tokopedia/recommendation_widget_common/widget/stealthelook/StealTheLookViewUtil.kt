package com.tokopedia.recommendation_widget_common.widget.stealthelook

import android.content.Context
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.recommendation_widget_common.R as recommendation_widget_commonR

object StealTheLookViewUtil {
    fun getContainerWidth(context: Context): Int {
        val screenWidth = DeviceScreenInfo.getScreenWidth(context)
        val maxWidth = context.resources.getDimensionPixelOffset(recommendation_widget_commonR.dimen.steal_the_look_container_max_width)
        return screenWidth.coerceAtMost(maxWidth)
    }
}
