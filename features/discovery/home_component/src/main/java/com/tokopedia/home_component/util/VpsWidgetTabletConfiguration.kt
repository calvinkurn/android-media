package com.tokopedia.home_component.util

import android.content.Context
import com.tokopedia.device.info.DeviceScreenInfo

/**
 * Created by frenzel
 */
object VpsWidgetTabletConfiguration {
    private const val SPAN_COUNT_2x2 = 2
    private const val SPAN_COUNT_4x1 = 4

    fun getSpanCount(context: Context?): Int {
        context?.let {
            return if (DeviceScreenInfo.isTablet(context)) {
                SPAN_COUNT_4x1
            } else {
                SPAN_COUNT_2x2
            }
        }
        return SPAN_COUNT_2x2
    }
}