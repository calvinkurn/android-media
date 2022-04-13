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

    fun getLayout(context: Context?): Int {
        context?.let {
//            return if (DeviceScreenInfo.isTablet(context)) {
//
//            } else

                LAYOUT_MOBILE
        }
        return LAYOUT_MOBILE
    }

    fun getSpacingSpaceForLego4Auto(): Int =
        SPAN_SPACING_2x2_LEGO_4_AUTO
}