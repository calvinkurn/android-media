package com.tokopedia.home_component.util

import android.content.Context
import com.tokopedia.device.info.DeviceScreenInfo

/**
 * Created by yfsx on 31/08/21.
 */
object DynamicChannelTabletConfiguration {

    private const val SPAN_COUNT_2x2_NORMAL = 2
    private const val SPAN_COUNT_2x2_TABLET = 4
    private const val SPAN_SPACING_2x2_TABLET = 10
    private const val SPAN_SPACING_2x2_NORMAL = 10

    fun getSpanCountFor2x2(context: Context): Int {
        return if (DeviceScreenInfo.isTablet(context)) SPAN_COUNT_2x2_TABLET else SPAN_COUNT_2x2_NORMAL
    }

    fun getSpanCountForHomeRecommendationAdapter(context: Context): Int {
        return getSpanCountFor2x2(context)
    }

    fun getSpacingSpaceFor2x2(context: Context): Int {
        return if (DeviceScreenInfo.isTablet(context)) SPAN_SPACING_2x2_TABLET else SPAN_SPACING_2x2_NORMAL
    }
}