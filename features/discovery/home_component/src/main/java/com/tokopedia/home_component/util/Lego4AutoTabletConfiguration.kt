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
    private val LAYOUT_TABLET_BELOW_768 = R.layout.layout_lego_4_auto_item_tablet_below_768
    @LayoutRes
    private val LAYOUT_TABLET_ABOVE_768 = R.layout.layout_lego_4_auto_item_tablet_above_768

    private const val SPAN_COUNT_2x2 = 2
    private const val LAYOUT_WIDTH_767 = 768
    private const val LAYOUT_WIDTH_700 = 700

    fun getLayout(context: Context?): Int {
        context?.let {
            return if (DeviceScreenInfo.isTablet(context)) {
                val width = context.resources.displayMetrics.widthPixels
                val density = context.resources.displayMetrics.density
                val widthPx = (width / density).toInt()
                when {
                    widthPx < LAYOUT_WIDTH_700 -> {
                        LAYOUT_MOBILE
                    }
                    widthPx in LAYOUT_WIDTH_700..LAYOUT_WIDTH_767 -> {
                        LAYOUT_TABLET_BELOW_768
                    }
                    else -> {
                        LAYOUT_TABLET_ABOVE_768
                    }
                }
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