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
    private val SPAN_SPACING_2x2_LEGO_4_AUTO_PADDING = 8f.toDpInt()
    @LayoutRes
    private val LAYOUT_MOBILE = R.layout.layout_lego_4_auto_item_mobile
    @LayoutRes
    private val LAYOUT_TABLET = R.layout.layout_lego_4_auto_item_tablet
    @LayoutRes
    val LAYOUT_MOBILE_PADDING = R.layout.layout_lego_4_auto_item_mobile_padding
    @LayoutRes
    val LAYOUT_TABLET_PADDING = R.layout.layout_lego_4_auto_item_tablet_padding

    private const val SPAN_COUNT_2x2 = 2

    fun getLayout(context: Context?, borderStyle: String): Int {
        val isUsingPadding = borderStyle == ChannelStyleUtil.BORDER_STYLE_PADDING
        val tabletLayout = if(isUsingPadding) LAYOUT_TABLET_PADDING else LAYOUT_TABLET
        val mobileLayout = if(isUsingPadding) LAYOUT_MOBILE_PADDING else LAYOUT_MOBILE
        context?.let {
            return if (DeviceScreenInfo.isTablet(context)) {
                tabletLayout
            } else
                mobileLayout
        }
        return mobileLayout
    }

    fun getSpacingSpaceForLego4Auto(isUsingPadding: Boolean): Int {
        return if(isUsingPadding) SPAN_SPACING_2x2_LEGO_4_AUTO_PADDING else SPAN_SPACING_2x2_LEGO_4_AUTO
    }

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
