package com.tokopedia.home_component.util

import android.content.Context
import androidx.annotation.LayoutRes
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.home_component.R

/**
 * Created by dhaba
 */
object Lego4AutoTabletConfiguration {
    private val SPAN_SPACING_2x2_LEGO_4_AUTO = 4f.toDpInt()
    @LayoutRes
    private val LAYOUT_MOBILE = R.layout.layout_lego_4_auto_item_mobile
    @LayoutRes
    private val LAYOUT_TABLET = R.layout.layout_lego_4_auto_item_tablet

    private const val SPAN_COUNT_2x2 = 2

    fun getLayout(context: Context?): Int {
        context?.let {
            return if (DeviceScreenInfo.isTablet(context)) {
                LAYOUT_TABLET
            } else
                LAYOUT_MOBILE
        }
        return LAYOUT_MOBILE
    }

    fun getSpacingSpaceForLego4Auto(): Int =
        SPAN_SPACING_2x2_LEGO_4_AUTO

    fun getSpanCount(context: Context?): Int {
        context?.let {
            return if (DeviceScreenInfo.isTablet(context)) {
                context.resources.getInteger(R.integer.span_count)
            } else {
                SPAN_COUNT_2x2
            }
        }
        return SPAN_COUNT_2x2
    }
}