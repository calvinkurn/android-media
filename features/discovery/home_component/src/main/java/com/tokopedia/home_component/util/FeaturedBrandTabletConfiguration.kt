package com.tokopedia.home_component.util

import android.content.Context
import androidx.annotation.LayoutRes
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.home_component.R

/**
 * Created by yfsx on 31/08/21.
 */
object FeaturedBrandTabletConfiguration {

    const val SPAN_COUNT = 4
    private const val SPAN_SPACING_MOBILE = 8
    private const val SPAN_SPACING_TABLET = 0
    @LayoutRes
    private val LAYOUT_MOBILE = R.layout.layout_featured_brand_item_mobile
    @LayoutRes
    private val LAYOUT_TABLET = R.layout.layout_featured_brand_item_tablet

    fun getSpanSpacing(context: Context?): Int {
        context?.let {
            return if (DeviceScreenInfo.isTablet(context)) SPAN_SPACING_TABLET else SPAN_SPACING_MOBILE
        }
        return SPAN_SPACING_MOBILE
    }

    fun getLayout(context: Context?): Int {
        context?.let {
            return if (DeviceScreenInfo.isTablet(context)) LAYOUT_TABLET else LAYOUT_MOBILE
        }
        return LAYOUT_MOBILE
    }

}